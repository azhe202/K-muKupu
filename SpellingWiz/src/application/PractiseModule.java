package application;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
	private ImageView checkSpelling;
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
	
	public static int wordCount;
	public static int score;
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
	
	public void setUpLang(MouseEvent event) throws MalformedURLException {
		// call functions to set up the correct labels
		exitRepeat(event);
		exitTranslate(event);
		exitMacron(event);
		exitSkip(event);
		exitStart(event);
		exitSubmit(event);
	}

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

	/**
	 * Function to start the practice game
	 */
	public void startSpellingGame() {
		startGame.setVisible(false);
		startGame.setDisable(true);
		repeatWordBtn.setDisable(false);
		translationBtn.setDisable(false);
		skipWordBtn.setDisable(false);
		macronBtn.setDisable(false);
		checkSpelling.setDisable(false);
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
			System.out.println(wordListWord);


			// extract the maori and english words from the selected words
			String temp = wordListWord;
			String tempArray[] = temp.split("#");
			word = tempArray[0].trim();
			englishWord = tempArray[1];
		
			
			System.out.println(word + " " + englishWord);

			// starting word count and score
			wordCount = 1;
			score = 0;

			textField.clear();

			attempts = 0;

			displayNumLetters(word); // display how many letters are in the word

			// get the speed of the voice 
			voiceSpeed = voiceSpeedSlider.getValue();
			
			// Start a new thread to say the word to spell to user
			PractiseThread practiseThread = new PractiseThread();
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


			// checkSpelling button will check the word or ask user to spell again
			checkSpelling.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override 
				public void handle(MouseEvent e) {

					String wordEntered = textField.getText().trim();

					// conditional checks to increase user score
					if (word.equalsIgnoreCase(wordEntered) && attempts == 1) {
						textField.clear();
						wordCount++;
						Sound.playSound("./correctSound.mp3");
						resume(); // resume function after check spelling button has been pressed
					} else if (wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						textField.clear();
						wordCount++;
						Sound.playSound("./correctSound.mp3");
						resume(); // resume function after check spelling button has been pressed
					} else if(!wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						bashCommand("echo You can do it! | festival --tts"); // encouraging message for user
						textField.clear();
						wordCount++;
						Sound.playSound("./incorrectSound.mp3");
						resume(); // resume function after check spelling button has been pressed
					} else if (!wordEntered.equalsIgnoreCase(word)){
						displayCorrectLetters(word, wordEntered);
						voiceSpeed = voiceSpeedSlider.getValue();
						spellingQuestion(word, 0, 1, 5, voiceSpeed); // call the function again to ask user to spell word again 
						// display to the user the appropriate hint
	
						textField.clear();
						attempts++;
						Sound.playSound("./incorrectSound.mp3");

					}
				}


			});

			// temporarily pause the method and wait for resume to be called
			pause();

			// skips the current word as per user request
			if (skipRequested) {
				bashCommand("echo You can do it! | festival --tts");
				skipRequested = false;
				wordCount++;
				continue;
			}

		}

		//disables game related buttons and enables start button
		startGame.setDisable(false);
		repeatWordBtn.setDisable(true);
		translationBtn.setDisable(true);
		skipWordBtn.setDisable(true);
		macronBtn.setDisable(true);
		checkSpelling.setDisable(true);
	}
	
	public void displayCorrectLetters(String word, String spelling) { 
		String textToDisplay = "";
		int diff = 0;

		diff = word.length() - spelling.length();
		if (diff < 0) {
			diff = 0;
		}

		for (int i = 0; i < word.length() - diff; i++) {
			if (word.charAt(i) == spelling.charAt(i)) {
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
	
	public void translate(MouseEvent event) {
		translationHint.setText("Hint: the english translation is " + englishWord);
	}
	
	/**
	 * Function allowing user to repeat a word 
	 */
	public void wordRepeat(MouseEvent event) {
		voiceSpeed = voiceSpeedSlider.getValue();
		repeatWord(voiceSpeed, word);
	}
	
	/*
	 * Function allowing the user to enter a macron
	 */
	public void insertMacron(MouseEvent event) {
		addMacron(event);
	}
	
	public void enterRepeat(MouseEvent event) throws MalformedURLException { 
		repeatWordBtn.setImage(new Image("./repeat"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitRepeat(MouseEvent event) { 
		repeatWordBtn.setImage(new Image("./repeatfade"+langExt+".jpg"));
	}

	public void enterMacron(MouseEvent event) throws MalformedURLException { 
		macronBtn.setImage(new Image("./macron"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitMacron(MouseEvent event) { 
		macronBtn.setImage(new Image("./macronfade"+langExt+".jpg"));
	}

	public void enterTranslate(MouseEvent event) throws MalformedURLException { 
		translationBtn.setImage(new Image("./translate"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitTranslate(MouseEvent event) { 
		translationBtn.setImage(new Image("./translatefade"+langExt+".jpg"));
	}

	public void enterSkip(MouseEvent event) throws MalformedURLException { 
		skipWordBtn.setImage(new Image("./skip"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitSkip(MouseEvent event) { 
		skipWordBtn.setImage(new Image("./skipfade"+langExt+".jpg"));
	}

	public void enterSubmit(MouseEvent event) throws MalformedURLException { 
		checkSpelling.setImage(new Image("./submit"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitSubmit(MouseEvent event) { 
		checkSpelling.setImage(new Image("./submitfade"+langExt+".jpg"));
	}
	
	public void enterStart(MouseEvent event) throws MalformedURLException { 
		startGame.setImage(new Image("./start"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitStart(MouseEvent event) { 
		startGame.setImage(new Image("./startfade"+langExt+".jpg"));
	}
	
	public void enterSpeed(MouseEvent event) throws MalformedURLException { 
		speedBtn.setImage(new Image("./speed"+langExt+".jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitSpeed(MouseEvent event) { 
		speedBtn.setImage(new Image("./speedfade"+langExt+".jpg"));
	}
	
	public void enterArrow(MouseEvent event) throws MalformedURLException { 
		arrowBtn.setImage(new Image("./arrowSelect.jpg"));
		Sound.playSound("./switch.wav");
	}

	public void exitArrow(MouseEvent event) { 
		arrowBtn.setImage(new Image("./arrow.jpg"));
	}
	
	public void back(MouseEvent event) { 
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("PractiseSelection.fxml"));
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
	
	boolean speedOpen = false;
	
	public void selectSpeed(MouseEvent event) {
		if (speedOpen) {
			speedOpen = false;
			voiceSpeedSlider.setVisible(false);
			speedWindow.setVisible(false);
		} else {
			speedOpen = true;
			voiceSpeedSlider.setVisible(true);
			speedWindow.setVisible(true);
		}
	}
	
	
	
}
