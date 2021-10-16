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

public class PractiseSelection {

	private Stage stage;
	private Scene scene;
	private Parent root;
	String langExt = SceneController.langExt;

	@FXML
	private ImageView randomBtn;
	@FXML
	private ImageView selectBtn;
	@FXML
	private ImageView arrowBtn;
	@FXML
	private ImageView practisePrompt;
	
	public static boolean randomSelected = false;
	
	public void setUpLang(MouseEvent event) throws MalformedURLException {
		exitRandom(event);
		exitSelect(event);
		practisePrompt.setImage(new Image("./images/practisePrompt"+langExt+".jpg"));
	}

	public void chooseRandom(MouseEvent event) {
		randomSelected = true;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("./FXML/PractiseModule.fxml"));
			root = loader.load();
			scene = new Scene(root);

			// access the controller and call function to set up the language
			PractiseModule controller = loader.getController();
			controller.setUpLang(event);

			// show GUI to user 
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void chooseSelect(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("./FXML/CategorySelection.fxml"));
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
	
	public void backToMenu(MouseEvent event) { 
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("./FXML/Menu.fxml"));
			root = loader.load();
			scene = new Scene(root);

			// access the controller and call function to set up the language
			SceneController controller = loader.getController();
			controller.setUpLang(event);

			// show GUI to user 
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
}
