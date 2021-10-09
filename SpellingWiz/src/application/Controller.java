package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Node;

/*
 * The Controller class includes methods which involves changing scenes 
 * and the functions used within the scenes themselves as well as additional
 * helper function
 */

public class Controller implements Initializable{

	@FXML
	private Label prompt;
	@FXML
	private TextField textField;
	@FXML
	private Button startGame;
	@FXML
	private ImageView checkSpelling;
	@FXML
	private ChoiceBox<String> wordpoolSelection;
	@FXML
	private Label hintLabel;
	@FXML
	private Label scoreLabel;
	@FXML
	private Label letterHintLabel;
	@FXML
	private ImageView repeatWordBtn;
	@FXML
	private ImageView skipWordBtn;
	@FXML
	private ImageView translationBtn;
	@FXML
	private Slider voiceSpeedSlider;
	@FXML
	private GridPane hintGrid;
	@FXML 
	private ImageView macronBtn;
	@FXML
	private Label wordLength;
	
	private String[] wordpool = {"Babies", "Colours", "Compass points", "Days of the week", "Days of the week loan words", "Engineering", "Feelings", "Months of the year", "Months of the year loan words", "Software", "Uni life", "Weather", "Work"};
	private String[] wordpoolFileNames = {"babies", "colours", "compassPoints", "daysOfTheWeek", "daysOfTheWeekLoanWords", "engineering", "feelings", "monthsOfTheYear", "monthsOfTheYearLoanWords", "software", "uniLife", "weather", "work"};
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private int attempts;
	private int wordCount;
	private int score;
	private int nextGridSpace = 0;
	private double voiceSpeed;
	private String word;
	private String englishWord;
	private Boolean skipRequested = false;
	private final Object PAUSE_KEY = new Object();
	
	private static File schemeFile = getPath();
	
