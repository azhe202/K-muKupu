package application;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SceneController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private ImageView gamesBtn;
	@FXML
	private ImageView practiseBtn;
	/*
	 * Function to change scenes to New Spelling Quiz when button is pressed
	 */
	/*
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
	*/
	
	public void chooseCategory(MouseEvent event) { 
		try {
			root = FXMLLoader.load(getClass().getResource("CatergorySelection.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enterGames(MouseEvent event) throws MalformedURLException { 
		gamesBtn.setImage(new Image("./gamesModuleSelect.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitGames(MouseEvent event) { 
		gamesBtn.setImage(new Image("./gamesModule.jpg"));
	}
	
	public void enterPractise(MouseEvent event) throws MalformedURLException { 
		practiseBtn.setImage(new Image("./practiseModuleSelect.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitPractise(MouseEvent event) { 
		practiseBtn.setImage(new Image("./practiseModule.jpg"));
	}
	
	/*
	 * Function exits out of the GUI when exit button is pressed
	 */
	public void exit(ActionEvent event) {
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}
}
