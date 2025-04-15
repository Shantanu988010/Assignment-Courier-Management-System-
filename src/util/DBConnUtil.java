package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {

    private static final String fileName = "src/db.properties"; // Path to properties file

    // This method returns the database connection using the connection string obtained from DbPropertyUtil
    public static Connection getDbConnection() {
        Connection conn = null;
        String connString = null;

        try {
            // Get the connection string using the properties file
            connString = DbPropertyUtil.getConnectionString(fileName);
        } catch (IOException e) {
            System.out.println("Connection String Creation Failed");
            e.printStackTrace();
        }

        // If connection string is obtained, attempt to establish the connection
        if (connString != null) {
            try {
                conn = DriverManager.getConnection(connString); // Get connection using the connection string
            } catch (SQLException e) {
                System.out.println("Error While Establishing DBConnection........");
                e.printStackTrace();
            }
        }
        return conn; // Return the connection object
    }
}
