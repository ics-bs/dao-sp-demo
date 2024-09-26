package se.lu.ics;

import java.io.IOException;

import se.lu.ics.data.DaoException;
import se.lu.ics.data.EmployeeDao;
import se.lu.ics.models.Employee;

public class Main {
    public static void main(String[] args) {
        try {
            EmployeeDao employeeDao = new EmployeeDao();

            Employee foundEmployee = employeeDao.getByNo("E8");

            foundEmployee.setName("Guy");
            foundEmployee.setSalary(1000000.0);

            employeeDao.update(foundEmployee);

            Employee updatedEmployee = employeeDao.getByNo("E8");
            System.out.println("Employee number: " + updatedEmployee.getEmployeeNumber());
            System.out.println("Name: " + updatedEmployee.getName());
            System.out.println("Salary: " + updatedEmployee.getSalary());
            
        } catch (IOException e) {
            // TODO Error handling
            e.printStackTrace();
        } catch (DaoException e) {
            // TODO Error handling
            e.printStackTrace();
        }
    }
}