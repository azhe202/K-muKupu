package application;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The Practise Module class controls all the functionality within the Practise Module
 * @author Group 22
 *
 */
public class PractiseModule extends Controller {
	@FXML
	private ImageView helpBtn;
	@FXML
	private ImageView helpWindow;
	@FXML
	private ImageView startGame;
	@FXML
	private ImageView repeatWordBtn;
	@FXML
	private ImageView skipWordBtn;
	@FXML
	private ImageView translationBtn;
	@FXML 
	private ImageView macronBtn;
	@FXML
	private TextField textField;
	@FXML
	private ImageView submitBtn;
	@FXML
	private Label translationHint;
	@FXML
	private ImageView speedBtn;
	@FXML
	private ImageView speedWindow;
	@FXML
	private Slider voiceSpeedSlider;
	@FXML
	private ImageView arrowBtn;
	@FXML
	private ImageView instruction;
	
	private int attempts;
	public static double voiceSpeed;
	public static String word;
	private String englishWord;
	private Boolean skipRequested = false;
	private String wordList;
	private String[] wordPoolFileNames = {"animals", "colours", "compassPoints", "daysOfTheWeek", "daysOfTheWeekLoanWords", 
			"feelings", "food", "monthsOfTheYear", "monthsOfTheYearLoanWords"};

	boolean helpOpen = false;
	boolean gameInPlay;
	String langExt = SceneController.langExt;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	/**
	 * Function sets up the language chosen by the user
	 * @param event
	 * @throws MalformedURLException
	 */
	public void setUpLang(MouseEvent event) throws MalformedURLException {
		// call functions to set up the correct labels
		exitRepeat(event);
		exitTranslate(event);
		exitMacron(event);
		exitSkip(event);
		exitStart(event);
		exitSubmit(event);
		instruction.setImage(new Image("./images/practiseInstructions"+langExt+".jpg"));
	}

