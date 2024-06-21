/********************************************************************************************
 *   COPYRIGHT (C) 2024 CREVAVI TECHNOLOGIES PVT LTD
 *   The reproduction, transmission or use of this document/file or its
 *   contents is not permitted without written authorization.
 *   Offenders will be liable for damages. All rights reserved.
 *---------------------------------------------------------------------------
 *   Purpose:  CRUDController.java file
 *   Project:  Student Management System
 *   Platform: Cross-platform (Windows, macOS, Linux)
 *   Compiler: JDK-22
 *   IDE:      Eclipse IDE for Enterprise Java and Web Developers (includes Incubating components)
 *	       Version: 2024-03 (4.31.0)
 *             Build id: 20240307-1437
 ********************************************************************************************/

package application;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

/**
 * Controller class for CRUD operations on Student Data. This class provides
 * functionalities to add, update, delete, and display student records. It also
 * allows exporting and importing student data in CSV and Excel formats.
 */

public class CRUDController implements Initializable {

	// FXML injected fields for CRUD operations
	@FXML
	private TextField crud_studentNumber;

	@FXML
	private TextField crud_fullName;

	@FXML
	private ComboBox<String> crud_year;

	@FXML
	private ComboBox<String> crud_course;

	@FXML
	private ComboBox<String> crud_gender;

	@FXML
	private Button crud_addBtn;

	@FXML
	private Button crud_updateBtn;

	@FXML
	private Button crud_clearBtn;

	@FXML
	private Button crud_deleteBtn;

	@FXML
	private TableView<StudentData> crud_tableView;

	@FXML
	private TableColumn<StudentData, String> crud_col_studentNumber;

	@FXML
	private TableColumn<StudentData, String> crud_col_fullName;

	@FXML
	private TableColumn<StudentData, String> crud_col_year;

	@FXML
	private TableColumn<StudentData, String> crud_col_course;

	@FXML
	private TableColumn<StudentData, String> crud_col_gender;

	@FXML
	private Button file_expcsvBtn;

	@FXML
	private Button file_expexcBtn;

	@FXML
	private Button file_impcsvBtn;

	@FXML
	private Button file_impexcBtn;

	// Operations instances for CSV and Excel operations
	private CSVOperations csvOperations;
	private ExcelOperations excelOperations;
	
	// Database instance
    private Database database;

	// Database connection variables
	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	private Alert alert;

