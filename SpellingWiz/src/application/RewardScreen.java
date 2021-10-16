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
	private ImageView messageLabel;
	@FXML
	private ImageView nextPage;
	
	String langExt = SceneController.langExt;

	private Stage stage;
	private Scene scene;
	private Parent root;

	public void start() {
		messageLabel.setImage(new Image("./images/horribleMessage" + langExt + ".png"));
		
		if (GamesModule.score>=0.5) {
			star1.setImage(new Image("./images/halfStar.png"));
		} if (GamesModule.score>=1) {
			star1.setImage(new Image("./images/filledStar.png"));
		}if (GamesModule.score>=1.5) {
			star2.setImage(new Image("./images/halfStar.png"));
		} if (GamesModule.score>=2) {
			star2.setImage(new Image("./images/filledStar.png"));
		}if (GamesModule.score>=2.5) {
			star3.setImage(new Image("./images/halfStar.png"));
		} if (GamesModule.score>=3) {
			messageLabel.setImage(new Image("./images/badMessage" + langExt + ".png"));
			star3.setImage(new Image("./images/filledStar.png"));
		}if (GamesModule.score>=3.5) {
			star4.setImage(new Image("./images/halfStar.png"));
		} if (GamesModule.score>=4) {
			messageLabel.setImage(new Image("./images/decentMessage" + langExt + ".png"));
			star4.setImage(new Image("./images/filledStar.png"));
		}if (GamesModule.score>=4.5) {
			star5.setImage(new Image("./images/halfStar.png"));
		} if (GamesModule.score==5) {
			messageLabel.setImage(new Image("./images/goodMessage" + langExt + ".png"));
			star5.setImage(new Image("./images/filledStar.png"));
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
		nextPage.setImage(new Image("./images/arrow.png"));
	}

	public void enterNextPage(MouseEvent event) throws MalformedURLException { 
		nextPage.setImage(new Image("./images/arrowSelect.png"));
		Sound.playSound("./sounds/switch.wav");
	}
	
	
	
}
