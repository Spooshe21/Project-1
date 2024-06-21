/********************************************************************************************
 *   COPYRIGHT (C) 2024 CREVAVI TECHNOLOGIES PVT LTD
 *   The reproduction, transmission or use of this document/file or its
 *   contents is not permitted without written authorization.
 *   Offenders will be liable for damages. All rights reserved.
 *---------------------------------------------------------------------------
 *   Purpose:  StudentData.java file
 *   Project:  Student Management System
 *   Platform: Cross-platform (Windows, macOS, Linux)
 *   Compiler: JDK-22
 *   IDE:      Eclipse IDE for Enterprise Java and Web Developers (includes Incubating components)
 *	       Version: 2024-03 (4.31.0)
 *             Build id: 20240307-1437
 ********************************************************************************************/

/**
 * This class represents student data and provides methods to access and modify student information.
 */
package application;

import javafx.beans.property.SimpleStringProperty;

public class StudentData {
	// Global variables
	private SimpleStringProperty studentNumber; // Student number
	private SimpleStringProperty fullName; // Full name of the student
	private SimpleStringProperty year; // Year of study
	private SimpleStringProperty course; // Course enrolled
	private SimpleStringProperty gender; // Gender of the student

	/**
	 * Constructor to initialize StudentData object with provided information.
	 * 
	 * @param studentNumber The student number
	 * @param fullName      The full name of the student
	 * @param year          The year of study
	 * @param course        The course enrolled
	 * @param gender        The gender of the student
	 */
	public StudentData(String studentNumber, String fullName, String year, String course, String gender) {
		// Initialize SimpleStringProperty objects with provided values
		this.studentNumber = new SimpleStringProperty(studentNumber);
		this.fullName = new SimpleStringProperty(fullName);
		this.year = new SimpleStringProperty(year);
		this.course = new SimpleStringProperty(course);
		this.gender = new SimpleStringProperty(gender);
	}

	// Getters and setters for accessing and modifying student information

	/**
	 * Gets the student number.
	 * 
	 * @return The student number
	 */
	public String getStudentNumber() {
		return studentNumber.get();
	}

	/**
	 * Sets the student number.
	 * 
	 * @param studentNumber The student number to set
	 */
	public void setStudentNumber(String studentNumber) {
		this.studentNumber.set(studentNumber);
	}

	/**
	 * Gets the full name of the student.
	 * 
	 * @return The full name of the student
	 */
	public String getFullName() {
		return fullName.get();
	}

	/**
	 * Sets the full name of the student.
	 * 
	 * @param fullName The full name of the student to set
	 */
	public void setFullName(String fullName) {
		this.fullName.set(fullName);
	}

	/**
	 * Gets the year of study.
	 * 
	 * @return The year of study
	 */
	public String getYear() {
		return year.get();
	}

	/**
	 * Sets the year of study.
	 * 
	 * @param year The year of study to set
	 */
	public void setYear(String year) {
		this.year.set(year);
	}

	/**
	 * Gets the course enrolled.
	 * 
	 * @return The course enrolled
	 */
	public String getCourse() {
		return course.get();
	}

	/**
	 * Sets the course enrolled.
	 * 
	 * @param course The course enrolled to set
	 */
	public void setCourse(String course) {
		this.course.set(course);
	}

	/**
	 * Gets the gender of the student.
	 * 
	 * @return The gender of the student
	 */
	public String getGender() {
		return gender.get();
	}

	/**
	 * Sets the gender of the student.
	 * 
	 * @param gender The gender of the student to set
	 */
	public void setGender(String gender) {
		this.gender.set(gender);
	}
}
