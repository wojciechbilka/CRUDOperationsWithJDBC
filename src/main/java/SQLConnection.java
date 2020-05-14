import java.sql.*;

public class SQLConnection {

    private Connection connection;

    public Connection connectionToSQLServer() {

        String databaseName = "ExampleJDBC";
        String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=" + databaseName + ";integratedSecurity=true;";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url);
            System.out.println(connection);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public Connection getConnection() {
        return (connection != null) ? connection : connectionToSQLServer();
    }
}
