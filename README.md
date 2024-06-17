# Student Management System

## Overview

The Student Management System is a JavaFX-based application designed to manage student details. It includes functionalities for user login, account registration, CRUD operations on student data, and import/export capabilities with a PostgreSQL database. The application also provides features for data validation and user notifications.

## Table of Contents

- **Features**
- **Technologies Used**
- **Setup and Installation**
- **Usage**
- **Project Structure**
- **License**

## Features

- **User Authentication:** Secure login and registration.
- **CRUD Operations:** Create, read, update, and delete student records.
- **Data Export/Import:** Export records to Excel/CSV files and import records from Excel/CSV files.
- **User Notifications:** Informative alert messages for user actions and validations.
- **Responsive UI:** User-friendly interface with JavaFX controls like input fields, labels, buttons, table View, table columns and combo boxes.

## Technologies Used

- **JavaSE-22:** Core programming language.
- **JavaFX:** For building the graphical user interface.
- **PostgreSQL:** Database management system for storing student details.
- **Eclipse IDE:** Development environment used for this project.
- **JDBC:** For database connectivity.

## Setup and Installation

### Prerequisites

- Java Development Kit (JDK) 22
- Eclipse IDE for Enterprise Java and Web Developers
- PostgreSQL Database
- JavaFX SceneBuilder
- Apache POI jar files.
- Postgresql-42.6.0 jar file.

### Steps

1. **Clone the repository:**
 
   git clone <C:\Users\DELL\git\repository18\.git>

2. **Open the project in Eclipse IDE:**

   - Select `File` > `Open Projects from File System...`
   - Navigate to the cloned repository and open it.

### Configure the database

1. **Create a PostgreSQL database:**
   - Ensure PostgreSQL is installed.
   - Create a new database for the project.

2. **Execute the SQL script:**
   - Locate `schema.sql` in `src/main/resources/db/`.
   - Execute the script to create necessary tables.

# Update database credentials:

1. Open `Database.java` in `src/application`.
2. Update the connection URL, username, and password variables with your PostgreSQL database credentials.

# Run the application:

1. Right-click on `Main.java`.
2. Select `Run As` > `Java Application`.

## Usage

- **Login:** Enter username and password. Default credentials are in the database setup script.
- **Register:** Create a new account using the registration form.
- **Student Management:**
  - **Add Student:** Fill out the student details form and save.
  - **Update Student:** Select a student from the table and update details after confirmation.
  - **Delete Student:** Delete a student after confirmation.
  - **Clear:** Clears the info typed in inputfield.
- **Export/Import:**
  - **Export records** to Excel/CSV using the export button.
  - **Import records** from Excel/CSV using the import button.

## Project Structure

|-- src/
| |-- application/
| |-- Main.java
| |-- MainController.java
| |-- CRUDController.java
| |-- Database.java
| |-- StudentData.java
| |-- CSVOperations.java
| |-- ExcelOperations.java
| |-- Main.fxml
| |-- CrudForm.fxml
| |-- application.css
| |-- crudDesign.css
|-- resources/
| |-- db/
| |-- schema.sql
|-- README.md


- **Main.java:** Entry point for the JavaFX application.
- **MainController.java:** Manages user interactions,database operations and login and register credentials.
- **CRUDController.java:** Handles the database operations along with file operations.
- **Database.java:** Handles PostgreSQL database connections and CRUD Operations.
- **StudentData.java:** Defines student entity attributes.
- **CSVOperations.java:** Import and Export the CSV files.
- **ExcelOperations.java:** Import and Export the Excel files.
- **Main.fxml:** Layout for login and registration forms.
- **CrudForm.fxml:** Layout for CRUD operations on student data.
- **application.css:** Styles for the JavaFX application of login and signup pages.
- **crudDesign.css:** Styles for the JavaFX application of Student Database Records(Dashboard).

## License

/********************************************************************************************

COPYRIGHT (C) 2022 CREVAVI TECHNOLOGIES PVT LTD
The reproduction, transmission or use of this document/file or its
contents is not permitted without written authorization.
Offenders will be liable for damages. All rights reserved.
********************************************************************************************/
