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

	@FXML
	private ImageView randomBtn;
	@FXML
	private ImageView selectBtn;
	@FXML
	private ImageView arrowBtn;
	
	public static String moduleSelected = "NotSelected";

	public void chooseRandom(MouseEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("PractiseModule.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void chooseSelect(MouseEvent event) {
		moduleSelected = "PractiseModule";
		try {
			root = FXMLLoader.load(getClass().getResource("CategorySelection.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void backToMenu(MouseEvent event) { 
		try {
			root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	//random
	public void enterRandom(MouseEvent event) throws MalformedURLException { 
		randomBtn.setImage(new Image("./randomise.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitRandom(MouseEvent event) { 
		randomBtn.setImage(new Image("./randomisefade.jpg"));
	}
	//select
	public void enterSelect(MouseEvent event) throws MalformedURLException { 
		selectBtn.setImage(new Image("./select.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitSelect(MouseEvent event) { 
		selectBtn.setImage(new Image("./selectfade.jpg"));
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
