package studysync.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        Properties props = new Properties();
        try (InputStream is = DBUtil.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (is == null) throw new RuntimeException("db.properties not found");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Derby JDBC driver not found", e);
        }

        URL  = props.getProperty("db.url");
        USER = props.getProperty("db.user");
        PASS = props.getProperty("db.password");
    }

    private DBUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}