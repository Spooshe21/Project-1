/********************************************************************************************
 *   COPYRIGHT (C) 2022 CREVAVI TECHNOLOGIES PVT LTD
 *   The reproduction, transmission or use of this document/file or its
 *   contents is not permitted without written authorization.
 *   Offenders will be liable for damages. All rights reserved.
 *---------------------------------------------------------------------------
 *   Purpose:  MainController.java file
 *   Project:  Student Management System
 *   Platform: JavaSE-22.0.1
 *   IDE:      Eclipse IDE for Enterprise Java and Web Developers (includes Incubating components)
 *	       Version: 2024-03 (4.31.0)
 *             Build id: 20240307-1437
 ********************************************************************************************/

/**********************************************************************************************
 * This class handles the login and registration functionalities for the application.
 **********************************************************************************************/

package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {

	// FXML variables for login form
	@FXML
	private BorderPane loginForm;

	@FXML
	private Button createAccountButton;

	@FXML
	private TextField usernameField;

	@FXML
	private TextField passwordField;

	@FXML
	private Button loginButton;

	// FXML variables for signup form
	@FXML
	private BorderPane signupForm;

	@FXML
	private Button loginAccountButton;

	@FXML
	private TextField signupUsernameField;

	@FXML
	private TextField signupPasswordField;

	@FXML
	private Button signupButton;

	// Database connection variables
	/**
	 * Represents the connection to the database.
	 */
	private Connection connect;
	/**
	 * Represents a precompiled SQL statement, allowing efficient execution multiple
	 * times.
	 */
	private PreparedStatement prepare;
	/**
	 * Represents the result of a database query.
	 */
	private ResultSet result;

	/**********************************************************************************************
	 * Handles the login functionality by validating user credentials. Attempts to
	 * log in the user based on the provided username and password. Retrieves the
	 * username and password from the admin table in the database and compares them
	 * with the provided values. If successful, opens the CRUD form and hides the
	 * login window. If unsuccessful due to missing fields, displays an error
	 * message prompting the user to fill all fields. If unsuccessful due to
	 * incorrect username/password combination, displays an error message.
	 * 
	 **********************************************************************************************/
	public void loginAccount() {
		String sql = "SELECT username, password FROM admin WHERE username=? and password=?";

		// Establishes a connection to the database
		connect = Database.connect();

		try {
			Alert alert;
			if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
				// Displays an error message if username or password is empty
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields");
				alert.showAndWait();
			} else {
				prepare = connect.prepareStatement(sql);
				prepare.setString(1, usernameField.getText());
				prepare.setString(2, passwordField.getText());
				result = prepare.executeQuery();

				if (result.next()) {
					// Displays a success message if login is successful
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Message");
					alert.setHeaderText(null);
					alert.setContentText("Login Successful");
					alert.showAndWait();
					// Opens the CRUD form and hides the login window
					loginButton.getScene().getWindow().hide();

					Parent root = FXMLLoader.load(getClass().getResource("/application/CrudForm.fxml"));

					Stage stage = new Stage();
					Scene scene = new Scene(root);

					stage.setScene(scene);
					stage.show();
				} else {
					// Displays an error message if login fails
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Message");
					alert.setHeaderText(null);
					alert.setContentText("Incorrect Username/Password");
					alert.showAndWait();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Closes database resources
			closeResources();
		}
	}

	/**********************************************************************************************
	 * Handles the registration functionality by creating a new user account.
	 * Registers a new account with the provided username and password. Inserts the
	 * username and password into the admin table in the database. Checks if the
	 * username is already taken and if the password meets the minimum length
	 * requirement. Displays appropriate error messages for invalid input or if the
	 * username is already taken. Upon successful registration, displays a success
	 * message and clears the signup form.
	 **********************************************************************************************/
	public void registerAccount() {
		String sql = "INSERT INTO admin(username, password) VALUES(?, ?)";
		// Establishes a connection to the database
		connect = Database.connect();

		try {
			Alert alert;
			if (signupUsernameField.getText().isEmpty() || signupPasswordField.getText().isEmpty()) {
				// Displays an error message if username or password is empty
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields");
				alert.showAndWait();
			} else {
				String checkData = "SELECT username FROM admin WHERE username= '" + signupUsernameField.getText() + "'";

				prepare = connect.prepareStatement(checkData);
				result = prepare.executeQuery();

				if (result.next()) {
					alert = new Alert(AlertType.ERROR);
					// Displays an error message if the username is already taken
					alert.setTitle("Error Message");
					alert.setHeaderText(null);
					alert.setContentText(signupUsernameField.getText() + " is already taken");
					alert.showAndWait();
				} else {
					if (signupPasswordField.getText().length() < 8) {
						// Displays an error message if the password is too short
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText("Invalid Password, at least 8 characters needed");
						alert.showAndWait();
					} else {
						// Inserts the new account into the database
						prepare = connect.prepareStatement(sql);
						prepare.setString(1, signupUsernameField.getText());
						prepare.setString(2, signupPasswordField.getText());
						prepare.executeUpdate();

						// Displays a success message upon successful registration
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Message");
						alert.setHeaderText(null);
						alert.setContentText("Successfully created a new account!");
						alert.showAndWait();

						// Clears the signup form and switches to the login form
						loginForm.setVisible(true);
						signupForm.setVisible(false);
						signupUsernameField.setText("");
						signupPasswordField.setText("");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Closes database resources
			closeResources();
		}
	}

	/**********************************************************************************************
	 * Switches between the login and signup forms based on user actions.
	 * 
	 * @param event the event triggered by clicking a button.
	 **********************************************************************************************/
	public void switchForm(ActionEvent event) {
		if (event.getSource() == loginAccountButton) {
			// If the login account button is clicked, show the login form and hide the
			// signup form
			loginForm.setVisible(true);
			signupForm.setVisible(false);
		} else if (event.getSource() == createAccountButton) {
			// If the create account button is clicked, hide the login form and show the
			// signup form
			loginForm.setVisible(false);
			signupForm.setVisible(true);
		}
	}

	/**********************************************************************************************
	 * Closes the database resources (ResultSet, PreparedStatement, and Connection)
	 * to prevent resource leaks.
	 **********************************************************************************************/
	private void closeResources() {
		try {
			if (result != null)
				result.close(); // Close ResultSet if it is not null
			if (prepare != null)
				prepare.close(); // Close PreparedStatement if it is not null
			if (connect != null)
				connect.close(); // Close Connection if it is not null
		} catch (SQLException e) {
			e.printStackTrace(); // Print stack trace if an SQLException occurs during resource closing
		}
	}
}
