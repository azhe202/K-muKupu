package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	
	/*
	 * Function to change scenes to New Spelling Quiz when button is pressed
	 */
	public void newSpellingQuiz(ActionEvent event) { 
		try {
			root = FXMLLoader.load(getClass().getResource("NewSpellingQuiz.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//closes stage when exit button pressed
		public void exit(ActionEvent event) {
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.close();
		}
}