	/*
	 * Function to change scenes back to the main menu when button is pressed 
	 */
	public void returnToMainMenu(ActionEvent event) {
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

	/*
	 * Function to start a new spelling game 
	 */
	public void startSpellingGame(String wordpoolSelected) {
		wordpoolSelection.getItems().addAll(wordpool);
		wordpoolSelection.setValue(wordpoolSelected);
		
		//disables game related buttons and enables submit button
		startGame.setDisable(true);
		checkSpelling.setDisable(false);
		repeatWordBtn.setDisable(false);
		translationBtn.setDisable(false);
		skipWordBtn.setDisable(false);
		macronBtn.setDisable(false);

		// get the selected word list
		int wordIndex;
		for (wordIndex=0; wordIndex<wordpool.length; wordIndex++) {
			if (wordpoolSelection.getValue() == wordpool[wordIndex]) {
				break;
			}
		}
		
		// get five random words from the appropriate file
		String command = "sort -u words/" + wordpoolFileNames[wordIndex] + " | shuf -n 5";
		String[] words = returnWordList(command);

		// extract the maori and english words from the selected words
		String[] englishWords = new String[5];
		for (int i=0; i<words.length; i++) {
			String temp = words[i];
			String tempArray[] = temp.split("#");
			words[i] = tempArray[0].trim();
			englishWords[i] = tempArray[1];
		}
		
		// show current score text
		scoreLabel.setText("0/0");

		// starting word count and score
		wordCount = 1;
		score = 0;

		// loop through the words the user needs to spell and mark the words accordingly
		for (int i=0; i<words.length; i++) {
			textField.clear();
			
			word = words[i]; // maori words
			englishWord = englishWords[i];

			attempts = 0;
			prompt.setLayoutX(255);
			
			// remove existing hints
			nextGridSpace = 0;
			hintGrid.getChildren().clear();
			
			displayNumLetters(word);
			
			voiceSpeed = voiceSpeedSlider.getValue();

			// call method to say the word
			spellingQuestion(word, wordCount, attempts, 5, voiceSpeed);
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
			checkSpelling.setOnMouseClicked( new EventHandler<MouseEvent>() {
				
				@Override 
				public void handle(MouseEvent e) {

					String wordEntered = textField.getText().trim();

					// conditional checks to increase user score
					if (word.equalsIgnoreCase(wordEntered) && attempts == 1) {
						bashCommand("echo Correct | festival --tts");
						textField.clear();
						wordCount++;
						score++;
						resume(); // resume function after check spelling button has been pressed
					} else if (wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						bashCommand("echo Correct | festival --tts");
						textField.clear();
						wordCount++;
						score++;
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
						voiceSpeed = voiceSpeedSlider.getValue();
						spellingQuestion(word, 0, 1, 5, voiceSpeed); // call the function again to ask user to spell word again 
						// display to the user the appropriate hint
						secondLetterHint(word);
						Label hintLabel = new Label("The second letter of the word is '"+word.charAt(1)+"'");
						hintLabel.setFont(new Font(15));
						hintGrid.add(hintLabel, 0, nextGridSpace);
						nextGridSpace++;
						textField.clear();
						attempts++;
					}
					// update score
					scoreLabel.setText(score+"/"+(wordCount-1));
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
				scoreLabel.setText(score+"/"+(wordCount-1)); // update the score
				continue;
			}

		}	
		
		// prompt the user the quiz has finished 
		prompt.setText("Spelling Quiz Completed!");
		
		// alert shows the user their final score and appropriate message
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game Complete");
		alert.setHeaderText(null);
		// select appropriate message to display
		if (score <=2) {
			alert.setContentText("You spelt " + score + "/5 words correcly. Maybe spend some more time practicing the \"" + wordpoolSelection.getValue() + "\" wordpool.");
		} else if (score <=4) {
			alert.setContentText("You spelt " + score + "/5 words correctly. Good job.");
		} else {
			alert.setContentText("Good job, you spelt " + score + "/5 words correctly. You have mastered the \"" + wordpoolSelection.getValue() + "\" wordpool.");
		}
		alert.showAndWait();
		
		//disables game related buttons and enables start button
		startGame.setDisable(false);
		checkSpelling.setDisable(true);
		repeatWordBtn.setDisable(true);
		translationBtn.setDisable(true);
		skipWordBtn.setDisable(true);
		macronBtn.setDisable(true);
		
		
		// remove existing hints
		hintGrid.getChildren().clear();

	}

	/**
	 * Function displays to the user how many letters are in the word
	 * @param word
	 */
	public void displayNumLetters(String word) {
		int numLetters = word.length();
		String textToDisplay = "_";
		
		for(int i=1; i<numLetters; i++) {
			textToDisplay = textToDisplay + " " + "_";
		}
		
		wordLength.setText(textToDisplay);
	}
	
	/**
	 * Function displays to the user how many letters are in the word along with the letter 
	 * in the second position
	 * @param word
	 */
	public void secondLetterHint(String word) {
		int numLetters = word.length();
		char secondLetter = word.charAt(1);
		String textToDisplay = "_";
		textToDisplay = textToDisplay + " " + secondLetter;
		
		for(int i=2; i<numLetters; i++) {
			textToDisplay = textToDisplay + " " + "_";
		}
		
		wordLength.setText(textToDisplay);
	}
	
	/*
	 * Function is used the pause a method
	 */
	public void pause() {
		Platform.enterNestedEventLoop(PAUSE_KEY);
	}

	/*
	 * function is used to resume a method
	 */
	public void resume() {
		Platform.exitNestedEventLoop(PAUSE_KEY, null);
	}
	
	/*
	 * Function to adjust the speed of festival voice 
	 */
	private void createSchemeFile(String word, double speed) {
		//open a scheme file and write to it 
		BufferedWriter scheme = null;
		try {
			scheme = new BufferedWriter(new FileWriter(schemeFile));
			scheme.write("(voice_akl_mi_pk06_cg)"); // set up Māori voice
			scheme.write("(Parameter.set 'Duration_Stretch " + speed + ")");
			scheme.write("(SayText \"" + word + "\")");
			scheme.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* 
	 * Function to get the path for the scheme file
	 */
	private static File getPath() {
		Path path = Paths.get(".speak.scm"); 
		schemeFile = new File(path.toAbsolutePath().toString());
		return schemeFile;
	}
	
	/*
	 * Function to repeat the word on users request
	 */
	public void repeatWord(double voiceSpeed, String word) {
		// voice speed is changed accordingly when word is repeated
//		voiceSpeed = voiceSpeedSlider.getValue();
		createSchemeFile(word, voiceSpeed);
		bashCommand("festival -b " + schemeFile);
	}
	
	/*
	 * Function which provides the English translation of the given word
	 */
	public void giveTranslation(ActionEvent event) {
		Label hintLabel = new Label("The english translation is: " + englishWord);
		hintLabel.setFont(new Font(15));
		hintGrid.add(hintLabel, 0, nextGridSpace); // add the hint in the correct space
		nextGridSpace++;
	}
	
	/*
	 * Function allowing the user to enter a macron
	 */
	public void addMacron(MouseEvent event) {
		
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

	/*
	 * Helper function for newSpellingQuiz 
	 */
	public void spellingQuestion(String word, int wordCount, int attempts, int numWords, double speed) {
		// display the appropriate message according to the number of attempts for a word 
		if (attempts == 0) {
			bashCommand("echo Please spell | festival --tts");
			createSchemeFile(word, speed); // file to speak the maori word
			bashCommand("festival -b " + schemeFile);	
		} else if (attempts == 1) {
			bashCommand("echo Incorrect, try once more. | festival --tts");
			createSchemeFile(word, speed); // file to speak the maori word
			bashCommand("festival -b " + schemeFile);
			bashCommand("festival -b " + schemeFile);
		}
	}

	/*
	 * Function for bash commands without a variable to be returned
	 */
	public static void bashCommand(String command) {
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			int exitStatus = process.waitFor();

			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					System.out.println(line);
				}
			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
					System.err.println(line);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Function using bash command to return the word list as an array
	 */
	public String[] returnWordList(String command) {
		String[] word = new String[5];
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			int exitStatus = process.waitFor();
			int i = 0;
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					word[i] = line;
					i++;
				}
			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
					System.err.println(line);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return word;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// initialise the wordPool
		
	}

	
	
	
}
