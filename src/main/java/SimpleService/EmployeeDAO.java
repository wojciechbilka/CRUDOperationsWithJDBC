package SimpleService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private String URL;

    public EmployeeDAO(String URL, String driverClass) {
        this.URL = URL;
        try{
            Class.forName(driverClass);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getEmployeeTable() throws SQLException {
        try(Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement selectStatement = connection.prepareStatement("select * from Employee");
            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                System.out.println("ID:" + rs.getInt("id") +
                        " Name:" + rs.getString("FirstName") +
                        " Surname:" + rs.getString("LastName"));
            }
        }
    }

    public int getMaxId() throws SQLException{
        int i = 0;
        try(Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement selectStatement = connection.prepareStatement("select * from Employee");
            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                int temp;
                if((temp = rs.getInt("id")) > i) {
                    i = temp;
                }
            }
        }
        return i;
    }

    public List<Employee> getEmployeeList() throws SQLException{
        List<Employee> employees = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement selectStatement = connection.prepareStatement("select * from Employee");
            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                Employee tempEmployee = new Employee(
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getDouble("Salary"),
                        rs.getInt("Age"),
                        rs.getDate("StartJobDate").toLocalDate(),
                        rs.getString("Benefit"));
                employees.add(tempEmployee);
            }
        }
        return employees;
    }

    public void insertEmployee(Employee employee) throws SQLException{
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

    public void updateEmployee(Employee employee, int id) throws SQLException{
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

    public void deleteEmployee(int id) throws SQLException{
        try(Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement("delete from Employee where ID = " + id + ";");
            statement.execute();
        }
    }

}
