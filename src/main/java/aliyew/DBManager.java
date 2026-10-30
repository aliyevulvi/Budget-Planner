package aliyew;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DBManager {

	private static final String LINK_STRING = System.getenv("DB_URL");
	private static final Logger logger = LogManager.getLogger(DBManager.class.getName());

	public static void main(String[] args) {

		try (Connection conn = DriverManager.getConnection(System.getenv("DB_URL"));
					Statement stmt = conn.createStatement()) {


			stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS app_schema;");

			stmt.executeUpdate("SET search_path TO app_schema;");

		} catch (Exception e) {
			e.printStackTrace();
		}



		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "CREATE TABLE IF NOT EXISTS app_schema.tb_records ( "
							  + "record_id VARCHAR(36) PRIMARY KEY, "
							  + "record_name VARCHAR(20) UNIQUE NOT NULL, "
							  + "record_income DOUBLE PRECISION NOT NULL, "
							  + "record_saving DOUBLE PRECISION NOT NULL, "
							  + "record_ts TIMESTAMP NOT NULL "
							  + ");";
			String sqlQuery2 = "CREATE TABLE IF NOT EXISTS app_schema.tb_expenses ( "
							   + " expense_id VARCHAR(36) PRIMARY KEY, "
							   + " expense_record_id VARCHAR(36) REFERENCES app_schema.tb_records(record_id) ON DELETE CASCADE NOT NULL, "
							   + "expense_date DATE NOT NULL, "
							   + "expense_cat VARCHAR(50) NOT NULL, "
							   + "expense_amt DOUBLE PRECISION NOT NULL);";

			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = conn.prepareStatement(sqlQuery2);
			pstmt.executeUpdate();
			pstmt.close();

			conn.close();

		} catch (SQLException e) {
			logger.severe(e.getMessage());

		}
	}

	public static String sync() {
		ArrayList<Synchronization> syncQueue = JsonManager.getOps();

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(LINK_STRING);
			conn.setAutoCommit(false);

			for (Synchronization syncOp : syncQueue) {
				switch (syncOp.getOp()) {

				case "CREATE_RECORD" :
					createRecord(conn, syncOp.getRecord());
					break;
				case "UPDATE_RECORD" :
					updateRecord(conn, syncOp.getRecord());
					break;
				case "DELETE_RECORD" :
					deleteRecord(conn, syncOp.getRecord());
					break;
				case "CREATE_EXPENSE" :
					createExpense(conn, syncOp.getExpense());
					break;
				case "UPDATE_EXPENSE" :
					updateExpense(conn, syncOp.getExpense());
					break;
				case "DELETE_EXPENSE" :
					deleteExpense(conn, syncOp.getExpense());
					break;
				}
			}

			syncQueue.clear();
			conn.commit();
			JsonManager.setAllOps(syncQueue);

			return "Synchronization Completed Successfully!";

		} catch (Exception e) {
			logger.severe(() -> e.getMessage());
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception ex) {

			}

			return "Synchronization Failed : " + e.getMessage();
		} finally {
			try {
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (Exception ex) {

			}

		}
	}

	public static ArrayList<Record> getRecords() {
		ArrayList<Record> allRecords = new ArrayList<>();
		String sqlQuery = "SELECT * FROM app_schema.tb_records;";

		try {
			try (Connection conn = DriverManager.getConnection(LINK_STRING); PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery); ResultSet rs = preparedStatement.executeQuery()) {

				while (rs.next()) {
					allRecords.add(new Record(rs.getString("record_id"), rs.getString("record_name"),
											  rs.getTimestamp("record_ts").toInstant(), rs.getDouble("record_income"),
											  rs.getDouble("record_saving")));
				}
			}

			return allRecords;

		} catch (SQLException e) {
			logger.severe(() -> e.getMessage() + " | " + e.getSQLState());
			allRecords.clear();
			return allRecords;
		}

	}

	public static void createRecord(Connection conn, Record newRecord) throws SQLException {

		String insertQuery = "INSERT INTO app_schema.tb_records (record_id, record_name, record_income, record_saving, record_ts) VALUES (?, ?, ?, ?, ?);";
		try (PreparedStatement pStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

			pStatement.setString(1, newRecord.getRecordId());
			pStatement.setString(2, newRecord.getRecordName());
			pStatement.setDouble(3, newRecord.getRecordIncome());
			pStatement.setDouble(4, newRecord.getRecordSaving());
			pStatement.setTimestamp(5, Timestamp.from(newRecord.getRecordCreationDate()));
			pStatement.executeUpdate();

		}

	}

	public static void updateRecord(Connection conn, Record rec) throws SQLException {

		String sqlQuery = "UPDATE app_schema.tb_records SET record_name = ?, record_income = ?, record_saving = ? WHERE record_id = ?;";
		try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

			pstmt.setString(1, rec.getRecordName());
			pstmt.setDouble(2, rec.getRecordIncome());
			pstmt.setDouble(3, rec.getRecordSaving());
			pstmt.setString(4, rec.getRecordId());
			int affectedRows = pstmt.executeUpdate();
		}
	}


	public static void deleteRecord(Connection conn, Record rec) throws SQLException {

		String sqlQuery = "DELETE FROM app_schema.tb_records WHERE record_id = ?;";
		try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			pstmt.setString(1, rec.getRecordId());
			pstmt.executeUpdate();
		}

	}

	public static ArrayList<Expense> getExpenses(Record rec) throws SQLException {
		ArrayList<Expense> allExpenses = new ArrayList<>();
		String sqlQuery = "SELECT * FROM app_schema.tb_expenses WHERE expense_record_id = ?;";

		try (Connection conn = DriverManager.getConnection(LINK_STRING); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

			pstmt.setString(1, rec.getRecordId());

			try (ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {
					allExpenses.add(new Expense(rs.getString("expense_id"), rs.getString("expense_record_id"), rs.getDate("expense_date").toLocalDate(), rs.getString("expense_cat"), rs.getDouble("expense_amt")));
				}
			}

			return allExpenses;

		} catch (SQLException e) {
			logger.severe(()->e.getMessage() + " | " + e.getSQLState());
			allExpenses.clear();
			return allExpenses;
		}

	}

	public static void createExpense(Connection conn, Expense exp) throws SQLException {

		String insertQuery = "INSERT INTO app_schema.tb_expenses(expense_id, expense_record_id, expense_date, expense_cat, expense_amt) "
							 + "VALUES (?,?,?,?,?);";
		try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, exp.getExpenseId());
			pstmt.setString(2, exp.getExpenseRecordId());
			pstmt.setObject(3, exp.getExpenseDate());
			pstmt.setString(4, exp.getExpenseCat());
			pstmt.setDouble(5, exp.getExpenseAmt());
			pstmt.executeUpdate();

		}
	}

	public static void updateExpense(Connection conn, Expense expense) throws SQLException {

		String sqlQuery = "UPDATE app_schema.tb_expenses SET expense_date = ?, expense_cat = ?, expense_amt = ? WHERE expense_id = ?;";
		try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			pstmt.setObject(1, expense.getExpenseDate());
			pstmt.setString(2, expense.getExpenseCat());
			pstmt.setDouble(3, expense.getExpenseAmt());
			pstmt.setString(4, expense.getExpenseId());
			int affectedRows = pstmt.executeUpdate();

		}
	}

	public static void deleteExpense(Connection conn, Expense expense) throws SQLException {

		String sqlQuery = "DELETE FROM app_schema.tb_expenses WHERE expense_id = ?;";
		try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

			pstmt.setString(1, expense.getExpenseId());
			pstmt.executeUpdate();

		}

	}

	public static void deleteExpense(Connection conn, Record record) throws SQLException {

		String sqlQuery = "DELETE FROM app_schema.tb_expenses WHERE expense_record_id = ?;";
		try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

			pstmt.setString(1, record.getRecordId());
			pstmt.executeUpdate();

		}

	}



}
