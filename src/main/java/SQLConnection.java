import java.sql.*;

public class SQLConnection {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=CONNECTIS;integratedSecurity=true;";
        try(Connection connection = DriverManager.getConnection(url)) {
            //use connection
            System.out.println("Connection is valid (500ms): " + connection.isValid(500));
        }
    }
}
