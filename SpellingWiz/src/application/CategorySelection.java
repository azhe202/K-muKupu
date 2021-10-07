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

public class CategorySelection{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	public ImageView foodBtn;
	@FXML
	public ImageView weatherBtn;
	@FXML
	public ImageView coloursBtn;
	@FXML
	public ImageView animalsBtn;
	@FXML
	public ImageView feelingsBtn;
	@FXML
	public ImageView directionsBtn;
	@FXML
	public ImageView monthsBtn;
	@FXML
	public ImageView months2Btn;
	@FXML
	public ImageView arrowBtn;
	
	public static String wordList;
	String langExt = SceneController.langExt;
	
	/**
	 * Change to the Games Module when the category is chosen
	 * @param event
	 */
	public void changeGamesModule(MouseEvent event) {
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
	 * Change to Practice module when the category is chosen
	 * @param event
	 */
	public void changePractiseModule(MouseEvent event) {
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
	
	public void changeScenes(MouseEvent event) {
		if(PractiseSelection.moduleSelected.equals("PractiseModule")) {
			changePractiseModule(event);
		} else {
			changeGamesModule(event);
		}
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
		foodBtn.setImage(new Image("./food"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}
	

	public void exitFood(MouseEvent event) { 
		foodBtn.setImage(new Image("./foodfade"+langExt+".jpg"));
	}
	//weather
	public void enterWeather(MouseEvent event) throws MalformedURLException { 
		weatherBtn.setImage(new Image("./weather"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitWeather(MouseEvent event) { 
		weatherBtn.setImage(new Image("./weatherfade"+langExt+".jpg"));
	}
	//colours
	public void enterColours(MouseEvent event) throws MalformedURLException { 
		coloursBtn.setImage(new Image("./colours"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitColours(MouseEvent event) { 
		coloursBtn.setImage(new Image("./coloursfade"+langExt+".jpg"));
	}
	
	//animals
	public void enterAnimals(MouseEvent event) throws MalformedURLException { 
		animalsBtn.setImage(new Image("./animals"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitAnimals(MouseEvent event) { 
		animalsBtn.setImage(new Image("./animalsfade"+langExt+".jpg"));
	}
	//feelings
	public void enterFeelings(MouseEvent event) throws MalformedURLException { 
		feelingsBtn.setImage(new Image("./feelings"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitFeelings(MouseEvent event) { 
		feelingsBtn.setImage(new Image("./feelingsfade"+langExt+".jpg"));
	}
	//directions
	public void enterDirections(MouseEvent event) throws MalformedURLException { 
		directionsBtn.setImage(new Image("./directions"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitDirections(MouseEvent event) { 
		directionsBtn.setImage(new Image("./directionsfade"+langExt+".jpg"));
	}
	//month1
	public void enterMonths(MouseEvent event) throws MalformedURLException { 
		monthsBtn.setImage(new Image("./months"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitMonths(MouseEvent event) { 
		monthsBtn.setImage(new Image("./monthsfade"+langExt+".jpg"));
	}
	//month2
	public void enterMonths2(MouseEvent event) throws MalformedURLException { 
		months2Btn.setImage(new Image("./months2"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitMonths2(MouseEvent event) { 
		months2Btn.setImage(new Image("./months2fade"+langExt+".jpg"));
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
