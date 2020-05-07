import SimpleService.Employee;
import SimpleService.EmployeeDAO;

import java.sql.*;
import java.time.LocalDate;

public class Main {
    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=CONNECTIS;integratedSecurity=true;";
    private static final String driverPath = "com.microsoft.sqlserver.jdbc.SQLServerDriver";


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        EmployeeDAO employeeDAO = new EmployeeDAO(URL, driverPath);

        System.out.println("\n=======================Start Table=========================\n");
        employeeDAO.getEmployeeTable();
        Employee employee1 = new Employee("John",
                "Vinna",
                "Jerozolimskie 223",
                "Warszawa",
                2300.0,
                22,
                LocalDate.of(2015, 12, 31),
                "Medium");

        employeeDAO.insertEmployee(employee1);
        System.out.println("\n=======================After Inserting=========================\n");
        employeeDAO.getEmployeeTable();

        employeeDAO.updateEmployee(employee1, 5);
        System.out.println("\n=======================After Updating=========================\n");
        employeeDAO.getEmployeeTable();

        employeeDAO.deleteEmployee(employeeDAO.getMaxId());
        System.out.println("\n=======================After Deleting=========================\n");
        employeeDAO.getEmployeeTable();
    }
}
