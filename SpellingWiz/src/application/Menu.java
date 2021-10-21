package application;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Menu extends CategorySelection{

	private Stage stage;
	private Scene scene;
	private Parent root;
	public static boolean isEnglish = true;
	public static String langExt = "";
	public static String moduleSelected = "NotSelected";

	@FXML
	private ImageView gamesBtn;
	@FXML
	private ImageView practiseBtn;
	@FXML
	private ImageView langToggle;
	
	/**
	 * Method will change the category labels to the appropriate label (english/maori)
	 * @param event
	 * @throws MalformedURLException
	 */
	public void setUpLang(MouseEvent event) {
		langToggle.setImage(new Image("./images/langTogglefade"+langExt+".jpg"));
		gamesBtn.setImage(new Image("./images/gamesModule"+langExt+".jpg"));
		practiseBtn.setImage(new Image("./images/practiseModule"+langExt+".jpg"));
	}

	/**
	 * This method loads the games module on users request
	 * @param event
	 * @throws Exception
	 */
	public void chooseGame(MouseEvent event) throws MalformedURLException { 
		FXMLLoader loader = changeScene("./FXML/GamesModule.fxml", event);
		GamesModule controller = loader.getController();
		controller.setUpLang(event);
	}

	/**
	 * This method loads the practise module on users request
	 * @param event
	 * @throws Exception
	 */
	public void choosePractise(MouseEvent event) throws MalformedURLException { 
		FXMLLoader loader = changeScene("./FXML/PractiseSelection.fxml", event);
		PractiseSelection controller = loader.getController();
		controller.setUpLang(event);
	}

	/**
	 * This method detects when the user chooses to update the language settings and updates app accordingly
	 * @param event
	 */
	public void chooseToggle(MouseEvent event) {
		if (isEnglish) {
			isEnglish = false;
			langExt = "m";
		} else {
			isEnglish = true;
			langExt = "";
		}
		langToggle.setImage(new Image("./images/langToggle"+langExt+".jpg"));
		gamesBtn.setImage(new Image("./images/gamesModule"+langExt+".jpg"));
		practiseBtn.setImage(new Image("./images/practiseModule"+langExt+".jpg"));
	}

	// the following methods detect when a word list has been selected
	
	public void enterGames(MouseEvent event) throws MalformedURLException { 
		gamesBtn.setImage(new Image("./images/gamesModuleSelect"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitGames(MouseEvent event) { 
		gamesBtn.setImage(new Image("./images/gamesModule"+langExt+".jpg"));
	}

	public void enterPractise(MouseEvent event) throws MalformedURLException { 
		practiseBtn.setImage(new Image("./images/practiseModuleSelect"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitPractise(MouseEvent event) { 
		practiseBtn.setImage(new Image("./images/practiseModule"+langExt+".jpg"));
	}

	public void enterToggle(MouseEvent event) throws MalformedURLException { 
		langToggle.setImage(new Image("./images/langToggle"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitToggle(MouseEvent event) { 
		langToggle.setImage(new Image("./images/langTogglefade"+langExt+".jpg"));
	}

	/**
	 * This method assists in loading new scenes on users request
	 * @param nextScene
	 * @param event
	 * @return
	 */
	public FXMLLoader changeScene(String nextScene, MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(nextScene));
			root = loader.load();
			scene = new Scene(root);

			// show GUI to user 
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();

			return loader;

		} catch (IOException e) {
			e.printStackTrace();
			return new FXMLLoader();

		}

	}

}
