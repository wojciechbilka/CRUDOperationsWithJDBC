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
            PreparedStatement statement = connection.prepareStatement("select * from Employee");
            ResultSet rs = statement.executeQuery();
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
            PreparedStatement statement = connection.prepareStatement("select max(ID) as 'maximum' from Employee");
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                i = rs.getInt("maximum");
            }
            return i;
        }
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
            System.out.println(employee.getStartJobDate().toString());
            PreparedStatement statement = connection.prepareStatement("insert into Employee values(?,?,?,?,?,?,?,?)");
            statement.setString(1, "'" + employee.getLastName() + "'");
            statement.setString(2, "'" + employee.getFirstName() + "'");
            statement.setString(3, "'" + employee.getAddress() + "'");
            statement.setString(4, "'" + employee.getCity() + "'");
            statement.setDouble(5, employee.getSalary());
            statement.setInt(6, employee.getAge());
            statement.setDate(7, Date.valueOf(employee.getStartJobDate()));
            statement.setString(8, "'" + employee.getBenefit() + "'");
            statement.execute();
        }
    }

    public void updateEmployee(Employee employee, int id) throws SQLException{
        try(Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement("update Employee set " +
                    "LastName =?, FirstName =?, Address =?, City =?, Salary =?, Age =?, StartJobDate =?, Benefit =? WHERE ID =?");
            statement.setString(1, "'" + employee.getLastName() + "'");
            statement.setString(2, "'" + employee.getFirstName() + "'");
            statement.setString(3, "'" + employee.getAddress() + "'");
            statement.setString(4, "'" + employee.getCity() + "'");
            statement.setDouble(5, employee.getSalary());
            statement.setInt(6, employee.getAge());
            statement.setDate(7, Date.valueOf(employee.getStartJobDate()));
            statement.setString(8, "'" + employee.getBenefit() + "'");
            statement.setInt(9, id);
            statement.execute();
        }
    }

    private void deleteRow(String tableName, String idName, int id) throws SQLException {
        try(Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement("delete from ? where ? = ?");
            statement.setString(1, tableName);
            statement.setString(2, idName);
            statement.setInt(3, id);
            statement.execute();
        }
    }

    public void deleteEmployee(int id) throws SQLException{
        try(Connection connection = DriverManager.getConnection(URL)) {

            PreparedStatement statementSelectCars = connection.prepareStatement("select CarID from EmployeeCarRelationFK where EmployeeID = " + id);
            statementSelectCars.executeQuery();
            deleteRow("EmployeeCarRelationFK", "EmployeeID", id);
            PreparedStatement statement = connection.prepareStatement("delete from Employee where ID = " + id + ";");
            statement.execute();
        }
    }

}
