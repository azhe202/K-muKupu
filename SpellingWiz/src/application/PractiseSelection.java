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
		practisePrompt.setImage(new Image("./practisePrompt"+langExt+".jpg"));
	}

	public void chooseRandom(MouseEvent event) {
		randomSelected = true;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("PractiseModule.fxml"));
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
	
	public void backToMenu(MouseEvent event) { 
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Menu.fxml"));
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
		randomBtn.setImage(new Image("./randomise"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitRandom(MouseEvent event) { 
		randomBtn.setImage(new Image("./randomisefade"+langExt+".jpg"));
	}
	//select
	public void enterSelect(MouseEvent event) throws MalformedURLException { 
		selectBtn.setImage(new Image("./select"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitSelect(MouseEvent event) { 
		selectBtn.setImage(new Image("./selectfade"+langExt+".jpg"));
	}

	//arrow
	public void enterArrow(MouseEvent event) throws MalformedURLException { 
		arrowBtn.setImage(new Image("./arrowSelect.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitArrow(MouseEvent event) { 
		arrowBtn.setImage(new Image("./arrow.jpg"));
	}
}
