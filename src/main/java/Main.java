import SimpleService.Employee;

import java.sql.*;
import java.time.LocalDate;

public class Main {

    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=CONNECTIS;integratedSecurity=true;";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        initialize();
        System.out.println("\n=======================Start Table=========================\n");
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
        System.out.println("\n=======================After Inserting=========================\n");
        getEmployeeTable();

        updateEmployee(employee1, 6);
        System.out.println("\n=======================After Updating=========================\n");
        getEmployeeTable();

        deleteEmployee(13);
        System.out.println("\n=======================After Deleting=========================\n");
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

    private static void updateEmployee(Employee employee, int id) throws SQLException{
        try(Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement("update Employee set " +
                    "LastName =" + "'" + employee.getLastName() + "'," +
                    "FirstName =" + "'" + employee.getFirstName() + "'," +
                    "Address =" + "'" + employee.getAddress() + "'," +
                    "City =" + "'" + employee.getCity() + "'," +
                    "Salary =" + employee.getSalary() + "," +
                    "Age =" + employee.getAge() + "," +
                    "StartJobDate =" + "'" + employee.getStartJobDate().toString() + "'," +
                    "Benefit =" + "'" + employee.getBenefit() +"'" +
                    "WHERE ID =" + id + ";");
            statement.execute();
        }
    }

    private static void deleteEmployee(int id) throws SQLException{
        try(Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement("delete from Employee where ID = " + id + ";");
            statement.execute();
        }
    }

}
