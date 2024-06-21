/********************************************************************************************
 *   COPYRIGHT (C) 2024 CREVAVI TECHNOLOGIES PVT LTD
 *   The reproduction, transmission or use of this document/file or its
 *   contents is not permitted without written authorization.
 *   Offenders will be liable for damages. All rights reserved.
 *---------------------------------------------------------------------------
 *   Purpose:  Database.java file
 *   Project:  Student Management System
 *   Platform: Cross-platform (Windows, macOS, Linux)
 *   Compiler: JDK-22
 *   IDE:  	   Eclipse IDE for Enterprise Java and Web Developers (includes Incubating components)
 *	           Version: 2024-03 (4.31.0)
 *             Build id: 20240307-1437
 ********************************************************************************************/

package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**********************************************************************************************
 * This class handles the database connection for the student management system.
 **********************************************************************************************/
public class Database {

    /*************************************************************************************
     * This method establishes a connection to the PostgreSQL database.
     * 
     * @return Connection The database connection object.
     *************************************************************************************/
    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/javafxCRUD", "postgres", "Crevavi");
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*************************************************************************************
     * This method retrieves all student data from the database.
     * 
     * @return List<StudentData> A list of student data objects.
     *************************************************************************************/
    public List<StudentData> getStudentData() {
        List<StudentData> studentList = new ArrayList<>();
        String sql = "SELECT * FROM student_info";

        try {
            Connection connect = connect();
            PreparedStatement prepare = connect.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                StudentData student = new StudentData(result.getString("student_number"), result.getString("full_name"),
                        result.getString("year"), result.getString("course"), result.getString("gender"));
                studentList.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentList;
    }

    /*************************************************************************************
     * This method adds a new student to the database.
     * 
     * @param student The student data object to be added.
     *************************************************************************************/
    public void addStudent(StudentData student) {
        String sql = "INSERT INTO student_info (student_number, full_name, year, course, gender) VALUES (?,?,?,?,?)";

        try {
            Connection connect = connect();
            PreparedStatement prepare = connect.prepareStatement(sql);
            prepare.setString(1, student.getStudentNumber());
            prepare.setString(2, student.getFullName());
            prepare.setString(3, student.getYear());
            prepare.setString(4, student.getCourse());
            prepare.setString(5, student.getGender());
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean isStudentNumberTaken(String studentNumber) {
        String sql = "SELECT 1 FROM student_info WHERE student_number = ?";
        try (Connection connect = connect();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, studentNumber);
            try (ResultSet result = prepare.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*************************************************************************************
     * This method updates an existing student in the database.
     * 
     * @param student The student data object to be updated.
     *************************************************************************************/
    public void updateStudent(StudentData student) {
        String sql = "UPDATE student_info SET full_name =?, year =?, course =?, gender =? WHERE student_number =?";

        try {
            Connection connect = connect();
            PreparedStatement prepare = connect.prepareStatement(sql);
            prepare.setString(1, student.getFullName());
            prepare.setString(2, student.getYear());
            prepare.setString(3, student.getCourse());
            prepare.setString(4, student.getGender());
            prepare.setString(5, student.getStudentNumber());
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*************************************************************************************
     * This method deletes a student from the database.
     * 
     * @param studentNumber The student number of the student to be deleted.
     *************************************************************************************/
    public void deleteStudent(String studentNumber) {
        String sql = "DELETE FROM student_info WHERE student_number =?";

        try {
            Connection connect = connect();
            PreparedStatement prepare = connect.prepareStatement(sql);
            prepare.setString(1, studentNumber);
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
