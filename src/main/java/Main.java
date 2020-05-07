import SimpleService.Employee;

import java.sql.*;
import java.time.LocalDate;

public class Main {

    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=CONNECTIS;integratedSecurity=true;";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        initialize();
        getEmployeeTable();
        Employee employee1 = new Employee("John",
                "Vinna",
                "Jerozolimskie 223",
                "Warszawa",
                2300.0,
                22,
                LocalDate.of(2015, 12, 31),
                "Medium");
        insertEmployee(employee1);
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

    private static void insertEmployee(Employee employee) throws SQLException{
        try(Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement("insert into Employee values(" +
                    "'" + employee.getLastName() + "'," +
                    "'" + employee.getFirstName() + "'," +
                    "'" + employee.getAddress() + "'," +
                    "'" + employee.getCity() + "'," +
                    employee.getSalary() + "," +
                    employee.getAge() + "," +
                    "'" + employee.getStartJobDate().toString() + "'," +
                    "'" + employee.getBenefit() +"')");
            statement.execute();
        }
    }



}
