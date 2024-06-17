/********************************************************************************************
 *   COPYRIGHT (C) 2022 CREVAVI TECHNOLOGIES PVT LTD
 *   The reproduction, transmission or use of this document/file or its
 *   contents is not permitted without written authorization.
 *   Offenders will be liable for damages. All rights reserved.
 *---------------------------------------------------------------------------
 *   Purpose:  CSVOperations.java file
 *   Project:  Student Management System
 *   Platform: JavaSE-22.0.1
 *   IDE:  	   Eclipse IDE for Enterprise Java and Web Developers (includes Incubating components)
 *	           Version: 2024-03 (4.31.0)
 *             Build id: 20240307-1437
 ********************************************************************************************/

package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**********************************************************************************************
 * This class handles the CSV operations such as exporting data to CSV and
 * importing data from CSV for the student management system.
 **********************************************************************************************/
public class CSVOperations {

    private List<StudentData> dataList = new ArrayList<>();    // A list to store StudentData objects read from a CSV file.

    // Instance of CRUDController to handle create, read, update, and delete operations
    private CRUDController crudController;
    
    /**
     * Constructor for CSVOperations class.
     * 
     * @param crudController an instance of CRUDController to handle create, read, update, and delete operations.
     */
    public CSVOperations(CRUDController crudController) {
        // Assigns the provided CRUDController instance to the instance variable
        this.crudController = crudController;
    }

    /*************************************************************************************
     * This method exports student data to a CSV file.
     * 
     * @param event the ActionEvent triggered when the export button is clicked.
     *************************************************************************************/
    public void exportToCSV(ActionEvent event) {
        // Create a FileChooser for saving the CSV file
        FileChooser fileChooser = new FileChooser();
        // Set file type filter to CSV
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv")); 
        
        fileChooser.setInitialFileName("Student Database.csv"); // Set default file name

        // Show the save dialog and get the selected file
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            // Try-with-resources to ensure FileWriter is closed properly
            try (FileWriter writer = new FileWriter(file)) {
                // Retrieve student data from the controller
                List<StudentData> studentList = crudController.getStudentData();
                // Write the CSV header
                writer.append("Student Number,Full Name,Year,Course,Gender\n");

                // Write each student's data to the CSV file
                for (StudentData student : studentList) {
                    writer.append(student.getStudentNumber()).append(",");
                    writer.append(student.getFullName()).append(",");
                    writer.append(student.getYear()).append(",");
                    writer.append(student.getCourse()).append(",");
                    writer.append(student.getGender()).append("\n");
                }
                // Success message
                System.out.println("CSV Export Complete! Your student data is now safely stored in a new file.");

            } catch (IOException e) {
                e.printStackTrace(); // Print stack trace if an IOException occurs
                // Show error message to the user
            }
        }
    }

    /*************************************************************************************
     * Imports student data from a CSV file and displays it in a TableView.
     * 
     * @param event The ActionEvent triggered when the import button is clicked
     * @return a list of StudentData objects imported from the CSV file.
     *************************************************************************************/
    public List<StudentData> importFromCSV(ActionEvent event) {
        // Create a FileChooser for opening the CSV file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Show the open dialog and get the selected file
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            // Try-with-resources to ensure BufferedReader is closed properly
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                TableView<StudentData> tableView = crudController.getCrud_tableView();
                ObservableList<StudentData> tableItems = FXCollections.observableArrayList();

                // Skip the header line if present
                reader.readLine();

                // Read each line from the CSV file
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 5) {
                        // Assuming the CSV file has columns: Student Number, Full Name, Year, Course, Gender
                        StudentData student = new StudentData(data[0], data[1], data[2], data[3], data[4]);
                        tableItems.add(student); // Add the student data to the list
                    }
                }

                // Set the items in the TableView and refresh it
                tableView.setItems(tableItems);
                tableView.refresh();

                // Success message
                System.out.println("Data Imported from CSV Successfully");
                crudController.studentShowData();
                return tableItems; // Return the list of imported data
            } catch (IOException e) {
                e.printStackTrace();
                // Show error message to the user
            }
        }
        return new ArrayList<>(); // Return an empty list if file is null
    }

    /*************************************************************************************
     * This method returns the data list.
     * 
     * @return List<StudentData> The list of student data.
     *************************************************************************************/
    public List<StudentData> getData() {
        return dataList; // assuming you have a List<StudentData> dataList field
    }

}