	/**
	 * Function to start the practice game
	 */
	public void startSpellingGame() {
		startGame.setVisible(false);
		instruction.setVisible(false);
		
		gameInPlay = true;

		while(gameInPlay) {

			// get the word lists
			if(PractiseSelection.randomSelected) {
				// get a random number between 1 - 8
				int random = (int)(Math.random() * 8 + 1);
				// select that wordList from the string of wordList 
				wordList = wordPoolFileNames[random];	
			} else {
				wordList = CategorySelection.wordList;
			}

			// give a random word from the word list 
			String command = "sort -u words/" + wordList + " | shuf -n 1";
			String wordListWord = returnWord(command);

			// extract the maori and english words from the selected words
			String temp = wordListWord;
			String tempArray[] = temp.split("#");
			word = tempArray[0].trim();
			englishWord = tempArray[1];

			textField.clear();

			attempts = 0;

			displayNumLetters(word); // display how many letters are in the word

			// get the speed of the voice 
			voiceSpeed = voiceSpeedSlider.getValue();
			
			// Start a new thread to say the word to spell to user
			PractiseThread practiseThread = new PractiseThread();
			practiseThread.setDaemon(true);
			practiseThread.start();

			attempts++;

			// create a skip request when don't know is pressed
			skipWordBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					skipRequested = true;
					resume();
				}
			});


			// submit button will check the word or ask user to spell again
			submitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override 
				public void handle(MouseEvent e) {
					assess();
				}


			});
			
			// enable user to use enter key to submit word
			textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent ke) {
			        if (ke.getCode().equals(KeyCode.ENTER)) {
			            assess();
			        }
			    }
			});

			// temporarily pause the method and wait for resume to be called
			pause();

			// skips the current word as per user request
			if (skipRequested) {
				Sound.playSound("sounds/incorrectSound.mp3");
				skipRequested = false;
			}
			
			// remove the translation hint 
			translationHint.setText("");
		}

	}
	
	/**
	 * Function will be called upon the submit button being pressed and will 
	 * assess the correctness of the word typed by the user
	 */
	public void assess() {

		String wordEntered = textField.getText().trim();

		// conditional checks to increase user score
		if (word.equalsIgnoreCase(wordEntered) && attempts == 1) {
			correctSpelling(textField);
			resume(); // resume function after check spelling button has been pressed
		} else if (wordEntered.equalsIgnoreCase(word) && attempts == 2) {
			correctSpelling(textField);
			resume(); // resume function after check spelling button has been pressed
		} else if(!wordEntered.equalsIgnoreCase(word) && attempts == 2) {	
			incorrectSpelling(textField);
			displayCorrectWord(word);
		} else if (!wordEntered.equalsIgnoreCase(word)){
			incorrectSpelling(textField);
			displayCorrectLetters(word, wordEntered);
			voiceSpeed = voiceSpeedSlider.getValue();
			spellingQuestion(word, 1, voiceSpeed); // call the function again to ask user to spell word again
			attempts++;
		}
	}
	
	
	/**
	 * Function displays to the user the correct spelling of the given word for 
	 * a certain time frame
	 * @param word
	 * @param event
	 */
	public void displayCorrectWord(String word) {
		
		disableButtons();
		
		// format word to line up with underscore placement
		String displayWord = "";
		for (int i = 0; i < word.length(); i++) {
			displayWord = (displayWord + word.charAt(i) + " ");
		}
		
		super.wordLength.setText(displayWord);
		PauseTransition pause = new PauseTransition(Duration.seconds(4.0));
		pause.setOnFinished( e -> {
			enableButtons();
			resume();
		});
		pause.play();
		
	}
	
	/**
	 * Disables all buttons on the screen excluding the back button
	 */
	public void disableButtons() {
		repeatWordBtn.setDisable(true);
		translationBtn.setDisable(true);
		skipWordBtn.setDisable(true);
		macronBtn.setDisable(true);
		submitBtn.setDisable(true);
		helpBtn.setDisable(true);
		speedBtn.setDisable(true);
	}
	
	/**
	 * Enables all buttons on the screen
	 */
	public void enableButtons() {
		repeatWordBtn.setDisable(false);
		translationBtn.setDisable(false);
		skipWordBtn.setDisable(false);
		macronBtn.setDisable(false);
		submitBtn.setDisable(false);
		helpBtn.setDisable(false);
		speedBtn.setDisable(false);
	}
	
	/**
	 * Display the any correct letters of the word if the user has the letters in the correct position
	 * @param word
	 * @param spelling
	 */
	public void displayCorrectLetters(String word, String spelling) { 
		String textToDisplay = "";
		int diff = 0;

		diff = word.length() - spelling.length();
		if (diff < 0) {
			diff = 0;
		}

		// check if any letter are in the correct position
		for (int i = 0; i < word.length() - diff; i++) {
			String wordChar = String.valueOf(word.charAt(i));
			String spellingChar = String.valueOf(spelling.charAt(i));
			if (wordChar.toLowerCase().equals(spellingChar.toLowerCase())) {
				textToDisplay = (textToDisplay + word.charAt(i) + " "); 
			} else {
				textToDisplay = (textToDisplay + "_ "); 
			}
		}

		for (int i = 0; i < diff; i++) {
			textToDisplay = (textToDisplay + "_ "); 
		}
		
		super.wordLength.setText(textToDisplay);
	}
	
	
	/**
	 * Function to translate word from maori to english
	 */
	public void translate(MouseEvent event) {
		if (SceneController.isEnglish) {
			translationHint.setText("Translation: " + englishWord);
		} else {
			translationHint.setText("Whakamāori: " + englishWord);
		}
		
	}
	
	/**
	 * Function allowing user to repeat a word 
	 */
	public void wordRepeat(MouseEvent event) {
		voiceSpeed = voiceSpeedSlider.getValue();
		repeatWord(voiceSpeed, word);
	}
	
	/**
	 * Function allowing the user to enter a macron
	 */
	public void insertMacron(MouseEvent event) {
		addMacron(event);
	}
	
	/**
	 * Functionality for fading and non fading of all buttons if selected
	 * @param event
	 * @throws MalformedURLException
	 */
	public void enterRepeat(MouseEvent event) throws MalformedURLException { 
		repeatWordBtn.setImage(new Image("./images/repeat"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitRepeat(MouseEvent event) { 
		repeatWordBtn.setImage(new Image("./images/repeatfade"+langExt+".jpg"));
	}

	public void enterMacron(MouseEvent event) throws MalformedURLException { 
		macronBtn.setImage(new Image("./images/macron"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitMacron(MouseEvent event) { 
		macronBtn.setImage(new Image("./images/macronfade"+langExt+".jpg"));
	}

	public void enterTranslate(MouseEvent event) throws MalformedURLException { 
		translationBtn.setImage(new Image("./images/translate"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitTranslate(MouseEvent event) { 
		translationBtn.setImage(new Image("./images/translatefade"+langExt+".jpg"));
	}

	public void enterSkip(MouseEvent event) throws MalformedURLException { 
		skipWordBtn.setImage(new Image("./images/skip"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitSkip(MouseEvent event) { 
		skipWordBtn.setImage(new Image("./images/skipfade"+langExt+".jpg"));
	}

	public void enterSubmit(MouseEvent event) throws MalformedURLException { 
		submitBtn.setImage(new Image("./images/submit"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitSubmit(MouseEvent event) { 
		submitBtn.setImage(new Image("./images/submitfade"+langExt+".jpg"));
	}
	
	public void enterStart(MouseEvent event) throws MalformedURLException { 
		startGame.setImage(new Image("./images/start"+langExt+".png"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitStart(MouseEvent event) { 
		startGame.setImage(new Image("./images/startfade"+langExt+".png"));
	}
	
	public void enterSpeed(MouseEvent event) throws MalformedURLException { 
		speedBtn.setImage(new Image("./images/speed.jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitSpeed(MouseEvent event) { 
		speedBtn.setImage(new Image("./images/speedfade.jpg"));
	}
	
	public void enterArrow(MouseEvent event) throws MalformedURLException { 
		arrowBtn.setImage(new Image("./images/arrowSelect.jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitArrow(MouseEvent event) { 
		arrowBtn.setImage(new Image("./images/arrow.jpg"));
	}
	
	public void exitWindow(MouseEvent event) {
		helpOpen = false;
		helpWindow.setVisible(false);
	}
	
	public void enterHelp(MouseEvent event) {
		helpBtn.setImage(new Image("./images/help.jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitHelp(MouseEvent event) {
		helpBtn.setImage(new Image("./images/helpfade.jpg"));
	}
	
	/**
	 * Functionality to for back arrow to return to practise selection page
	 * @param event
	 */
	public void back(MouseEvent event) { 
		PractiseSelection.randomSelected = false;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("./FXML/PractiseSelection.fxml"));
			root = loader.load();
			scene = new Scene(root);

			// access the controller and call function to set up the language
			PractiseSelection controller = loader.getController();
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
	 * Functionality to open and hide the help window
	 * @param event
	 */
	public void selectHelp(MouseEvent event) {
		super.selectHelp(event, helpWindow, langExt);
	}
	
	/**
	 * Functionality to open and hide the speed window
	 * @param event
	 */
	
	public void selectSpeed(MouseEvent event) {
		super.selectSpeed(event, voiceSpeedSlider, speedWindow);
	}
	
	
	
}
