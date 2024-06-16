/********************************************************************************************
 *   COPYRIGHT (C) 2022 CREVAVI TECHNOLOGIES PVT LTD
 *   The reproduction, transmission or use of this document/file or its
 *   contents is not permitted without written authorization.
 *   Offenders will be liable for damages. All rights reserved.
 *---------------------------------------------------------------------------
 *   Purpose:  ExcelOperations.java file
 *   Project:  Student Management System
 *   Platform: Cloud Service Provider Independent
 *   Compiler: JavaSE-11
 *   IDE:	   Eclipse IDE for Enterprise Java and Web Developers (includes Incubating components)
 *			   Version: 2021-09 (4.21.0)
 *             Build id: 20210910-1417
 ********************************************************************************************/

/**********************************************************************************************
 * This class handles the import and export of student data to and from Excel files.
 **********************************************************************************************/

package application;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExcelOperations {

	/**********************************************************************************************
	 * List to hold the imported student data.
	 **********************************************************************************************/
	private List<StudentData> dataList = new ArrayList<>();

	/**********************************************************************************************
	 * Reference to the CRUD controller.
	 **********************************************************************************************/
	private CRUDController crudController;

	/**********************************************************************************************
	 * Constructor to initialize the CRUD controller reference.
	 * 
	 * @param crudController Reference to the CRUDController.
	 **********************************************************************************************/
	public ExcelOperations(CRUDController crudController) {
		this.crudController = crudController;
	}

	/**********************************************************************************************
	 * Exports student data to an Excel file.
	 * 
	 * @param event The action event that triggered the export.
	 **********************************************************************************************/
	public void exportToExcel(ActionEvent event) {
		// Create a FileChooser to select the save location for the Excel file
		FileChooser fileChooser = new FileChooser();
		// Add a filter to only show Excel files
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
		// Set the default file name
		fileChooser.setInitialFileName("students.xlsx");

		// Show the save dialog
		File file = fileChooser.showSaveDialog(new Stage());
		if (file != null) {
			try (XSSFWorkbook workbook = new XSSFWorkbook()) {
				// Create a new sheet in the workbook
				Sheet sheet = workbook.createSheet("Students");

				// Create the header row and set the column titles
				Row header = sheet.createRow(0);
				header.createCell(0).setCellValue("Student Number");
				header.createCell(1).setCellValue("Full Name");
				header.createCell(2).setCellValue("Year");
				header.createCell(3).setCellValue("Course");
				header.createCell(4).setCellValue("Gender");

				// Retrieve the list of student data
				List<StudentData> studentList = crudController.studentListData();
				int rowIndex = 1;
				// Populate the sheet with student data
				for (StudentData student : studentList) {
					Row row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(student.getStudentNumber());
					row.createCell(1).setCellValue(student.getFullName());
					row.createCell(2).setCellValue(student.getYear());
					row.createCell(3).setCellValue(student.getCourse());
					row.createCell(4).setCellValue(student.getGender());
				}

				// Write the workbook to the chosen file
				try (FileOutputStream outputStream = new FileOutputStream(file)) {
					workbook.write(outputStream);
					// Success message
					System.out.println("✨ Data exported successfully! Your Excel file is ready to shine. 📊✨");
				}
			} catch (Exception e) {
				// Handle exceptions
				e.printStackTrace();
				System.out.println("⚠️ An error occurred while exporting data to Excel. Please try again.");
			}
		}
	}

	/**********************************************************************************************
	 * Imports student data from an Excel file.
	 * 
	 * @param event The action event that triggered the import.
	 * @return List of StudentData objects containing the imported data.
	 **********************************************************************************************/
	public List<StudentData> importFromExcel(ActionEvent event) {
		// Create a FileChooser to select the Excel file to import
		FileChooser fileChooser = new FileChooser();
		// Add a filter to only show Excel files
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

		// Show the open dialog
		File file = fileChooser.showOpenDialog(new Stage());
		if (file != null) {
			try (FileInputStream fis = new FileInputStream(file); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
				// Get the first sheet from the workbook
				Sheet sheet = workbook.getSheetAt(0);
				List<StudentData> dataList = new ArrayList<>();

				// Iterate through the rows in the sheet
				for (Row row : sheet) {
					if (row.getRowNum() == 0)
						continue; // Skip header row

					// Extract cell values as strings
					String studentNumber = getCellValueAsString(row.getCell(0));
					String fullName = getCellValueAsString(row.getCell(1));
					String year = getCellValueAsString(row.getCell(2));
					String course = getCellValueAsString(row.getCell(3));
					String gender = getCellValueAsString(row.getCell(4));

					// Create a new StudentData object and add it to the list
					StudentData student = new StudentData(studentNumber, fullName, year, course, gender);
					dataList.add(student);
				}

				// Set the data in the TableView
				crudController.getCrud_tableView().setItems(FXCollections.observableArrayList(dataList));
				crudController.getCrud_tableView().refresh();

				// Success message
				System.out.println("Data Imported from Excel Successfully");

				// Display the data in the TableView
				crudController.studentShowData();

				return dataList; // Return the list of imported data
			} catch (Exception e) {
				// Handle exceptions
				e.printStackTrace();
			}
		}
		return new ArrayList<>(); // Return an empty list if file is null
	}

	/**********************************************************************************************
	 * Helper method to get the cell value to a string.
	 * 
	 * @param cell The cell whose value is to be converted.
	 * @return The cell value as a string.
	 **********************************************************************************************/
	private String getCellValueAsString(Cell cell) {
		// Check the type of the cell and return the appropriate string value
		switch (cell.getCellType()) {
		case STRING:
			// Return the string value of the cell
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				// Return the date value as a string if the cell is formatted as a date
				return cell.getDateCellValue().toString();
			} else {
				// Format numeric value without scientific notation
				DecimalFormat df = new DecimalFormat("0");
				return df.format(cell.getNumericCellValue());
			}
		case BOOLEAN:
			// Return the boolean value as a string
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			// Return the formula of the cell as a string
			return cell.getCellFormula();
		case BLANK:
			// Return an empty string if the cell is blank
			return "";
		default:
			// Return an empty string for any other cell types
			return "";
		}
	}
}
