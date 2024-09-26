package se.lu.ics.models;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private double budget;
    private List<Employee> employees = new ArrayList<>();

    public Department(String name, double budget) {
        this.name = name;
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}

