package se.lu.ics.models;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String employeeNumber;
    private String name;
    private double salary;
    private List<Department> departments = new ArrayList<>();

    public Employee(String employeeNumber, String name, double salary) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.salary = salary;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<Department> getDepartments() {
        return departments;
    }
}