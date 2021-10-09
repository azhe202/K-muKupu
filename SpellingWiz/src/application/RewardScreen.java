package application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RewardScreen implements Initializable{
	
	@FXML
	private ImageView star1;
	@FXML
	private ImageView star2;
	@FXML
	private ImageView star3;
	@FXML
	private ImageView star4;
	@FXML
	private ImageView star5;
	@FXML
	private Label messageLabel;
	@FXML
	private ImageView nextPage;

	private Stage stage;
	private Scene scene;
	private Parent root;

	public void start() {
		messageLabel.setText("Unlucky, maybe try again to \npractice your spelling.");
		
		if (GamesModule.score>=1) {
			star1.setImage(new Image("./filledStar.png"));
		} if (GamesModule.score>=2) {
			messageLabel.setText("Good job, you should try giving it \nanother go and see if you can \nget a higher score!");
			star2.setImage(new Image("./filledStar.png"));
		} if (GamesModule.score>=3) {
			star3.setImage(new Image("./filledStar.png"));
		} if (GamesModule.score>=4) {
			messageLabel.setText("Great job! You should try \nanother word list now.");
			star4.setImage(new Image("./filledStar.png"));
		} if (GamesModule.score==5) {
			messageLabel.setText("Awesome! Try another word list \nand see if you can get \nfull marks in that too!");
			star5.setImage(new Image("./filledStar.png"));
		}
	}
	
	public void switchToSummary(MouseEvent event) throws IOException { 
			root = FXMLLoader.load(getClass().getResource("Summary.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		start();
		
	}
	
	public void exitNextPage(MouseEvent event) { 
		nextPage.setImage(new Image("./arrow.png"));
	}

	public void enterNextPage(MouseEvent event) throws MalformedURLException { 
		nextPage.setImage(new Image("./arrowSelect.png"));
		Sound.playSound("./switch.wav");
	}
	
	
	
}
