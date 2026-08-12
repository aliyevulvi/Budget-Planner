package aliyew;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBManager {

    private static final String LINK_STRING = "";
    
	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "CREATE TABLE IF NOT EXISTS tb_records ( "
							  + "record_id SERIAL PRIMARY KEY, "
							  + "record_name VARCHAR(20) UNIQUE NOT NULL, "
							  + "record_income INTEGER NOT NULL, "
							  + "record_saving INTEGER NOT NULL, "
							  + "record_ts TIMESTAMP NOT NULL "
							  + ");";
			String sqlQuery2 = "CREATE TABLE IF NOT EXISTS tb_expenses ( "
							+ " expense_id SERIAL PRIMARY KEY, "
							+ " expense_record_id INT REFERENCES tb_records(record_id) ON DELETE CASCADE, "
							+ "expense_date DATE NOT NULL, "
							+ "expense_cat VARCHAR(50) NOT NULL, "
							+ "expense_amt INTEGER NOT NULL);";

			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sqlQuery2);
			pstmt.executeUpdate();
			
			conn.close();

		} catch (SQLException e) {
			System.err.println(e.getMessage() + " (" + e.getSQLState() + ") main Method");

		}
	}
	
	public static String addIncome(Record rec, int addAmt) {
	    try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
	        String sqlQuery = "UPDATE tb_records SET record_income = ? WHERE record_id = ?;";
	        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
	        pstmt.setInt(1, rec.getRecordIncome()+addAmt);
	        pstmt.setInt(2, rec.getRecordId());
	        pstmt.executeUpdate();
	        
	        conn.close();
	        
	        return "Income Updated Successfully";
	    } catch (SQLException e) {
	        return e.getMessage() + " (" + e.getSQLState() + ") addIncome Method";
	    }
	}

	public static String createNewRecord(Record newRecord) {

		try {
			Connection conn = DriverManager.getConnection(LINK_STRING);
			String insertQuery = "INSERT INTO tb_records (record_name, record_income, record_saving, record_ts) VALUES (?, ?, ?, ?);";

			PreparedStatement pStatement = conn.prepareStatement(insertQuery);
			pStatement.setString(1, newRecord.getRecordName());
			pStatement.setInt(2, newRecord.getRecordIncome());
			pStatement.setInt(3, newRecord.getRecordSaving());
			pStatement.setTimestamp(4, java.sql.Timestamp.from(java.time.Instant.now()));
			pStatement.executeUpdate();

			conn.close();

			return "Record Created Successfully";

		} catch (SQLException e) {
			if (e.getSQLState().equals("23505")) {
				return newRecord.getRecordName() + " Record already created!";
			} else {
				return e.getMessage() + " (" + e.getSQLState() + ") createNewRecord Method";
			}
		}
	}

	public static ArrayList<Record> getRecords() {
		ArrayList<Record> allRecords = new ArrayList<>();

		try {
			Connection conn = DriverManager.getConnection(LINK_STRING);
			String sqlQuery = "SELECT * FROM tb_records;";
			PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				allRecords.add(new Record(rs.getInt("record_id"), rs.getString("record_name"), rs.getTimestamp("record_ts") + "", rs.getInt("record_income"),  rs.getInt("record_saving")));
			}
			conn.close();
			return allRecords;

		} catch (SQLException e) {
			System.err.println(e.getMessage() + "getRecords");
			allRecords.clear();
			return allRecords;
		}

	}

	public static String createExpense(Record rec, Expense exp) {
		try {

			Connection conn = DriverManager.getConnection(LINK_STRING);
			String insertQuery = "INSERT INTO tb_expenses(expense_record_id, expense_date, expense_cat, expense_amt) "
								 + "VALUES (?,?,?,?);";
			PreparedStatement pstmt = conn.prepareStatement(insertQuery);
			pstmt.setInt(1, rec.getRecordId());
			pstmt.setObject(2, exp.getExpenseDate());
			pstmt.setString(3, exp.getExpenseCat());
			pstmt.setInt(4, exp.getExpenseAmt());
			pstmt.executeUpdate();
			conn.close();

			return "Expense Created Successfully";

		} catch (SQLException e) {
			return e.getMessage() + " (" + e.getSQLState() + ")";
		}
	}

	public static void adminDeleteRecords() {
		ArrayList<Record> allRecords = getRecords();

		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {

			String sqlQuery = "DROP TABLE tb_records;";
			PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
			preparedStatement.executeUpdate();

			conn.close();

		} catch (SQLException e) {
			System.err.println(e.getMessage() + " adminDeleteRecords " + e.getSQLState());
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
				allExpenses.add(new Expense(rs.getInt("expense_id"), rs.getInt("expense_record_id"), rs.getDate("expense_date").toLocalDate(), rs.getString("expense_cat"), rs.getInt("expense_amt")));
			}
			conn.close();
			return allExpenses;

		} catch (SQLException e) {
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
			return "Expense Deleted Successfully";
		} catch (SQLException e) {
			return e.getMessage() + " (" + e.getSQLState() + ") deleteExpense Method";
		}
	}

	public static String updateRecord(Record rec, String newName, int income, int saving) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "UPDATE tb_records SET record_name = ?, record_income = ?, record_saving = ? WHERE record_id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setString(1, newName);
			pstmt.setInt(2, income);
			pstmt.setInt(3, saving);
			pstmt.setInt(4, rec.getRecordId());
			pstmt.executeUpdate();
			
            conn.close();

			return "Record Updated Successfully";

		} catch (SQLException e) {
			return e.getMessage() + " (" + e.getSQLState() + ") updateRecord Method";
		}
	}

	public static String updateExpense(Expense expense) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "UPDATE tb_expenses SET expense_date = ?, expense_cat = ?, expense_amt = ? WHERE expense_id = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setObject(1, expense.getExpenseDate());
			pstmt.setString(2, expense.getExpenseCat());
			pstmt.setInt(3, expense.getExpenseAmt());
			pstmt.setInt(4, expense.getExpenseId());
			pstmt.executeUpdate();
			conn.close();

			return "Expense Updated Successfully";
		} catch (SQLException e) {
			return e.getMessage() + " (" + e.getSQLState() + ") updateExpense Method";
		}
	}

    public static String deleteRecord(Record rec) {
        try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
            String sqlQuery = "DELETE FROM tb_records WHERE record_id = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setInt(1, rec.getRecordId());
            pstmt.executeUpdate();


            conn.close();

            return "Record Deleted Successfully";
        } catch (SQLException e) {
            return e.getMessage() + " (" + e.getSQLState() + ") deleteRecord Method";

        }

    }



}
