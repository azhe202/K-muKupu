package application;

import java.io.IOException;

/**
 * The Summary class sets up everything for the summary page
 * and supplies functionality for buttons
 * 
 * @author Group 22
 */



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


/**
 * The Summary class sets up everything for the summary page
 * and supplies functionality for buttons
 * 
 * @author Group 22
 */


public class Summary implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private String speltWords = "";
	
	@FXML
	private Label speltWordsLabel;
	@FXML
	private ImageView indicator1;
	@FXML
	private ImageView indicator2;
	@FXML
	private ImageView indicator3;
	@FXML
	private ImageView indicator4;
	@FXML
	private ImageView indicator5;
	@FXML
	private ImageView newQuizBut;
	@FXML
	private ImageView goBackBut;
	@FXML
	private ImageView resultsBanner;
	
	String langExt = Menu.langExt;
	
	
	/**
	 * Function sets languages of images and displays words
	 * from quiz along with correct or incorrect indicator
	 */
	private void start() {
		//sets languages for labels
		resultsBanner.setImage(new Image("./images/Results" + langExt + ".png"));
		
		//Sets words and tick/cross icons
		int i = 1;
		for (String word : GamesModule.wordsForSummary) {
			String[] words = word.split("#");
			speltWords = speltWords + words[0] + "\n";
			if (i==1) {
				indicator1.setImage(new Image("./images/" + words[1] + ".png"));
			} else if (i==2) {
				indicator2.setImage(new Image("./images/" + words[1] + ".png"));
			} else if (i==3) {
				indicator3.setImage(new Image("./images/" + words[1] + ".png"));
			} else if (i==4) {
				indicator4.setImage(new Image("./images/" + words[1] + ".png"));
			} else if (i==5) {
				indicator5.setImage(new Image("./images/" + words[1] + ".png"));
			} 
			i++;
		}
		speltWordsLabel.setText(speltWords);
		
	}
	
	
	/**
	 * Method changes scene to Menu for user to start quiz
	 * @param event
	 * @throws IOException
	 */
	public void startNewQuiz(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("./FXML/Menu.fxml"));
			root = loader.load();
			scene = new Scene(root);

			// access the controller and call function to set up the language
			Menu controller = loader.getController();
			controller.setUpLang(event);

			// show GUI to user 
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method takes user back to RewardScreen
	 * @param event
	 * @throws IOException
	 */
	public void goBack(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("./FXML/RewardScreen.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		start();
		
	}
	
	public void exitNewQuiz(MouseEvent event) { 
		newQuizBut.setImage(new Image("./images/arrow.png"));
	}

	public void enterNewQuiz(MouseEvent event) throws MalformedURLException { 
		newQuizBut.setImage(new Image("./images/arrowSelect.png"));
		Sound.playSound("./sounds/switch.wav");
	}
	
	public void exitGoBack(MouseEvent images) { 
		goBackBut.setImage(new Image("./images/arrow.png"));
	}

	public void enterGoBack(MouseEvent event) throws MalformedURLException { 
		goBackBut.setImage(new Image("./images/arrowSelect.png"));
		Sound.playSound("./sounds/switch.wav");
	}
	
	
}
