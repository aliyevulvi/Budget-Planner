package aliyew;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBManager {

    public static void main(String[] args) {
        System.out.println("DBManager");

        try {
            Connection conn = DriverManager.getConnection("");
            System.out.println("Connected to Database");

            String sqlQuery = "CREATE TABLE IF NOT EXISTS tb_records ( "
                    + "record_id SERIAL PRIMARY KEY, "
                    + "record_name VARCHAR(20) UNIQUE NOT NULL, "
                    + "record_ts TIMESTAMP NOT NULL "
                    + ");";

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            System.out.println(pstmt.execute());
            System.out.println(createNewRecord());
            conn.close();
            System.out.println("Disconnected From Database");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.toString());
            System.err.println(e.getCause());
        }
    }

    public static String createNewRecord() {

        try {
            Connection conn = DriverManager.getConnection("");
            String insertQuery = "INSERT INTO tb_records (record_name, record_ts) VALUES (?, ?)";
            PreparedStatement pStatement = conn.prepareStatement(insertQuery);
            pStatement.setString(1, "TEST1");
            pStatement.setTimestamp(2, java.sql.Timestamp.from(java.time.Instant.now()));

            return pStatement.executeUpdate() + "";

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                return "TEST1 Record already created!";
            } else {
                return e.getMessage() + e.getSQLState();
            }
        }
    }
    
    public static ArrayList<Record> getRecords() {
        
        return new ArrayList<Record>();
    }

}
