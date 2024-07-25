package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String url="jdbc:sqlite:hamsterDb";

    public static Connection getConnection()throws SQLException{
        return DriverManager.getConnection(url);
    }
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Connection to SQLite has been established.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
}}
