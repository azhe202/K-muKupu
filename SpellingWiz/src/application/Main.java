package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.image.ImageView;

public class Main extends Application {
	
	public static boolean isEnglish = true;
	public static String langExt = "";
	
	@FXML
	private ImageView borderImg;
	
	@Override
	public void start(Stage primaryStage) {
		// loads and displays the main menu to the user
		try {
			Parent root = FXMLLoader.load(getClass().getResource("./FXML/Menu.fxml"));
			primaryStage.setTitle("Kēmu Kupu"); // set a title for the application
		   

		    Scene scene = new Scene(root,1350,900);
		    primaryStage.setScene(scene);
		    
		    // keep a constant window width and height
		    primaryStage.setResizable(false);
		    
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
