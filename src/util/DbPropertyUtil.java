package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DbPropertyUtil {

    // This method takes the file name containing DB connection properties and returns the connection string
    public static String getConnectionString(String fileName) throws IOException {
        String connStr = null;
        Properties props = new Properties();

        // Load properties file
        FileInputStream fis = new FileInputStream(fileName);
        props.load(fis);

        // Extract individual properties from the file
        String user = props.getProperty("user");
        String password = props.getProperty("password");
        String protocol = props.getProperty("protocol");
        String system = props.getProperty("system");
        String database = props.getProperty("database");
        String port = props.getProperty("port");

        // Format the connection string
        connStr = protocol + "://" + system + ":" + port + "/" + database + "?user=" + user + "&password=" + password;

        return connStr; // Return the formatted connection string
    }
}
