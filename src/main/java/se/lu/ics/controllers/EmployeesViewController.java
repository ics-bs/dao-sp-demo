package se.lu.ics.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.EmployeeDao;
import se.lu.ics.models.Employee;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for managing Employee-related operations in the view.
 * This class interacts with the EmployeeDao to perform CRUD operations and 
 * updates the UI (TableView) accordingly. It handles user actions such as 
 * adding employees and loading employee data into the TableView.
 */
public class EmployeesViewController {

    @FXML
    private TableView<Employee> tableViewEmployee;

    @FXML
    private TableColumn<Employee, String> tableColumnEmployeeNumber; 

    @FXML
    private TableColumn<Employee, String> tableColumnEmployeeName;

    @FXML
    private TableColumn<Employee, Double> tableColumnEmployeeSalary;

    @FXML
    private TextField textFieldEmployeeNumber; 

    @FXML
    private TextField textFieldEmployeeName;

    @FXML
    private TextField textFieldEmployeeSalary;

    @FXML
    private Button btnEmployeeAdd;

    @FXML
    private Label labelErrorMessage;

    private EmployeeDao employeeDao;

    /**
     * Constructor for EmployeeController.
     * It initializes the EmployeeDao to manage database interactions. 
     * If there is an error initializing the database, it will display an error message.
     */
    public EmployeesViewController() {
        try {
            employeeDao = new EmployeeDao();
        } catch (IOException e) {
            displayErrorMessage("Error initializing database connection: " + e.getMessage());
        }
    }

    /**
     * Initializes the TableView by setting up the columns and loading the initial 
     * list of employees from the database.
     */
    @FXML
    public void initialize() {
        // Set up table columns for displaying employee data
        tableColumnEmployeeNumber.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        tableColumnEmployeeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmployeeSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        // Load employee data from the database
        loadEmployees();
    }

    /**
     * Handles the event when the "Add" button is clicked. 
     * It reads the input from the text fields, creates a new Employee object,
     * and saves it to the database using EmployeeDao. If successful, it refreshes
     * the TableView to display the newly added employee.
     *
     * @param event MouseEvent triggered when the "Add" button is clicked.
     */
    @FXML
    private void buttonEmployeeAdd_OnClick(MouseEvent event) {
        clearErrorMessage();

        try {
            // Retrieve input from text fields
            String empNo = textFieldEmployeeNumber.getText();
            String empName = textFieldEmployeeName.getText();
            double empSalary = Double.parseDouble(textFieldEmployeeSalary.getText());

            // Create a new Employee object
            Employee newEmployee = new Employee(empNo, empName, empSalary);

            // Save the new employee to the database
            employeeDao.save(newEmployee);

            // Refresh the TableView to display the new employee
            loadEmployees();

            // Clear input fields after successful addition
            textFieldEmployeeNumber.clear(); 
            textFieldEmployeeName.clear();
            textFieldEmployeeSalary.clear();
        } catch (DaoException e) {
            displayErrorMessage(e.getMessage());
        } catch (NumberFormatException e) {
            displayErrorMessage("Invalid salary. Please enter a valid number.");
        }
    }

    /**
     * Loads the list of employees from the database and populates the TableView.
     * It retrieves all employees using the EmployeeDao and displays them in the table.
     */
    private void loadEmployees() {
        clearErrorMessage();
        try {
            // Fetch all employees from the database
            List<Employee> employeeList = employeeDao.getAll();
            // Convert the list to an ObservableList for TableView
            ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList(employeeList);
            // Set the items in the TableView
            tableViewEmployee.setItems(employeeObservableList);
        } catch (DaoException e) {
            displayErrorMessage("Error loading employees: " + e.getMessage());
        }
    }

    /**
     * Displays an error message in the label and changes its text color to red.
     *
     * @param message The error message to display.
     */
    private void displayErrorMessage(String message) {
        labelErrorMessage.setText(message);
        labelErrorMessage.setStyle("-fx-text-fill: red;");
    }

    /**
     * Clears any displayed error messages in the label.
     */
    private void clearErrorMessage() {
        labelErrorMessage.setText("");
    }
}