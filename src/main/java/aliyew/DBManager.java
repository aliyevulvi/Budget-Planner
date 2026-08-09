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
        try {
            Connection conn = DriverManager.getConnection(LINK_STRING);
            String sqlQuery = "CREATE TABLE IF NOT EXISTS tb_records ( "
                    + "record_id SERIAL PRIMARY KEY, "
                    + "record_name VARCHAR(20) UNIQUE NOT NULL, "
                    + "record_income INTEGER NOT NULL, "
                    + "record_ts TIMESTAMP NOT NULL "
                    + ");";

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.execute();
            conn.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.toString());
            System.err.println(e.getCause());
        }
    }

    public static String createNewRecord(Record newRecord) {

        try {
            Connection conn = DriverManager.getConnection(LINK_STRING);
            String insertQuery = "INSERT INTO tb_records (record_name, record_income, record_ts) VALUES (?, ?, ?);";
            String expenseQuery = "CREATE TABLE record_"+newRecord.getRecordName()+"_expenses ( "
            + "expense_id SERIAL PRIMARY KEY, "
            + "expense_date DATE NOT NULL, "
            + "expense_cat VARCHAR(50) NOT NULL, "
            + "expense_amt INTEGER NOT NULL);";

            PreparedStatement pStatement = conn.prepareStatement(insertQuery);
            pStatement.setString(1, newRecord.getRecordName());
            pStatement.setInt(2, newRecord.getTotalIncome());
            pStatement.setTimestamp(3, java.sql.Timestamp.from(java.time.Instant.now()));
            pStatement.executeUpdate();
            pStatement = conn.prepareStatement(expenseQuery);
            pStatement.executeUpdate();
            conn.close();

            return "Record Created Successfully";

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                return newRecord.getRecordName() + " Record already created!";
            } else {
                return e.getMessage() + " (" + e.getSQLState() + ")";
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
                allRecords.add(new Record(rs.getInt("record_id"), rs.getString("record_name"), rs.getTimestamp("record_ts") + "", rs.getInt("record_income")));
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
            String insertQuery = "INSERT INTO record_"+rec.getRecordName()+"_expenses(expense_date, expense_cat, expense_amt) " + 
                                 "VALUES (?,?,?);";
             PreparedStatement pstmt = conn.prepareStatement(insertQuery);
             pstmt.setObject(1, exp.getExpenseDate());
             pstmt.setString(2, exp.getExpenseCat());
             pstmt.setInt(3, exp.getExpenseAmt());
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
            for (Record rec : allRecords) {
                PreparedStatement pstmt = conn.prepareStatement("DROP TABLE record_"+rec.getRecordName()+"_expenses;");
                pstmt.executeUpdate();
            }
           
            
            String sqlQuery = "DROP TABLE tb_records;";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.executeUpdate();
            
            conn.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage() + " adminDeleteRecords " + e.getSQLState());
        }
    }

}
