package application;

import java.io.IOException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GamesModule extends Controller {

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
	private ImageView frog;
	@FXML
	private ImageView stones;
	@FXML
	private ImageView helpBtn;
	@FXML
	private ImageView helpWindow;
	@FXML
	private Label timeLabel;
	@FXML
	private Label noMoreTimeLabel;
	@FXML
	private Slider voiceSpeedSlider;
	@FXML
	private ImageView speedBtn;
	@FXML
	private ImageView speedWindow;
	@FXML
	public ImageView arrowBtn;
	@FXML
	private ImageView star;
	@FXML
	private ImageView instruction;


	private Stage stage;
	private Scene scene;
	private Parent root;

	public static int wordCount;
	public static float score;
	private int totalSeconds = 31;
	public static ArrayList<String> wordsForSummary = new ArrayList<>();
	private int attempts;
	public static double voiceSpeed;
	public static String word;
	private String englishWord;
	private Boolean skipRequested = false;
	String langExt = SceneController.langExt;
	TranslateTransition translate = new TranslateTransition();
	TranslateTransition translateStar = new TranslateTransition();
	Timer timer = new Timer();
	TimerTask timerTask;

	public void setUpLang(MouseEvent event) throws MalformedURLException {
		// call functions to set up the correct labels
		exitRepeat(event);
		exitTranslate(event);
		exitMacron(event);
		exitSkip(event);
		exitStart(event);
		exitSubmit(event);
		instruction.setImage(new Image("./images/gamesInstructions"+langExt+".jpg"));
	}

	/**
	 * Function to start the game
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void startSpellingGame(MouseEvent event) throws IOException, InterruptedException {
		startGame.setVisible(false);
		instruction.setVisible(false);
		wordsForSummary.clear();
		translate.setNode(frog);
		translate.setDuration(Duration.millis(1000));
		translate.setByX(126);
		
		translateStar.setNode(star);
		translateStar.setDuration(Duration.millis(1000));
		translateStar.setByY(-170);
		translateStar.setAutoReverse(true);
		translateStar.setCycleCount(2);

		//disables game related buttons and enables submit button
		startGame.setDisable(true);
		checkSpelling.setDisable(false);
		repeatWordBtn.setDisable(false);
		translationBtn.setDisable(false);
		skipWordBtn.setDisable(false);
		macronBtn.setDisable(false);


		// get the word list 
		String wordList = CategorySelection.wordList;

		// give random word
		String command = "sort -u words/" + wordList + " | shuf -n 5";
		String[] words = returnWordList(command);

		// extract the maori and english words from the selected words
		String[] englishWords = new String[5];
		for (int i=0; i<words.length; i++) {
			String temp = words[i];
			String tempArray[] = temp.split("#");
			words[i] = tempArray[0].trim();
			englishWords[i] = tempArray[1];
		}

		// starting word count and score
		wordCount = 1;
		score = 0;

		SpellingThread speakWord; 

		for (int i=0; i<words.length; i++) {
			textField.clear();
			noMoreTimeLabel.setText("");

			word = words[i]; // maori words
			englishWord = englishWords[i];

			attempts = 0;

			displayNumLetters(word); // display how many letters are in the word
			
			// get the speed of the voice 
			voiceSpeed = voiceSpeedSlider.getValue();

			// Start a new thread to say the word to spell to user
			speakWord = new SpellingThread();
			speakWord.start();

			// start timer
			startTimer();

			attempts++;

			// create a skip request when don't know is pressed
			skipWordBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					skipRequested = true;
					resume();
				}
			});

			// checkSpelling button will check the word and increase the score or ask user to spell again
			checkSpelling.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override 
				public void handle(MouseEvent e) {
					

					String wordEntered = textField.getText().trim();

					// conditional checks to increase user score
					if (word.equalsIgnoreCase(wordEntered) && attempts == 1) {
						wordsForSummary.add(word + "#Correct");
						pauseTimer(); 
						if (noMoreTimeLabel.getText().equals("Time's Up") || noMoreTimeLabel.getText().equals("Kaore o wa i toe")) {
							score+= 0.5;
							star.setImage(new Image("./images/halfStar.png"));
							translateStar.play();
						} else {
							score++;
							star.setImage(new Image("./images/filledStar.png"));
							translateStar.play();
						}
						translate.play();
						textField.clear();
						wordCount++;
						Sound.playSound("./sounds/correctSound.mp3");
						resume(); // resume function after check spelling button has been pressed
					} else if (wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						wordsForSummary.add(word + "#Correct");
						pauseTimer();
						if (noMoreTimeLabel.getText().equals("Time's Up") || noMoreTimeLabel.getText().equals("Kaore o wa i toe")) {
							score+= 0.5;
							star.setImage(new Image("./images/halfStar.png"));
							translateStar.play();
						} else {
							score++;
							star.setImage(new Image("./images/filledStar.png"));
							translateStar.play();
						}
						translate.play();
						textField.clear();
						wordCount++;
						Sound.playSound("./sounds/correctSound.mp3");
						resume(); // resume function after check spelling button has been pressed
					} else if(!wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						pauseTimer();
						wordsForSummary.add(word + "#Incorrect");
						textField.clear();
						wordCount++;
						Sound.playSound("./sounds/incorrectSound.mp3");
						resume(); // resume function after check spelling button has been pressed
					} else if (!wordEntered.equalsIgnoreCase(word)){
						voiceSpeed = voiceSpeedSlider.getValue();
						SpellingThread2 repeat = new SpellingThread2(); // call the function again to ask user to spell word again
						repeat.start(); // call the function again to ask user to spell word again 
						textField.clear();
						attempts++;
						Sound.playSound("./sounds/incorrectSound.mp3");
						
					}
				}

			});

			// temporarily pause the method and wait for resume to be called
			pause();

			// skips the current word as per user request
			if (skipRequested) {
				pauseTimer(); // when skip is requested then pause and delete the old timer
				Sound.playSound("sounds/incorrectSound.mp3");
				wordsForSummary.add(word + "#Incorrect");
				skipRequested = false;
				wordCount++;
				continue;
			}
			
			// remove existing hints
			translationHint.setText("");

		}

		//disables game related buttons and enables start button
		startGame.setDisable(false);
		checkSpelling.setDisable(true);
		repeatWordBtn.setDisable(true);
		translationBtn.setDisable(true);
		skipWordBtn.setDisable(true);
		macronBtn.setDisable(true);
		
		root = FXMLLoader.load(getClass().getResource("RewardScreen.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
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
		RepeatThread repeat = new RepeatThread();
		repeat.start();
	}

	/*
	 * Function allowing the user to enter a macron
	 */
	public void insertMacron(MouseEvent event) {
		addMacron(event);
	}

	/* 
	 * Help button functionality
	 */

	boolean helpOpen = false;

	public void selectHelp(MouseEvent event) {
		if (helpOpen) {
			helpOpen = false;
			helpWindow.setVisible(false);
		} else {
			helpOpen = true;
			helpWindow.setImage(new Image("./images/helpWindow"+langExt+".jpg"));
			helpWindow.setVisible(true);
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
	
	

	public void enterHelp(MouseEvent event) {
		helpBtn.setImage(new Image("./images/help.jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitHelp(MouseEvent event) {
		helpBtn.setImage(new Image("./images/helpfade.jpg"));
	}


	/**
	 * Functionality for the timer
	 */
	public void setTimer() {

		// add a new timer task for count down timer 
		timerTask = new TimerTask() {

			@Override
			public void run() {
				totalSeconds--;

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						if(totalSeconds == 0) {
							timeLabel.setText("");
							if(SceneController.langExt == "m") {
								noMoreTimeLabel.setText("Kaore o wa i toe");
							} else {
								noMoreTimeLabel.setText("Time's Up");
							}
							totalSeconds = 31;
							timer.cancel();
						} else {
							timeLabel.setText(Integer.toString(totalSeconds));
						}
					}
				});
			}

		};
		timer.schedule(timerTask, 0, 1200);	
	}

	/**
	 * Function creates a new timer
	 */
	public void startTimer() {
		timer = new Timer();
		setTimer();
	}

	/**
	 * Function pauses timer and deletes the current timer 
	 */
	public void pauseTimer() {
		timer.cancel();
		totalSeconds = 31;
	}


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
		checkSpelling.setImage(new Image("./images/submit"+langExt+".jpg"));
		Sound.playSound("./sounds/switch.wav");
	}

	public void exitSubmit(MouseEvent event) { 
		checkSpelling.setImage(new Image("./images/submitfade"+langExt+".jpg"));
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
	
	public void back(MouseEvent event) { 
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("CategorySelection.fxml"));
			root = loader.load();
			scene = new Scene(root);

			// access the controller and call function to set up the language
			CategorySelection controller = loader.getController();
			controller.setUpLang(event);

			// show GUI to user 
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
