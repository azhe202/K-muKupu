package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GamesModule extends Controller {
	
	@FXML
	private Button startGame;
	@FXML
	private Button repeatWordBtn;
	@FXML
	private Button skipWordBtn;
	@FXML
	private Button translationBtn;
	@FXML 
	private Button macronBtn;
	@FXML
	private TextField textField;
	@FXML
	private Button checkSpelling;
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
	
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public static int wordCount;
	public static int score;
	private int totalSeconds = 31;
	private int secondsPassed;
	public static ArrayList<String> wordsForSummary = new ArrayList<>();
	private int attempts;
	public static double voiceSpeed;
	public static String word;
	private String englishWord;
	private Boolean skipRequested = false;
	TranslateTransition translate = new TranslateTransition();
	Timer timer = new Timer();
	TimerTask timerTask;
	
	/**
	 * Function to start the game
	 */
	public void startSpellingGame() {
		
		wordsForSummary.clear();
		startGame.setDisable(true);
		repeatWordBtn.setDisable(false);
		translationBtn.setDisable(false);
		skipWordBtn.setDisable(false);
		macronBtn.setDisable(false);
		translate.setNode(frog);
		translate.setDuration(Duration.millis(1000));
		translate.setByX(80);
		
		
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
			
			word = words[i]; // maori words
			englishWord = englishWords[i];

			attempts = 0;
			
			displayNumLetters(word); // display how many letters are in the word
			
			// Start a new thread to say the word to spell to user
			speakWord = new SpellingThread();
			speakWord.start();
			
			// start timer
			startTimer();
			
			attempts++;
			
			// create a skip request when don't know is pressed
			skipWordBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					skipRequested = true;
					resume();
				}
			});
			
			// checkSpelling button will check the word and increase the score or ask user to spell again
			checkSpelling.setOnAction(new EventHandler<ActionEvent>() {

				@Override 
				public void handle(ActionEvent e) {

					String wordEntered = textField.getText().trim();

					// conditional checks to increase user score
					if (word.equalsIgnoreCase(wordEntered) && attempts == 1) {
						wordsForSummary.add(word + "#Correct");
						score++;
						translate.play();
						textField.clear();
						wordCount++;
						Sound.playSound("./correctSound.mp3");
						resume(); // resume function after check spelling button has been pressed
					} else if (wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						wordsForSummary.add(word + "#Correct");
						score++;
						translate.play();
						textField.clear();
						wordCount++;
						Sound.playSound("./correctSound.mp3");
						resume(); // resume function after check spelling button has been pressed
					} else if(!wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						if (wordCount != words.length) {
							bashCommand("echo You can do it! | festival --tts"); // encouraging message for user
						}
						wordsForSummary.add(word + "#Incorrect");
						textField.clear();
						wordCount++;
						Sound.playSound("./incorrectSound.mp3");
						resume(); // resume function after check spelling button has been pressed
					} else if (!wordEntered.equalsIgnoreCase(word)){
						spellingQuestion(word, 0, 1, 5, 1); // call the function again to ask user to spell word again 
						// display to the user the appropriate hint
						secondLetterHint(word);
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
				pauseTimer(); // when skip is requested then pause and delete the old timer
				if (wordCount != words.length) {
					bashCommand("echo You can do it! | festival --tts");
				}
				wordsForSummary.add(word + "#Incorrect");
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
		
		
	}
	
	/**
	 * Function allowing user to repeat a word 
	 */
	public void wordRepeat(ActionEvent event) {
		RepeatThread repeat = new RepeatThread();
		repeat.start();
	}
	
	/**
	 * Function to translate word from maori to english
	 */
	public void translate(ActionEvent event) {
		translationHint.setText("Hint: the english translation is " + englishWord);
	}
	
	/*
	 * Function allowing the user to enter a macron
	 */
	public void insertMacron(ActionEvent event) {
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
	
	public void rewardScreen(ActionEvent event) throws IOException { 
			root = FXMLLoader.load(getClass().getResource("RewardScreen.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			//RewardScreen rewardScreen = new RewardScreen();
			//rewardScreen.setScore();
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
				secondsPassed++;

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						if(totalSeconds == 0) {
							timeLabel.setText("Time's Up");
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
	
			
	

	
		

}
