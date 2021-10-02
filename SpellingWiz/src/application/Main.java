package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		// loads and displays the main menu to the user
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			primaryStage.setTitle("Kēmu Kupu"); // set a title for the application
			Scene scene = new Scene(root,1900,900);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false); // fix the size of GUI
			primaryStage.show();	
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}	
}
