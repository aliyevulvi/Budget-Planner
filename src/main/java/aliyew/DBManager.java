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

			System.out.println("Özel şema başarıyla oluşturuldu ve aktif edildi!");

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
							   + " expense_record_id VARCHAR(36) REFERENCES tb_records(record_id) ON DELETE CASCADE, "
							   + "expense_date DATE NOT NULL, "
							   + "expense_cat VARCHAR(50) NOT NULL, "
							   + "expense_amt DOUBLE PRECISION NOT NULL);";

			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sqlQuery2);
			pstmt.executeUpdate();

			conn.close();

		} catch (SQLException e) {
			logger.severe(e.getMessage());

		}
	}

	public static boolean connectDB() {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			conn.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public static ArrayList<Record> getRecords() {
		ArrayList<Record> allRecords = new ArrayList<>();

		try {
			try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
				String sqlQuery = "SELECT * FROM tb_records;";
				PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
				ResultSet rs = preparedStatement.executeQuery();

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

	public static String createRecord(Record newRecord) {

		try {
			try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
				String insertQuery = "INSERT INTO tb_records (record_name, record_income, record_saving, record_ts) VALUES (?, ?, ?, ?);";

				PreparedStatement pStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
				pStatement.setString(1, newRecord.getRecordId());
				pStatement.setString(2, newRecord.getRecordName());
				pStatement.setDouble(3, newRecord.getRecordIncome());
				pStatement.setDouble(4, newRecord.getRecordSaving());
				pStatement.setTimestamp(5, Timestamp.from(newRecord.getRecordCreationDate()));
				pStatement.executeUpdate();

			}

			return "SUCCESS";


		} catch (SQLException e) {
			logger.severe(() -> e.getMessage() + " | " + e.getSQLState());
			return "FAIL";

		}
	}

	public static String updateRecord(Record rec) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "UPDATE tb_records SET record_name = ?, record_income = ?, record_saving = ? WHERE record_id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setString(1, rec.getRecordName());
			pstmt.setDouble(2, rec.getRecordIncome());
			pstmt.setDouble(3, rec.getRecordSaving());
			pstmt.setString(4, rec.getRecordId());
			int affectedRows = pstmt.executeUpdate();

			conn.close();

			if (affectedRows == 1) {
				rec.setRecordSync(true);
				return "SUCCESS";
			}
			return "FAIL";

		} catch (SQLException e) {
			logger.severe(()->e.getMessage() + " | " + e.getSQLState());
			return "FAIL";
		}
	}


	public static String deleteRecord(Record rec) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "DELETE FROM tb_records WHERE record_id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setString(1, rec.getRecordId());
			int affectedRows = pstmt.executeUpdate();


			conn.close();
			if (affectedRows == 1) {
				rec.setRecordSync(true);
				return "SUCCESS";
			}
			return "FAIL";
		} catch (SQLException e) {
			logger.severe(()->e.getMessage() + " | " + e.getSQLState());
			return "FAIL";

		}

	}

	public static ArrayList<Expense> getExpenses(Record rec) {
		ArrayList<Expense> allExpenses = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "SELECT * FROM tb_expenses WHERE expense_record_id = ?;";

			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setString(1, rec.getRecordId());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				allExpenses.add(new Expense(rs.getString("expense_id"), rs.getString("expense_record_id"), rs.getDate("expense_date").toLocalDate(), rs.getString("expense_cat"), rs.getDouble("expense_amt")));
			}
			conn.close();
			return allExpenses;

		} catch (SQLException e) {
			logger.severe(()->e.getMessage() + " | " + e.getSQLState());
			allExpenses.clear();
			return allExpenses;
		}

	}

	public static String createExpense(Expense exp) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {

			String insertQuery = "INSERT INTO tb_expenses(expense_record_id, expense_date, expense_cat, expense_amt) "
								 + "VALUES (?,?,?,?);";
			PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, exp.getExpenseId());
			pstmt.setString(2, exp.getExpenseRecordId());
			pstmt.setObject(3, exp.getExpenseDate());
			pstmt.setString(4, exp.getExpenseCat());
			pstmt.setDouble(5, exp.getExpenseAmt());
			pstmt.executeUpdate();

			conn.close();

			return "SUCCESS";

		} catch (SQLException e) {
			logger.severe(() -> e.getMessage() + " | " + e.getSQLState());
			return "FAIL";
		}
	}

	public static String updateExpense(Expense expense) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "UPDATE tb_expenses SET expense_date = ?, expense_cat = ?, expense_amt = ? WHERE expense_id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setObject(1, expense.getExpenseDate());
			pstmt.setString(2, expense.getExpenseCat());
			pstmt.setDouble(3, expense.getExpenseAmt());
			pstmt.setString(4, expense.getExpenseId());
			int affectedRows = pstmt.executeUpdate();
			conn.close();

			if (affectedRows == 1) {
				expense.setExpenseSync(true);
				return "SUCCESS";
			}
			return "FAIL";
		} catch (SQLException e) {
			logger.severe(()->e.getMessage() + " | " + e.getSQLState());
			return "FAIL";
		}
	}

	public static String deleteExpense(Expense expense) {

		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "DELETE FROM tb_expenses WHERE expense_id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setString(1, expense.getExpenseId());
			int affectedRows = pstmt.executeUpdate();
			conn.close();

			if (affectedRows == 1) {
				expense.setExpenseSync(true);
				return "SUCCESS";
			}
			return "FAIL";
		} catch (SQLException e) {
			logger.severe(()->e.getMessage() + " | " + e.getSQLState());
			return "FAIL";
		}
	}

	public static String deleteExpense(Record record) {

		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "DELETE FROM tb_expenses WHERE expense_record_id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setString(1, record.getRecordId());
			pstmt.executeUpdate();
			conn.close();


			return "SUCCESS";
		} catch (SQLException e) {
			logger.severe(()->e.getMessage() + " | " + e.getSQLState());
			return "FAIL";
		}
	}



}
