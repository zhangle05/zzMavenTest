/**
 * 
 */
package zz.maven.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author zhangle
 *
 */
public class JDBCConnectionTest {
    public static void main(String[] argv) {

        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        String DB_CORE_DRIVERCLASSNAME = System.getenv("DB_CORE_DRIVERCLASSNAME");
        String DB_CORE_HOST = System.getenv("DB_CORE_HOST");
        String DB_CORE_NAME = System.getenv("DB_CORE_NAME");
        String DB_CORE_URL = System.getenv("DB_CORE_URL");
        String DB_CORE_USERNAME = System.getenv("DB_CORE_USERNAME");
        String DB_CORE_PASSWORD = System.getenv("DB_CORE_PASSWORD");

        System.out.println("DB_CORE_DRIVERCLASSNAME is:" + DB_CORE_DRIVERCLASSNAME);
        System.out.println("DB_CORE_HOST is:" + DB_CORE_HOST);
        System.out.println("DB_CORE_NAME is:" + DB_CORE_NAME);
        System.out.println("DB_CORE_URL is:" + DB_CORE_URL);
        System.out.println("DB_CORE_USERNAME is:" + DB_CORE_USERNAME);
        System.out.println("DB_CORE_PASSWORD is:" + DB_CORE_PASSWORD);
        try {
            Class.forName(DB_CORE_DRIVERCLASSNAME);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_CORE_URL, DB_CORE_USERNAME, DB_CORE_PASSWORD);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SHOW DATABASES;");
     
                if (stmt.execute("SHOW DATABASES;")) {
                    rs = stmt.getResultSet();
                }
     
                while (rs.next()) {
                    System.out.println("Database:" + rs.getString("Database"));
                }
            }
            catch (SQLException ex){
                // handle any errors
                ex.printStackTrace();
            }
        } else {
            System.out.println("Failed to make connection!");
        }
    }
}
