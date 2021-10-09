package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PractiseModule {
	@FXML
	private ImageView helpBtn;
	@FXML
	private ImageView helpWindow;
	@FXML
	private Button startGame;
	@FXML
	private Button repeatWordBtn;
	@FXML
	private Button skipWordBtn;
	@FXML
	private Button translationBtn;
	@FXML 
	private Button macronBtn;
	@FXML
	private TextField textField;
	@FXML
	private Button checkSpelling;
	@FXML
	private Label translationHint;
	
	
	boolean helpOpen = false;
	
	public void selectHelp(MouseEvent event) {
		if (helpOpen) {
			helpOpen = false;
			helpWindow.setVisible(false);
		} else {
			helpOpen = true;
			helpWindow.setVisible(true);
		}
	}
	
	public void enterHelp(MouseEvent event) {
		helpBtn.setImage(new Image("./help.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitHelp(MouseEvent event) {
		helpBtn.setImage(new Image("./helpfade.jpg"));
	}
}
