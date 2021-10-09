package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PractiseModule extends Controller {
	@FXML
	private ImageView helpBtn;
	@FXML
	private ImageView helpWindow;
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

	public static int wordCount;
	public static int score;
	private int attempts;
	public static double voiceSpeed = 1;
	public static String word;
	private String englishWord;
	private Boolean skipRequested = false;
	private String wordList;
	private String[] wordPoolFileNames = {"animals", "colours", "compassPoints", "daysOfTheWeek", "daysOfTheWeekLoanWords", 
			"feelings", "food", "monthsOfTheYear", "monthsOfTheYearLoanWords"};

	boolean helpOpen = false;
	boolean gameInPlay;

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
	public void startPractiseGame() {
		startGame.setDisable(true);
		repeatWordBtn.setDisable(false);
		translationBtn.setDisable(false);
		skipWordBtn.setDisable(false);
		macronBtn.setDisable(false);

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
		
			
			System.out.println(word + englishWord);

			// starting word count and score
			wordCount = 1;
			score = 0;

			textField.clear();

			attempts = 0;

			displayNumLetters(word); // display how many letters are in the word

			// Start a new thread to say the word to spell to user
			PractiseThread practiseThread = new PractiseThread();
			practiseThread.start();

			attempts++;

			// create a skip request when don't know is pressed
			skipWordBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					skipRequested = true;
					resume();
				}
			});


			// checkSpelling button will check the word or ask user to spell again
			checkSpelling.setOnAction(new EventHandler<ActionEvent>() {

				@Override 
				public void handle(ActionEvent e) {

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
						spellingQuestion(word, 0, 1, 5, 1); // call the function again to ask user to spell word again 
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

	}
	
	/**
	 * Function allowing user to repeat a word 
	 */
	public void wordRepeat(ActionEvent event) {
		repeatWord(voiceSpeed, word);
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
	public void insertMacron(MouseEvent event) {
		addMacron(event);
	}
}
