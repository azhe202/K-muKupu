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

/**
 * The CategorySelection class lets the user select a word list to be quizzed on
 * @author Group 22
 *
 */
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
	@FXML
	public ImageView prompt;

	public static String wordList;
	String langExt = Menu.langExt;

	/**
	 * Method will change the category labels to the appropriate label (english/maori)
	 * @param event
	 * @throws MalformedURLException
	 */
	public void setUpLang(MouseEvent event) throws MalformedURLException {
		// set up labels in their unselected state in the appropriate language
		exitFood(event);
		exitWeather(event);
		exitColours(event);
		exitAnimals(event);
		exitFeelings(event);
		exitDirections(event);
		exitMonths(event);
		exitMonths2(event);
		prompt.setImage(new Image("./images/categoryPrompt"+langExt+".jpg"));
	}

	/**
	 * This method loads the appropriate module when a word list has been chosen (games/practise)
	 * @param event
	 * @throws Exception
	 */
	public void chooseModule(MouseEvent event) throws Exception {
		if(Menu.moduleSelected.equals("PractiseModule")) {
			FXMLLoader loader = changeScene("./FXML/PractiseModule.fxml", event);
			// access the controller and call function to set up the language
			PractiseModule controller = loader.getController();
			controller.setUpLang(event);
		} else {
			FXMLLoader loader = changeScene("./FXML/GamesModule.fxml", event);
			// access the controller and call function to set up the language
			GamesModule controller = loader.getController();
			controller.setUpLang(event);
		}
	}

	// the following methods detect when a word list has been selected
	
	public void chooseFood(MouseEvent event) throws Exception {
		wordList = "food";
		chooseModule(event);
	}

	public void chooseWeather(MouseEvent event) throws Exception {
		wordList = "weather";
		chooseModule(event);
	}

	public void chooseColours(MouseEvent event) throws Exception {
		wordList = "colours";
		chooseModule(event);
	}

	public void chooseAnimals(MouseEvent event) throws Exception {
		wordList = "animals";
		chooseModule(event);
	}

	public void chooseFeelings(MouseEvent event) throws Exception {
		wordList = "feelings";
		chooseModule(event);
	}

	public void chooseDirections(MouseEvent event) throws Exception {
		wordList = "compassPoints";
		chooseModule(event);
	}

	public void chooseMonths(MouseEvent event) throws Exception {
		wordList = "monthsOfTheYear";
		chooseModule(event);
	}

	public void chooseMonths2(MouseEvent event) throws Exception {
		wordList = "monthsOfTheYearLoanWords";
		chooseModule(event);
	}
	
	/**
	 * This method loads the Menu screen when user clicks back
	 * @param event
	 */
	public void backToMenu(MouseEvent event) { 
		FXMLLoader loader = changeScene("./FXML/Menu.fxml", event);
		Menu controller = loader.getController();
		controller.setUpLang(event);
	}

	// The following methods deal with updating the buttons when the user hovers over them

	// food
	public void enterFood(MouseEvent event) throws MalformedURLException { 
		foodBtn.setImage(new Image("./images/food"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitFood(MouseEvent event) { 
		foodBtn.setImage(new Image("./images/foodfade"+langExt+".jpg"));
	}

	// weather
	public void enterWeather(MouseEvent event) throws MalformedURLException { 
		weatherBtn.setImage(new Image("./images/weather"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitWeather(MouseEvent event) { 
		weatherBtn.setImage(new Image("./images/weatherfade"+langExt+".jpg"));
	}

	// colours
	public void enterColours(MouseEvent event) throws MalformedURLException { 
		coloursBtn.setImage(new Image("./images/colours"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitColours(MouseEvent event) { 
		coloursBtn.setImage(new Image("./images/coloursfade"+langExt+".jpg"));
	}

	// animals
	public void enterAnimals(MouseEvent event) throws MalformedURLException { 
		animalsBtn.setImage(new Image("./images/animals"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitAnimals(MouseEvent event) { 
		animalsBtn.setImage(new Image("./images/animalsfade"+langExt+".jpg"));
	}

	// feelings
	public void enterFeelings(MouseEvent event) throws MalformedURLException { 
		feelingsBtn.setImage(new Image("./images/feelings"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitFeelings(MouseEvent event) { 
		feelingsBtn.setImage(new Image("./images/feelingsfade"+langExt+".jpg"));
	}

	// directions
	public void enterDirections(MouseEvent event) throws MalformedURLException { 
		directionsBtn.setImage(new Image("./images/directions"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitDirections(MouseEvent event) { 
		directionsBtn.setImage(new Image("./images/directionsfade"+langExt+".jpg"));
	}

	// months1
	public void enterMonths(MouseEvent event) throws MalformedURLException { 
		monthsBtn.setImage(new Image("./images/months"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitMonths(MouseEvent event) { 
		monthsBtn.setImage(new Image("./images/monthsfade"+langExt+".jpg"));
	}

	// months2
	public void enterMonths2(MouseEvent event) throws MalformedURLException { 
		months2Btn.setImage(new Image("./images/months2"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitMonths2(MouseEvent event) { 
		months2Btn.setImage(new Image("./images/months2fade"+langExt+".jpg"));
	}

	//arrow
	public void enterArrow(MouseEvent event) throws MalformedURLException { 
		arrowBtn.setImage(new Image("./images/arrowSelect.jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitArrow(MouseEvent event) { 
		arrowBtn.setImage(new Image("./images/arrow.jpg"));
	}

	/**
	 * This method assists in loading new scenes on users request
	 * @param nextScene
	 * @param event
	 * @return
	 */
	public FXMLLoader changeScene(String nextScene, MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(nextScene));
			root = loader.load();
			scene = new Scene(root);

			// show GUI to user 
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();

			return loader;

		} catch (IOException e) {
			e.printStackTrace();
			return new FXMLLoader();

		}

	}
}
