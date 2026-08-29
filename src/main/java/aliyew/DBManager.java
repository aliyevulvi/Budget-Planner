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
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "CREATE TABLE IF NOT EXISTS tb_records ( "
							  + "record_id SERIAL PRIMARY KEY, "
							  + "record_name VARCHAR(20) UNIQUE NOT NULL, "
							  + "record_income DOUBLE PRECISION NOT NULL, "
							  + "record_saving DOUBLE PRECISION NOT NULL, "
							  + "record_ts TIMESTAMP NOT NULL "
							  + ");";
			String sqlQuery2 = "CREATE TABLE IF NOT EXISTS tb_expenses ( "
							+ " expense_id SERIAL PRIMARY KEY, "
							+ " expense_record_id INT REFERENCES tb_records(record_id) ON DELETE CASCADE, "
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
			// logger.severe(e.getMessage());
			return false;
		}
	}

	public static String addIncome(Record rec, double addAmt) {
	    try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
	        String sqlQuery = "UPDATE tb_records SET record_income = ? WHERE record_id = ?;";
	        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
	        pstmt.setDouble(1, rec.getRecordIncome()+addAmt);
	        pstmt.setInt(2, rec.getRecordId());
	        pstmt.executeUpdate();
	        
	        conn.close();
	        
	        return "Update Income Successfully";
	    } catch (SQLException e) {
			logger.severe(() -> e.getMessage() + " | " + e.getSQLState());
	        return "Update Income Failed";
	    }
	}

	public static String createNewRecord(Record newRecord) {

		try {
			try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
				String insertQuery = "INSERT INTO tb_records (record_name, record_income, record_saving, record_ts) VALUES (?, ?, ?, ?);";

				PreparedStatement pStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
				pStatement.setString(1, newRecord.getRecordName());
				pStatement.setDouble(2, newRecord.getRecordIncome());
				pStatement.setDouble(3, newRecord.getRecordSaving());
				pStatement.setTimestamp(4, Timestamp.valueOf(newRecord.getCreationDate()));
				pStatement.executeUpdate();

				ResultSet rs = pStatement.getGeneratedKeys();

				if (rs.next()) {
					newRecord.setRecordId(rs.getInt(1));
					newRecord.setRecordSync(true);
				}


			}

			return "Create Record Successfully";

		} catch (SQLException e) {
			logger.severe(() -> e.getMessage() + " | " + e.getSQLState());
			if (e.getSQLState().equals("23505")) {
				return "Create Record Failed ("+newRecord.getRecordName() + ") already created!";
			} else {
				return "Create Record Failed";
			}
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
					allRecords.add(new Record(rs.getInt("record_id"), rs.getString("record_name"),
							rs.getTimestamp("record_ts") + "", rs.getDouble("record_income"),
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

	public static String createExpense(Record rec, Expense exp) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {

			String insertQuery = "INSERT INTO tb_expenses(expense_record_id, expense_date, expense_cat, expense_amt) "
								 + "VALUES (?,?,?,?);";
			PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, rec.getRecordId());
			pstmt.setObject(2, exp.getExpenseDate());
			pstmt.setString(3, exp.getExpenseCat());
			pstmt.setDouble(4, exp.getExpenseAmt());
			pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				exp.setExpenseId(rs.getInt(1));
				exp.setExpenseSync(true);
			}
			conn.close();

			return "Create Expense Successfully";

		} catch (SQLException e) {
			logger.severe(() -> e.getMessage() + " | " + e.getSQLState());
			return "Create Expense Failed";
		}
	}

	public static void adminDeleteRecords() {

		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {

			String sqlQuery = "DROP TABLE tb_records;";
			PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
			preparedStatement.executeUpdate();

			conn.close();

		} catch (SQLException e) {
			logger.severe(()->e.getMessage()+" | "+e.getSQLState());
		}
	}

	public static ArrayList<Expense> getExpenses(Record rec) {
		ArrayList<Expense> allExpenses = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "SELECT * FROM tb_expenses WHERE expense_record_id = ?;";

			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setInt(1, rec.getRecordId());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				allExpenses.add(new Expense(rs.getInt("expense_id"), rs.getInt("expense_record_id"), rs.getDate("expense_date").toLocalDate(), rs.getString("expense_cat"), rs.getDouble("expense_amt")));
			}
			conn.close();
			return allExpenses;

		} catch (SQLException e) {
			logger.severe(()->e.getMessage()+" | "+e.getSQLState());
			allExpenses.clear();
			return allExpenses;
		}

	}

	public static String deleteExpense(int expenseId) {

		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "DELETE FROM tb_expenses WHERE expense_id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setInt(1, expenseId);
			pstmt.executeUpdate();
			conn.close();
			return "Delete Expense Successfully";
		} catch (SQLException e) {
			logger.severe(()->e.getMessage()+" | "+e.getSQLState());
			return "Delete Expense Failed";
		}
	}

	public static String updateRecord(Record rec, String newName, double income, double saving) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "UPDATE tb_records SET record_name = ?, record_income = ?, record_saving = ? WHERE record_id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setString(1, newName);
			pstmt.setDouble(2, income);
			pstmt.setDouble(3, saving);
			pstmt.setInt(4, rec.getRecordId());
			pstmt.executeUpdate();
			
            conn.close();

			rec.setRecordSync(true);
			return "Update Record Successfully";

		} catch (SQLException e) {
			logger.severe(()->e.getMessage()+" | "+e.getSQLState());
			return "Update Record Failed";
		}
	}

	public static String updateExpense(Expense expense) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "UPDATE tb_expenses SET expense_date = ?, expense_cat = ?, expense_amt = ? WHERE expense_id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setObject(1, expense.getExpenseDate());
			pstmt.setString(2, expense.getExpenseCat());
			pstmt.setDouble(3, expense.getExpenseAmt());
			pstmt.setInt(4, expense.getExpenseId());
			pstmt.executeUpdate();
			conn.close();

			expense.setExpenseSync(true);

			return "Update Expense Successfully";
		} catch (SQLException e) {
			logger.severe(()->e.getMessage()+" | "+e.getSQLState());
			return "Update Expense Failed";
		}
	}

    public static String deleteRecord(Record rec) {
        try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
            String sqlQuery = "DELETE FROM tb_records WHERE record_id = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setInt(1, rec.getRecordId());
            pstmt.executeUpdate();


            conn.close();

            return "Delete Record Successfully";
        } catch (SQLException e) {
			logger.severe(()->e.getMessage()+" | "+e.getSQLState());
            return "Delete Record Failed";

        }

    }



}
