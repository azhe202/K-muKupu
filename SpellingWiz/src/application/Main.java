package application;

import java.util.concurrent.ScheduledExecutorService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.*;

/**
 * This class is the entry point of the Kemu Kupu application
 * @author Group 22
 *
 */
public class Main extends Application {
	
	public static boolean isEnglish = true;
	public static String langExt = "";
	
	@Override
	public void start(Stage primaryStage) {
		// loads and displays the main menu to the user
		try {
			Parent root = FXMLLoader.load(getClass().getResource("./FXML/Menu.fxml"));
			primaryStage.setTitle("Kēmu Kupu"); 
		   

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
