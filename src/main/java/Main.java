import java.sql.*;

public class Main {

    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=CONNECTIS;integratedSecurity=true;";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        initialize();
        getEmployeeTable();


    }

    private static void initialize() throws ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

    private static void getEmployeeTable() throws SQLException{
        try(Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement selectStatement = connection.prepareStatement("select * from Employee");
            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) { // will traverse through all rows
                System.out.println("ID:" + rs.getInt("id") +
                        " Name:" + rs.getString("FirstName") +
                        " Surname:" + rs.getString("LastName"));
            }
        }
    }



}
