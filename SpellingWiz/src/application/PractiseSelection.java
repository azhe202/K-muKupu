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

/**
 * The PractiseSelection class lets the user choose to randomise words or select a word list
 * @author Group 22
 *
 */
public class PractiseSelection {

	private Stage stage;
	private Scene scene;
	private Parent root;
	String langExt = Menu.langExt;

	@FXML
	private ImageView randomBtn;
	@FXML
	private ImageView selectBtn;
	@FXML
	private ImageView arrowBtn;
	@FXML
	private ImageView practisePrompt;
	
	public static boolean randomSelected = false;
	
	/**
	 * Method will change the category labels to the appropriate label (english/maori)
	 * @param event
	 * @throws MalformedURLException
	 */
	public void setUpLang(MouseEvent event) throws MalformedURLException {
		exitRandom(event);
		exitSelect(event);
		practisePrompt.setImage(new Image("./images/practisePrompt"+langExt+".jpg"));
	}

	/**
	 * This method loads the Practise Module when the user chooses to randomise words
	 * @param event
	 * @throws Exception
	 */
	public void chooseRandom(MouseEvent event) throws MalformedURLException {
		randomSelected = true;
		FXMLLoader loader = changeScene("./FXML/PractiseModule.fxml", event);
		PractiseModule controller = loader.getController();
		controller.setUpLang(event);
	}

	/**
	 * This method loads the Category Selection screen when the user chooses to select a word list
	 * @param event
	 * @throws Exception
	 */
	public void chooseSelect(MouseEvent event) throws MalformedURLException {
		FXMLLoader loader = changeScene("./FXML/CategorySelection.fxml", event);
		CategorySelection controller = loader.getController();
		controller.setUpLang(event);
	}
	
	/**
	 * This method loads the Menu screen when user clicks back
	 * @param event
	 */
	public void backToMenu(MouseEvent event) { 
		FXMLLoader loader = changeScene("./FXML/Menu.fxml", event);
		Menu controller = loader.getController();
		controller.setUpLang(event);
	}
	
	// The following methods deal with updating the buttons when the user hovers over them

	//random
	public void enterRandom(MouseEvent event) throws MalformedURLException { 
		randomBtn.setImage(new Image("./images/randomise"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitRandom(MouseEvent event) { 
		randomBtn.setImage(new Image("./images/randomisefade"+langExt+".jpg"));
	}
	//select
	public void enterSelect(MouseEvent event) throws MalformedURLException { 
		selectBtn.setImage(new Image("./images/select"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitSelect(MouseEvent event) { 
		selectBtn.setImage(new Image("./images/selectfade"+langExt+".jpg"));
	}

	//arrow
	public void enterArrow(MouseEvent event) throws MalformedURLException { 
		arrowBtn.setImage(new Image("./images/arrowSelect.jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitArrow(MouseEvent event) { 
		arrowBtn.setImage(new Image("./images/arrow.jpg"));
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
