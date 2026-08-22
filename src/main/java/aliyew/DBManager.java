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
			System.err.println(e.getMessage() + " (" + e.getSQLState() + ") main Method");

		}
	}

	public static boolean connnectDB() {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			conn.close();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage() + " (" + e.getSQLState() + ") connectDB Method");
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
	        rec.setSync(true);
	        return "Income Updated Successfully";
	    } catch (SQLException e) {
			rec.setSync(false);
	        return e.getMessage() + " (" + e.getSQLState() + ") addIncome Method";
	    } finally {
			for (Record record : JsonManager.readValueRecords()) {
			if (record.getRecordId() == rec.getRecordId()) {
				record.setIncome(record.getRecordIncome() + addAmt);
				record.setSync(false);
				JsonManager.updateRecord(record);
			}
		}
		}
	}

	public static String createNewRecord(Record newRecord) {

		try {
			try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
				String insertQuery = "INSERT INTO tb_records (record_name, record_income, record_saving, record_ts) VALUES (?, ?, ?, ?);";

				PreparedStatement pStatement = conn.prepareStatement(insertQuery, java.sql.Statement.RETURN_GENERATED_KEYS);
				pStatement.setString(1, newRecord.getRecordName());
				pStatement.setDouble(2, newRecord.getRecordIncome());
				pStatement.setDouble(3, newRecord.getRecordSaving());
				pStatement.setTimestamp(4, java.sql.Timestamp.from(java.time.Instant.now()));
				pStatement.executeUpdate();

				int generatedId = -1;

				try (ResultSet generatedKeys = pStatement.getGeneratedKeys()) {
        			if (generatedKeys.next()) {
            			generatedId = generatedKeys.getInt(1);
						newRecord.setId(generatedId);
            
        			}
    			} catch (SQLException es) {
    				System.out.println("Hata: " + es.getMessage());
				}
			}
			newRecord.setSync(true);
			return "Record Created Successfully";

		} catch (SQLException e) {
			newRecord.setSync(false);
			if (e.getSQLState().equals("23505")) {
				return newRecord.getRecordName() + " Record already created!";
			} else {
				return e.getMessage() + " (" + e.getSQLState() + ") createNewRecord Method";
			}
		} finally {
			newRecord.setDate();
			JsonManager.writeValue(newRecord);
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
			System.err.println(e.getMessage() + "getRecords");
			allRecords.clear();
			return allRecords;
		}

	}

	public static String createExpense(Record rec, Expense exp) {
		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {

			String insertQuery = "INSERT INTO tb_expenses(expense_record_id, expense_date, expense_cat, expense_amt) "
								 + "VALUES (?,?,?,?);";
			PreparedStatement pstmt = conn.prepareStatement(insertQuery);
			pstmt.setInt(1, rec.getRecordId());
			pstmt.setObject(2, exp.getExpenseDate());
			pstmt.setString(3, exp.getExpenseCat());
			pstmt.setDouble(4, exp.getExpenseAmt());
			pstmt.executeUpdate();

			int generatedId = -1;
			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
        			if (generatedKeys.next()) {
            			generatedId = generatedKeys.getInt(1);
						exp.setId(generatedId);
            
        			}
    			} catch (SQLException es) {
    				System.out.println("Hata: " + es.getMessage());
				}

			conn.close();
			exp.setSync(true);
			return "Expense Created Successfully";

		} catch (SQLException e) {
			exp.setSync(false);
			exp.setId(-1);
			return e.getMessage() + " (" + e.getSQLState() + ")";
		} finally {
			if (exp.getExpenseId() != -1) {
				ArrayList<Expense> allExpenses = getExpenses();
				exp.setId(allExpenses.get(allExpenses.size()-1).getExpenseId());
			}
			JsonManager.writeValue(exp);
		}
	}

	public static void adminDeleteRecords() {

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
				allExpenses.add(new Expense(rs.getInt("expense_id"), rs.getInt("expense_record_id"), rs.getDate("expense_date").toLocalDate(), rs.getString("expense_cat"), rs.getDouble("expense_amt")));
			}
			conn.close();
			return allExpenses;

		} catch (SQLException e) {
			allExpenses.clear();
			return allExpenses;
		}

	}

	public static ArrayList<Expense> getExpenses() {
		ArrayList<Expense> allExpenses = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(LINK_STRING)) {
			String sqlQuery = "SELECT * FROM tb_expenses;";

			PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				allExpenses.add(new Expense(rs.getInt("expense_id"), rs.getInt("expense_record_id"),
						rs.getDate("expense_date").toLocalDate(), rs.getString("expense_cat"),
						rs.getDouble("expense_amt")));
			}
			conn.close();
			return allExpenses;

		} catch (SQLException e) {
			allExpenses.clear();
			return allExpenses;
		}

	}

	public static String deleteExpense(int expenseId) {
		JsonManager.deleteExpense(expenseId);

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
			rec.setSync(true);

			return "Record Updated Successfully";

		} catch (SQLException e) {
			rec.setSync(false);
			return e.getMessage() + " (" + e.getSQLState() + ") updateRecord Method";
		} finally {
			rec.setName(newName);
			rec.setIncome(income);
			rec.setSaving(saving);
			JsonManager.updateRecord(rec);
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

			expense.setSync(true);
			return "Expense Updated Successfully";
		} catch (SQLException e) {
			expense.setSync(false);
			return e.getMessage() + " (" + e.getSQLState() + ") updateExpense Method";
		} finally {
			JsonManager.updateExpense(expense);
		}
	}

    public static String deleteRecord(Record rec) {
		JsonManager.deleteRecord(rec.getRecordId());

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