	// Observable list to hold student data
	private ObservableList<StudentData> studentList;

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 * 
	 * @param location  the location used to resolve relative paths for the root
	 *                  object, or null if the location is not known
	 * 
	 * @param resources the resources used to localize the root object, or null if
	 *                  the root object was not localized
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initOperations();
		studentYearList();
		studentCourseList();
		studentGenderList();
		studentShowData();
		crud_tableView.setItems(studentList);
	}

	/**
	 * Initializes CSV and Excel operations and the observable list for student
	 * data.
	 */
	public void initOperations() {
		csvOperations = new CSVOperations(this);
		excelOperations = new ExcelOperations(this);
		studentList = FXCollections.observableArrayList();
		this.database = new Database();
	}

	/**
	 * Handles the event for exporting data to CSV format.
	 * 
	 * @param event the action event triggering the export
	 */
	@FXML
	private void handleExportToCSV(ActionEvent event) {
		csvOperations.exportToCSV(event);
	}

	/**
	 * Handles the event for importing data from a CSV file.
	 * 
	 * @param event the action event triggering the import
	 */
	@FXML
	private void handleImportFromCSV(ActionEvent event) {
		List<StudentData> data = csvOperations.importFromCSV(event);
		importDataIntoPostgres(data);
		studentShowData();
	}

	/**
	 * Handles the event for exporting data to Excel format.
	 * 
	 * @param event the action event triggering the export
	 */
	@FXML
	private void handleExportToExcel(ActionEvent event) {
		excelOperations.exportToExcel(event);
	}

	/**
	 * Handles the event for importing data from an Excel file.
	 * 
	 * @param event the action event triggering the import
	 */
	@FXML
	private void handleImportFromExcel(ActionEvent event) {
		List<StudentData> data = excelOperations.importFromExcel(event);
		if (data != null) {
			importDataIntoPostgres(data);
			studentShowData();
		} else {
			// Handle the case where data is null
			showAlert(AlertType.ERROR, "Error", "Failed to import data from Excel file.");
		}
	}

	/**
	 * Handles the event for adding a new student to the database.
	 * 
	 * @param event the action event triggering the addition of a new student
	 */
	@FXML
	public void studentAddBtn(ActionEvent event) {
	    if (crud_studentNumber.getText().isEmpty() || crud_fullName.getText().isEmpty()
	            || crud_year.getSelectionModel().getSelectedItem() == null
	            || crud_course.getSelectionModel().getSelectedItem() == null
	            || crud_gender.getSelectionModel().getSelectedItem() == null) {
	        showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
	    } else {
	        StudentData student = new StudentData(crud_studentNumber.getText(), 
	        		                              crud_fullName.getText(),
	                                              crud_year.getSelectionModel().getSelectedItem(), 
	                                              crud_course.getSelectionModel().getSelectedItem(),
	                                              crud_gender.getSelectionModel().getSelectedItem());
	       
	        try {
	            if (database.isStudentNumberTaken(student.getStudentNumber())) {
	                showAlert(AlertType.ERROR, "Error Message",
	                        "Student Number: " + crud_studentNumber.getText() + " is already taken");
	            } else {
	                database.addStudent(student);
	                showAlert(AlertType.INFORMATION, "INFORMATION Message", "Successfully Added!");
	                studentListData();
	                studentClearBtn(event);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            showAlert(AlertType.ERROR, "Error Message", "Error adding student: " + e.getMessage());
	        }
	    }
	}
	/**
	 * Handles the event for updating student information in the database.
	 * 
	 * @param event the action event triggering the update of student information
	 */
	@FXML
	public void studentUpdateBtn(ActionEvent event) {
	    if (crud_studentNumber.getText().isEmpty()) {
	        showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
	    } else {
	        // Confirm update operation with user
	        alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation Message");
	        alert.setHeaderText(null);
	        alert.setContentText(
	                "Are you sure you want to UPDATE Student Number: " + crud_studentNumber.getText() + "?");
	        Optional<ButtonType> option = alert.showAndWait();

	        if (option.isPresent() && option.get() == ButtonType.OK) {
	            // Execute update operation if user confirms
	            StudentData student = new StudentData(crud_studentNumber.getText(), crud_fullName.getText(),
	                    crud_year.getSelectionModel().getSelectedItem(), crud_course.getSelectionModel().getSelectedItem(),
	                    crud_gender.getSelectionModel().getSelectedItem());
	            database.updateStudent(student);
	            showAlert(AlertType.INFORMATION, "Information Message", "Successfully Updated!");

	            // Refresh the student list and clear input fields
	            studentListData();
	            studentClearBtn(event);
	        } else {
	            // Display cancellation message if user cancels the operation
	            showAlert(AlertType.INFORMATION, "Information Message", "Cancelled!");
	        }
	    }
	}

	
	/**
	 * Handles the event for deleting a selected student from the database.
	 * 
	 * @param event the action event triggering the deletion of student information
	 */
	@FXML
	public void studentDeleteBtn(ActionEvent event) {
	    StudentData selectedStudent = getCrud_tableView().getSelectionModel().getSelectedItem();

	    if (selectedStudent == null) {
	        showAlert(AlertType.ERROR, "Error Message", "Please select a student to delete.");
	    } else {
	        // Confirm delete operation with user
	        alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation");
	        alert.setHeaderText(null);
	        alert.setContentText("Are you sure you want to delete this student?");

	        // Show confirmation dialog and wait for user response
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.isPresent() && result.get() == ButtonType.OK) {
	            // Execute delete operation if user confirms
	            database.deleteStudent(selectedStudent.getStudentNumber());
	            showAlert(AlertType.INFORMATION, "Information Message", "Student deleted successfully.");

	            // Refresh the student list
	            studentListData();
	        }
	    }
	}

	

	/**
	 * Handles the event for clearing the input fields in the student form.
	 * 
	 * @param event the action event triggering the clearing of input fields
	 */
	@FXML
	public void studentClearBtn(ActionEvent event) {
		// Clear the text field for student number
		crud_studentNumber.setText("");

		// Clear the text field for full name
		crud_fullName.setText("");

		// Clear the selected value in the year combo box
		crud_year.getSelectionModel().clearSelection();

		// Clear the selected value in the course combo box
		crud_course.getSelectionModel().clearSelection();

		// Clear the selected value in the gender combo box
		crud_gender.getSelectionModel().clearSelection();
	}

	/**
	 * Array containing the list of years for students.
	 */
	private String[] yearList = { "1st Year", "2nd Year", "3rd Year", "4th Year" };

	/**
	 * Loads the list of available years into the year combo box. This method
	 * initializes the year combo box with predefined year options.
	 */
	public void studentYearList() {
		// Create a list to hold the year options
		List<String> yList = new ArrayList<>();

		// Add each year from the yearList array to the list
		for (String data : yearList) {
			yList.add(data);
		}

		// Convert the list to an observable list for JavaFX
		ObservableList<String> listData = FXCollections.observableArrayList(yList);

		// Set the observable list as the items of the year combo box
		crud_year.setItems(listData);
	}

	/**
	 * Array containing the list of courses available for students.
	 */
	private String[] courseList = { "BSIT", "BSCS", "BSCE" };

	/**
	 * Loads the list of available courses into the course combo box. This method
	 * initializes the course combo box with predefined course options.
	 */
	public void studentCourseList() {
		// Create a list to hold the course options
		List<String> cList = new ArrayList<>();

		// Add each course from the courseList array to the list
		for (String data : courseList) {
			cList.add(data);
		}

		// Convert the list to an observable list for JavaFX
		ObservableList<String> listData = FXCollections.observableArrayList(cList);

		// Set the observable list as the items of the course combo box
		crud_course.setItems(listData);
	}

	/**
	 * Array containing the list of genders available for students.
	 */
	private String[] genderList = { "Male", "Female", "Others" };

	/**
	 * Loads the list of available genders into the gender combo box. This method
	 * initializes the gender combo box with predefined gender options.
	 */
	public void studentGenderList() {
		// Create a list to hold the gender options
		List<String> gList = new ArrayList<>();

		// Add each gender from the genderList array to the list
		for (String data : genderList) {
			gList.add(data);
		}

		// Convert the list to an observable list for JavaFX
		ObservableList<String> listData = FXCollections.observableArrayList(gList);

		// Set the observable list as the items of the gender combo box
		crud_gender.setItems(listData);
	}

	/**
	 * Retrieves the list of students from the database.
	 *
	 * @return an ObservableList containing student data from the student_info
	 *         table.
	 */
	public ObservableList<StudentData> studentListData() {
		// Initialize an observable list to store student data
		ObservableList<StudentData> listData = FXCollections.observableArrayList();

		// SQL query to select all records from the student_info table
		String sql = "SELECT * FROM student_info";

		// Establish a connection to the database
		connect = Database.connect();

		try {
			// Prepare the SQL statement
			prepare = connect.prepareStatement(sql);

			// Execute the query and get the result set
			result = prepare.executeQuery();

			// Iterate through the result set
			while (result.next()) {
				// Create a new StudentData object for each record
				StudentData student = new StudentData(result.getString("student_number"), result.getString("full_name"),
						result.getString("year"), result.getString("course"), result.getString("gender"));

				// Add the student object to the observable list
				listData.add(student);
			}
		} catch (Exception e) {
			// Print the stack trace if an exception occurs
			e.printStackTrace();
		}

		// Return the list of student data
		return listData;
	}

	/**
	 * Fetches and displays student data from the database into the TableView.
	 */
	public void studentShowData() {
		// Clear the existing data from the student list
		studentList.clear();

		// SQL query to select specific columns from the student_info table
		String sql = "SELECT student_number, full_name, year, course, gender FROM student_info";

		try {
			// Establish a connection to the database
			connect = Database.connect();

			// Prepare the SQL statement
			prepare = connect.prepareStatement(sql);

			// Execute the query and get the result set
			result = prepare.executeQuery();

			// Iterate through the result set
			while (result.next()) {
				// Add each student record to the student list
				studentList.add(new StudentData(result.getString("student_number"), result.getString("full_name"),
						result.getString("year"), result.getString("course"), result.getString("gender")));
			}

			// Set up the table columns with appropriate data
			crud_col_studentNumber.setCellValueFactory(new PropertyValueFactory<StudentData, String>("studentNumber"));
			crud_col_fullName.setCellValueFactory(new PropertyValueFactory<StudentData, String>("fullName"));
			crud_col_year.setCellValueFactory(new PropertyValueFactory<StudentData, String>("year"));
			crud_col_course.setCellValueFactory(new PropertyValueFactory<StudentData, String>("course"));
			crud_col_gender.setCellValueFactory(new PropertyValueFactory<StudentData, String>("gender"));

			// Populate the TableView with the student list
			crud_tableView.setItems(studentList);
		} catch (Exception e) {
			// Print the stack trace if an exception occurs
			e.printStackTrace();
		} finally {
			// Close the resources in the finally block to ensure they are closed even if an
			// exception occurs
			try {
				if (result != null)
					result.close();
				if (prepare != null)
					prepare.close();
				if (connect != null)
					connect.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Displays an alert dialog with the specified type, title, and message.
	 * 
	 * @param alertType The type of alert (e.g., INFORMATION, WARNING, ERROR).
	 * @param title     The title of the alert dialog.
	 * @param message   The content message of the alert dialog.
	 */
	private void showAlert(AlertType alertType, String title, String message) {
		// Create a new alert of the specified type
		Alert alert = new Alert(alertType);

		// Set the title of the alert dialog
		alert.setTitle(title);

		// Remove the header text from the alert dialog
		alert.setHeaderText(null);

		// Set the content message of the alert dialog
		alert.setContentText(message);

		// Display the alert dialog and wait for the user to close it
		alert.showAndWait();
	}

	/**
	 * Handles the table edit event for the StudentData table. Updates the database
	 * and the corresponding StudentData object with the new value. Displays an
	 * alert message based on the success or failure of the update operation.
	 * 
	 * @param event The cell edit event containing the new value and the affected
	 *              StudentData object.
	 */
	@FXML
	public void handleTableEdit(TableColumn.CellEditEvent<StudentData, String> event) {
		// Get the student object associated with the edited row
		StudentData student = event.getRowValue();

		// Get the new value entered by the user
		String newValue = event.getNewValue();

		// Check if the student object and new value are not null and the new value is
		// not empty
		if (student != null && newValue != null && !newValue.trim().isEmpty()) {
			try {
				// Get the column name and format it for the SQL query
				String column = event.getTableColumn().getText().toLowerCase().replace(" ", "_");

				// Create the SQL update query
				String updateSQL = "UPDATE student_info SET " + column + " = ? WHERE student_number = ?";

				// Prepare the SQL statement
				prepare = connect.prepareStatement(updateSQL);

				// Set the new value and the student number in the SQL query
				prepare.setString(1, newValue);
				prepare.setString(2, student.getStudentNumber());

				// Execute the SQL update
				prepare.executeUpdate();

				// Update the corresponding field in the StudentData object
				switch (column) {
				case "student_number":
					student.setStudentNumber(newValue);
					break;
				case "full_name":
					student.setFullName(newValue);
					break;
				case "year":
					student.setYear(newValue);
					break;
				case "course":
					student.setCourse(newValue);
					break;
				case "gender":
					student.setGender(newValue);
					break;
				}

				// Refresh the table view to reflect the changes
				crud_tableView.refresh();

				// Display an information alert for successful update
				showAlert(AlertType.INFORMATION, "Information Message", "Student data updated successfully.");
			} catch (Exception e) {
				e.printStackTrace();

				// Display an error alert if the update fails
				showAlert(AlertType.ERROR, "Error Message", "Failed to update student data.");
			}
		}
	}

	/**
	 * Retrieves the TableView component used for displaying StudentData.
	 * 
	 * @return The TableView object for StudentData.
	 */
	public TableView<StudentData> getCrud_tableView() {
		return crud_tableView;
	}

	/**
	 * Imports student data into a PostgreSQL database.
	 * 
	 * @param data The list of StudentData objects to be imported.
	 */
	private void importDataIntoPostgres(List<StudentData> data) {
		// Database connection details
		String url = "jdbc:postgresql://localhost:5432/javafxCRUD";
		String username = "postgres";
		String password = "Crevavi";

		try (
				// Establishing a connection to the PostgreSQL database
				Connection conn = DriverManager.getConnection(url, username, password);
				// Creating a PreparedStatement for batch insertion
				PreparedStatement pstmt = conn.prepareStatement(
						"INSERT INTO student_info (student_number, full_name, year, course, gender) VALUES (?,?,?,?,?)")) {

			// Iterating over the list of StudentData objects
			for (StudentData student : data) {
				// Setting parameters for the PreparedStatement
				pstmt.setString(1, student.getStudentNumber());
				pstmt.setString(2, student.getFullName());
				pstmt.setString(3, student.getYear());
				pstmt.setString(4, student.getCourse());
				pstmt.setString(5, student.getGender());
				// Adding the statement to the batch
				pstmt.addBatch();
			}
			// Executing the batch insertion
			pstmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves a list of student data from the database.
	 * 
	 * @return A list of StudentData objects representing the student information.
	 */
	public List<StudentData> getStudentData() {
		// List to store StudentData objects
		List<StudentData> studentList = new ArrayList<>();
		// SQL query to select student information
		String sql = "SELECT student_number, full_name, year, course, gender FROM student_info";

		try {
			// Establishing a database connection
			connect = Database.connect();
			// Creating a PreparedStatement for the SQL query
			prepare = connect.prepareStatement(sql);
			// Executing the query and obtaining the result set
			result = prepare.executeQuery();

			// Iterating over the result set
			while (result.next()) {
				// Creating a StudentData object for each record in the result set
				StudentData student = new StudentData(result.getString("student_number"), result.getString("full_name"),
						result.getString("year"), result.getString("course"), result.getString("gender"));

				// Adding the StudentData object to the list
				studentList.add(student);
			}
		} catch (Exception e) {
			// Printing stack trace in case of an exception
			e.printStackTrace();
		} finally {
			try {
				// Closing result set, prepared statement, and database connection
				if (result != null)
					result.close();
				if (prepare != null)
					prepare.close();
				if (connect != null)
					connect.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Returning the list of student data
		return studentList;
	}
}
