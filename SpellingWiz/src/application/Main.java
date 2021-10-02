package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class Main extends Application {
	
	@FXML
	private ImageView borderImg;
	
	@Override
	public void start(Stage primaryStage) {
		// loads and displays the main menu to the user
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			primaryStage.setTitle("Kēmu Kupu"); // set a title for the application
		   

		    Scene scene = new Scene(root,600,900);
		    primaryStage.setScene(scene);
		    
		    // keep a constant ratio for window width and height
		    primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(1.5));
		    primaryStage.minHeightProperty().bind(scene.widthProperty().divide(1.5));
		    
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();	
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}	
}
