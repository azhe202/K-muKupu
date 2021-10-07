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

public class SceneController extends CategorySelection{

	private Stage stage;
	private Scene scene;
	private Parent root;
	public static boolean isEnglish = true;
	public static String langExt = "";

	@FXML
	private ImageView gamesBtn;
	@FXML
	private ImageView practiseBtn;
	@FXML
	private ImageView langToggle;
	
	public void setUp(MouseEvent event) {
		isEnglish = true;
		langExt = "";
		langToggle.setImage(new Image("./english.jpg"));
		gamesBtn.setImage(new Image("./gamesModule"+langExt+".jpg"));
		practiseBtn.setImage(new Image("./practiseModule"+langExt+".jpg"));
	}

	public void chooseGame(MouseEvent event) { 
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("CategorySelection.fxml"));
			root = loader.load();
			scene = new Scene(root);

			// access the controller and call function to set up the language
			CategorySelection controller = loader.getController();
			controller.setUpLang(event);

			// show GUI to user 
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void choosePractise(MouseEvent event) { 
		try {
			root = FXMLLoader.load(getClass().getResource("PractiseSelection.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void chooseToggle(MouseEvent event) {
		if (isEnglish) {
			isEnglish = false;
			langExt = "m";
			langToggle.setImage(new Image("./maori.jpg"));
		} else {
			isEnglish = true;
			langExt = "";
			langToggle.setImage(new Image("./english.jpg"));
		}
		gamesBtn.setImage(new Image("./gamesModule"+langExt+".jpg"));
		practiseBtn.setImage(new Image("./practiseModule"+langExt+".jpg"));
	}

	public void enterGames(MouseEvent event) throws MalformedURLException { 
		gamesBtn.setImage(new Image("./gamesModuleSelect"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitGames(MouseEvent event) { 
		gamesBtn.setImage(new Image("./gamesModule"+langExt+".jpg"));
	}

	public void enterPractise(MouseEvent event) throws MalformedURLException { 
		practiseBtn.setImage(new Image("./practiseModuleSelect"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitPractise(MouseEvent event) { 
		practiseBtn.setImage(new Image("./practiseModule"+langExt+".jpg"));
	}

	public void enterToggle(MouseEvent event) throws MalformedURLException { 
		if (isEnglish) {
			langToggle.setImage(new Image("./english.jpg"));
		} else {
			langToggle.setImage(new Image("./maori.jpg"));
		}
		Sound.playSound("./switch.wav");
	}

	public void exitToggle(MouseEvent event) { 
		if (isEnglish) {
			langToggle.setImage(new Image("./englishfade.jpg"));
		} else {
			langToggle.setImage(new Image("./maorifade.jpg"));
		}
	}


}
