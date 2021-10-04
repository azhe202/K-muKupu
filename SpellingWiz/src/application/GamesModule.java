package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
	
	private int wordCount;
	private int score;
	private int attempts;
	private String word;
	private String englishWord;
	private Boolean skipRequested = false;
	
	/**
	 * Function to start the game
	 */
	public void startSpellingGame() {
		
		startGame.setDisable(true);
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
				
		for (int i=0; i<words.length; i++) {
			textField.clear();
			
			word = words[i]; // maori words
			englishWord = englishWords[i];

			attempts = 0;
			
			displayNumLetters(word); // display how many letters are in the word
			
			// call method to say the word
			spellingQuestion(word, wordCount, attempts, 5, 1);
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
			checkSpelling.setOnAction( new EventHandler<ActionEvent>() {

				@Override 
				public void handle(ActionEvent e) {

					String wordEntered = textField.getText().trim();

					// conditional checks to increase user score
					if (word.equalsIgnoreCase(wordEntered) && attempts == 1) {
						bashCommand("echo Correct | festival --tts");
						textField.clear();
						wordCount++;
						resume(); // resume function after check spelling button has been pressed
					} else if (wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						bashCommand("echo Correct | festival --tts");
						textField.clear();
						wordCount++;
						resume(); // resume function after check spelling button has been pressed
					} else if(!wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						bashCommand("echo Incorrect. | festival --tts");
						if (wordCount != words.length) {
							bashCommand("echo You can do it! | festival --tts"); // encouraging message for user
						}
						textField.clear();
						wordCount++;
						resume(); // resume function after check spelling button has been pressed
					} else if (!wordEntered.equalsIgnoreCase(word)){
						spellingQuestion(word, 0, 1, 5, 1); // call the function again to ask user to spell word again 
						// display to the user the appropriate hint
						secondLetterHint(word);
						attempts++;

					}
				}

		});

			// temporarily pause the method and wait for resume to be called
			pause();

			// skips the current word as per user request
			if (skipRequested) {
				if (wordCount != words.length) {
					bashCommand("echo You can do it! | festival --tts");
				}
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
	
	
	/*
	 * Function allowing the user to enter a macron
	 */
	public void addMacron(ActionEvent event) {
		
		// get the current word and macronize the last letter
		String noMacronWord = textField.getText();
		if (!noMacronWord.isEmpty()) {
			String preMacron = noMacronWord.substring(0,noMacronWord.length()-1);
			String noMacronChar	= noMacronWord.substring(noMacronWord.length()-1);
			String macronChar;
			// add the appropriate macron
			switch (noMacronChar) {
			case "a":
				macronChar = "ā";
				break;
			case "e":
				macronChar = "ē";
				break;
			case "i":
				macronChar = "ī";
				break;
			case "o":
				macronChar = "ō";
				break;
			case "u":
				macronChar = "ū";
				break;
			case "A":
				macronChar = "Ā";
				break;
			case "E":
				macronChar = "Ē";
				break;
			case "I":
				macronChar = "Ī";
				break;
			case "O":
				macronChar = "Ō";
				break;
			case "U":
				macronChar = "Ū";
				break;
			default:
				macronChar = noMacronChar;
				break;
			}
			textField.clear();
			textField.setText(preMacron + macronChar); // display the word with the macron to user
		}

	}
	
	
	
}
