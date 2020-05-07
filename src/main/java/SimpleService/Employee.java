package SimpleService;

import java.time.LocalDate;

public class Employee {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private double salary;
    private int age;
    private LocalDate startJobDate;
    private String benefit;

    public Employee(String firstName, String lastName, String address, String city, double salary, int age, LocalDate startJobDate, String benefit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.salary = salary;
        this.age = age;
        this.startJobDate = startJobDate;
        this.benefit = benefit;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getStartJobDate() {
        return startJobDate;
    }

    public void setStartJobDate(LocalDate startJobDate) {
        this.startJobDate = startJobDate;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }
}
