/********************************************************************************************
 *   COPYRIGHT (C) 2024 CREVAVI TECHNOLOGIES PVT LTD
 *   The reproduction, transmission or use of this document/file or its
 *   contents is not permitted without written authorization.
 *   Offenders will be liable for damages. All rights reserved.
 *---------------------------------------------------------------------------
 *   Purpose:  Main.java file
 *   Project:  Student Management System
 *   Platform: Cross-platform (Windows, macOS, Linux)
 *   Compiler: JDK-22
 *   IDE:  	   Eclipse IDE for Enterprise Java and Web Developers (includes Incubating components)
 *	           Version: 2024-03 (4.31.0)
 *             Build id: 20240307-1437
 ********************************************************************************************/

/**********************************************************************************************
 * This class serves as the main entry point for the JavaFX application.
 **********************************************************************************************/

package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

    /**********************************************************************************************
     * Starts the JavaFX application by setting the primary stage.
     * 
     * @param primaryStage The primary stage for this application.
     **********************************************************************************************/
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));

            // Create the scene with the loaded root node
            Scene scene = new Scene(root);

            // Add the stylesheet to the scene
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            // Set the scene to the primary stage and show it
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**********************************************************************************************
     * The main method to launch the JavaFX application.
     * 
     * @param args Command line arguments.
     **********************************************************************************************/
    public static void main(String[] args) {
        launch(args);
    }
}
