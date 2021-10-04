package application;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class CategorySelection{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private ImageView foodBtn;
	@FXML
	private ImageView weatherBtn;
	@FXML
	private ImageView coloursBtn;
	@FXML
	private ImageView animalsBtn;
	@FXML
	private ImageView feelingsBtn;
	@FXML
	private ImageView directionsBtn;
	@FXML
	private ImageView monthsBtn;
	@FXML
	private ImageView months2Btn;
	@FXML
	private ImageView arrowBtn;
	
	String wordList;
	/**
	 * Change to the Games Module when the category is chosen
	 * @param event
	 */
	public void changeScenes(MouseEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("GamesModule.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Function will return the given word list from the category
	 * @return
	 */
	public String getWordList() {
		return wordList;
	}
	
	public void chooseFood(MouseEvent event) {
		wordList = "food";
		changeScenes(event);
	}
	
	public void chooseWeather(MouseEvent event) {
		wordList = "weather";
		changeScenes(event);
	}
	
	public void chooseColours(MouseEvent event) {
		wordList = "colours";
		changeScenes(event);
	}
	
	public void chooseAnimals(MouseEvent event) {
		wordList = "animals";
		changeScenes(event);
	}
	
	public void chooseFeelings(MouseEvent event) {
		wordList = "feelings";
		changeScenes(event);
	}
	
	public void chooseDirections(MouseEvent event) {
		wordList = "compassPoints";
		changeScenes(event);
	}
	
	public void chooseMonths(MouseEvent event) {
		wordList = "monthsOfTheYear";
		changeScenes(event);
	}
	
	public void chooseMonths2(MouseEvent event) {
		wordList = "monthsOfTheYearLoanWords";
		changeScenes(event);
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
	//food
	public void enterFood(MouseEvent event) throws MalformedURLException { 
		foodBtn.setImage(new Image("./food.jpg"));
		Sound.playSound("./switch.wav");
	}
	

	public void exitFood(MouseEvent event) { 
		foodBtn.setImage(new Image("./foodfade.jpg"));
	}
	//weather
	public void enterWeather(MouseEvent event) throws MalformedURLException { 
		weatherBtn.setImage(new Image("./weather.jpg"));
		Sound.playSound("./switch.wav");
		
	}

	public void exitWeather(MouseEvent event) { 
		weatherBtn.setImage(new Image("./weatherfade.jpg"));
	}
	//colours
	public void enterColours(MouseEvent event) throws MalformedURLException { 
		coloursBtn.setImage(new Image("./colours.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitColours(MouseEvent event) { 
		coloursBtn.setImage(new Image("./coloursfade.jpg"));
	}
	
	//animals
	public void enterAnimals(MouseEvent event) throws MalformedURLException { 
		animalsBtn.setImage(new Image("./animals.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitAnimals(MouseEvent event) { 
		animalsBtn.setImage(new Image("./animalsfade.jpg"));
	}
	//feelings
	public void enterFeelings(MouseEvent event) throws MalformedURLException { 
		feelingsBtn.setImage(new Image("./feelings.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitFeelings(MouseEvent event) { 
		feelingsBtn.setImage(new Image("./feelingsfade.jpg"));
	}
	//directions
	public void enterDirections(MouseEvent event) throws MalformedURLException { 
		directionsBtn.setImage(new Image("./directions.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitDirections(MouseEvent event) { 
		directionsBtn.setImage(new Image("./directionsfade.jpg"));
	}
	//month1
	public void enterMonths(MouseEvent event) throws MalformedURLException { 
		monthsBtn.setImage(new Image("./months.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitMonths(MouseEvent event) { 
		monthsBtn.setImage(new Image("./monthsfade.jpg"));
	}
	//month2
	public void enterMonths2(MouseEvent event) throws MalformedURLException { 
		months2Btn.setImage(new Image("./months2.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitMonths2(MouseEvent event) { 
		months2Btn.setImage(new Image("./months2fade.jpg"));
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
