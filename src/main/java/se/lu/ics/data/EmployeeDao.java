package se.lu.ics.data;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.lu.ics.models.Department;
import se.lu.ics.models.Employee;

public class EmployeeDao {

    private ConnectionHandler connectionHandler;

    public EmployeeDao() throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }

    /**
     * Retrieves all employees from the database.
     * This method executes the stored procedure uspGetAllEmployees.
     *
     * @return A list of Employee objects.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<Employee> getAll() {
        String callProcedure = "{CALL uspGetAllEmployees}";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                CallableStatement statement = connection.prepareCall(callProcedure);
                ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and map each row to an Employee object
            while (resultSet.next()) {
                employees.add(mapToEmployee(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching all employees.", e);
        }

        return employees;
    }

    /**
     * Retrieves an employee by EmpNo from the database.
     * This method executes the stored procedure uspGetEmployeeByEmpNo.
     *
     * @param empNo The employee number.
     * @return An Employee object.
     * @throws DaoException If there is an error accessing the database.
     */
    public Employee getByNo(String empNo) {
        String callProcedure = "{CALL uspGetEmployeeByEmpNo(?)}";

        try (Connection connection = connectionHandler.getConnection();
                CallableStatement statement = connection.prepareCall(callProcedure)) {

            statement.setString(1, empNo);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToEmployee(resultSet);
                } else {
                    return null; // Employee not found
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching employee with EmpNo: " + empNo, e);
        }
    }

    /**
     * Saves a new employee to the database.
     * This method executes the stored procedure uspInsertEmployee.
     *
     * @param employee The Employee object containing the data to be saved.
     * @throws DaoException If there is an error saving the employee (e.g., if the Employee No already exists).
     */
    public void save(Employee employee) {
        String callProcedure = "{CALL uspInsertEmployee(?, ?, ?)}";

        try (Connection connection = connectionHandler.getConnection();
                CallableStatement statement = connection.prepareCall(callProcedure)) {

            // Set employee data into the prepared statement
            statement.setString(1, employee.getEmployeeNumber());
            statement.setString(2, employee.getName());
            statement.setDouble(3, employee.getSalary());

            // Execute the insert operation
            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) { // Unique constraint violation
                throw new DaoException("An employee with this Employee No already exists.", e);
            } else {
                throw new DaoException("Error saving employee: " + employee.getEmployeeNumber(), e);
            }
        }
    }

    /**
     * Updates an existing employee's details in the database.
     * This method executes the stored procedure uspUpdateEmployee.
     *
     * @param employee The Employee object containing the updated data.
     * @throws DaoException If there is an error updating the employee's data.
     */
    public void update(Employee employee) {
        String callProcedure = "{CALL uspUpdateEmployee(?, ?, ?)}";

        try (Connection connection = connectionHandler.getConnection();
                CallableStatement statement = connection.prepareCall(callProcedure)) {

            // Set updated employee data into the prepared statement
            statement.setString(1, employee.getEmployeeNumber());
            statement.setString(2, employee.getName());
            statement.setDouble(3, employee.getSalary());

            // Execute the update operation
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error updating employee: " + employee.getEmployeeNumber(), e);
        }
    }

    /**
     * Deletes an employee from the database by Employee No.
     * This method executes the stored procedure uspDeleteEmployee.
     *
     * @param empNo The Employee No of the employee to be deleted.
     * @throws DaoException If there is an error deleting the employee.
     */
    public void deleteByNo(String empNo) {
        String callProcedure = "{CALL uspDeleteEmployee(?)}";

        try (Connection connection = connectionHandler.getConnection();
                CallableStatement statement = connection.prepareCall(callProcedure)) {

            // Set Employee No in the prepared statement
            statement.setString(1, empNo);

            // Execute the delete operation
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error deleting employee with Employee No: " + empNo, e);
        }
    }

    /**
     * Retrieves all employees and their respective departments.
     * This method executes the stored procedure uspGetAllEmployeesWithDepartments.
     *
     * @return A list of Employee objects, each containing their departments.
     * @throws DaoException If there is an error accessing the database.
     */
    public List<Employee> getAllEmployeesWithDepartments() {
        String callProcedure = "{CALL uspGetAllEmployeesWithDepartments}";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionHandler.getConnection();
                CallableStatement statement = connection.prepareCall(callProcedure);
                ResultSet resultSet = statement.executeQuery()) {

            // Map to hold Employee by EmpNo
            Map<String, Employee> employeeMap = new HashMap<>();

            while (resultSet.next()) {
                String empNo = resultSet.getString("EmpNo");
                String empName = resultSet.getString("EmpName");
                double empSalary = resultSet.getDouble("EmpSalary");
                String deptName = resultSet.getString("DeptName");
                double deptBudget = resultSet.getDouble("DeptBudget");
                // Get or create Employee object
                Employee employee = employeeMap.get(empNo);
                if (employee == null) {
                    employee = new Employee(empNo, empName, empSalary);
                    employeeMap.put(empNo, employee);
                }

                // Create Department object with budget and add to the employee's department list
                Department department = new Department(deptName, deptBudget);
                employee.getDepartments().add(department);
            }

            // Add all employees to the list
            employees.addAll(employeeMap.values());

        } catch (SQLException e) {
            throw new DaoException("Error fetching employees and their departments.", e);
        }

        return employees;
    }

    /**
     * Maps a row in the ResultSet to an Employee object.
     * This method is a helper function used to convert the result of a SQL query into an Employee object.
     *
     * @param resultSet The ResultSet containing the employee data.
     * @return An Employee object with the data from the ResultSet.
     * @throws SQLException If there is an error accessing the data in the ResultSet.
     */
    private Employee mapToEmployee(ResultSet resultSet) throws SQLException {
        return new Employee(
                resultSet.getString("EmpNo"),
                resultSet.getString("EmpName"),
                resultSet.getDouble("EmpSalary"));
    }
}