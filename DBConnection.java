import java.sql.*;

public class DBConnection {
    final static String dburl = "jdbc:mysql://localhost:3306/gamemanagementdb";
    final static String dbuser = "root";
    final static String dbpass = "";
    static Connection con;

    DBConnection() {
    }

    public static Connection getConnection() throws Exception {
        con = DriverManager.getConnection(dburl, dbuser, dbpass);
        return con;
    }
}
