package application;

import java.io.IOException;
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
	
	
	private void start() {
		int i = 1;
		for (String word : GamesModule.wordsForSummary) {
			String[] words = word.split("#");
			speltWords = speltWords + words[0] + "\n";
			if (i==1) {
				indicator1.setImage(new Image("./" + words[1] + ".png"));
			} else if (i==2) {
				indicator2.setImage(new Image("./" + words[1] + ".png"));
			} else if (i==3) {
				indicator3.setImage(new Image("./" + words[1] + ".png"));
			} else if (i==4) {
				indicator4.setImage(new Image("./" + words[1] + ".png"));
			} else if (i==5) {
				indicator5.setImage(new Image("./" + words[1] + ".png"));
			} 
			i++;
		}
		speltWordsLabel.setText(speltWords);
		
	}
	
	public void startNewQuiz(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("CategorySelection.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void goHome(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		start();
		
	}
	
	
}
