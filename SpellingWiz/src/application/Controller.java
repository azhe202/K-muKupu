package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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
	private Button checkSpelling;
	@FXML
	private ChoiceBox<String> wordpoolSelection;
	@FXML
	private Label hintLabel;
	@FXML
	private Label scoreLabel;
	@FXML
	private Label letterHintLabel;
	@FXML
	private Button repeatWordBtn;
	@FXML
	private Slider voiceSpeedSlider;
	
	private String[] wordpool = {"babies", "colours", "compassPoints", "daysOfTheWeek1", "daysOfTheWeek2", "engineering", "feelings", "monthsOfTheYear1", "monthsOfTheYear2", "software", "uniLife", "weather", "work"};
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private int attempts;
	private int wordCount;
	private int score;
	private double voiceSpeed;
	private String word;
	private String englishWord;
	private final Object PAUSE_KEY = new Object();
	
	private static String binPath = (new File(System.getProperty("java.class.path"))).getAbsolutePath().split(File.pathSeparator)[0];
	private static String path = new File(binPath).getParentFile().getAbsolutePath();
	private static File schemeFile = new File(path + "/.speak.scm");
	
	/*
	 * Function to change scenes back to the main menu when button is pressed 
	 */
	public void returnToMainMenu(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml"));
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
	public void startSpellingGame(ActionEvent event) {
		// make the files to store the mastered, faulted and failed words 
		bashCommand("touch .MASTERED_WORDS");
		bashCommand("touch .FAULTED_WORDS");
		bashCommand("touch .FAILED_WORDS");
		bashCommand("touch .QUIZZED_WORDS");
		bashCommand("touch .FOR_REVIEW");
		
		//disables start button and enables submit button
		startGame.setDisable(true);
		checkSpelling.setDisable(false);

		// get three random words from the appropriate file
		String command = "sort -u words/" + wordpoolSelection.getValue() + " | shuf -n 5";
		String[] words = returnWordList(command);
		
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
			
			word = words[i];
			englishWord = englishWords[i];

			attempts = 0;
			prompt.setLayoutX(255);
			
			// remove existing hints
			hintLabel.setText("");
			letterHintLabel.setText("");
			
			voiceSpeed = voiceSpeedSlider.getValue();

			// call method to say the word
			spellingQuestion(word, wordCount, attempts, 5, voiceSpeed);
			attempts++;

			// checkSpelling button will check the word and append the word to the correct file
			checkSpelling.setOnAction( new EventHandler<ActionEvent>() {
				
				@Override 
				public void handle(ActionEvent e) {

					String wordEntered = textField.getText().trim();

					// conditional checks to append word entered to the correct files 
					if (word.equalsIgnoreCase(wordEntered) && attempts == 1) {
						bashCommand("echo Correct | festival --tts");
						bashCommand("echo "+word+" >> .MASTERED_WORDS");
						textField.clear();
						wordCount++;
						score++;
						resume(); // resume function after check spelling button has been pressed
					} else if (wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						bashCommand("echo Correct | festival --tts");
						bashCommand("echo "+word+" >> .FAULTED_WORDS");	
						textField.clear();
						wordCount++;
						score++;
						resume(); // resume function after check spelling button has been pressed
					} else if(!wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						bashCommand("echo Incorrect | festival --tts");
						bashCommand("echo "+word+" >> .FAILED_WORDS");
						bashCommand("grep -qxFs "+word+" .FOR_REVIEW || echo "+word+" >> .FOR_REVIEW");
						textField.clear();
						wordCount++;
						resume(); // resume function after check spelling button has been pressed
					} else if (!wordEntered.equalsIgnoreCase(word)){
						voiceSpeed = voiceSpeedSlider.getValue();
						spellingQuestion(word, 0, 1, 5, voiceSpeed); // call the function again to ask user to spell word again 
						letterHintLabel.setText("The second letter of the word is '"+word.charAt(1)+"'");
						textField.clear();
						attempts++;
					}
					// update score
					scoreLabel.setText(score+"/"+(wordCount-1));
				}

			});

			// temporarily pause the method and wait for resume to be called
			pause();

		}	
		
		// prompt the user the quiz has finished 
		prompt.setLayoutX(50);
		prompt.setText("Spelling Quiz Completed! Click start quiz to play again or return to main menu to exit quiz");
		
		//disables submit and enables start button
		startGame.setDisable(false);
		checkSpelling.setDisable(true);
		
		// remove existing hints
		hintLabel.setText("");
		letterHintLabel.setText("");

	}
	
	/*
	 * Function is used the pause a method
	 */
	private void pause() {
		Platform.enterNestedEventLoop(PAUSE_KEY);
	}

	/*
	 * function is used to resume a method
	 */
	private void resume() {
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
			scheme.write("(voice_akl_mi_pk06_cg)");
			scheme.write("(Parameter.set 'Duration_Stretch " + speed + ")" + System.getProperty("line.separator"));
			scheme.write("(SayText \"" + word + "\")" + System.getProperty("line.separator"));
			scheme.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Function to repeat the word on users request
	 */
	public void repeatWord(ActionEvent event) {
		// voice speed is changed accordingly
		voiceSpeed = voiceSpeedSlider.getValue();
		createSchemeFile(word, voiceSpeed);
		bashCommand("festival -b " + schemeFile);
	}
	
	public void giveTranslation(ActionEvent event) {
		hintLabel.setText("The english translation is: " + englishWord);
	}

	/*
	 * Helper method for newSpellingQuiz and reviewMistakes
	 */
	public void spellingQuestion(String word, int wordCount, int attempts, int numWords, double speed) {
		// display the appropriate message according to the number of attempts for a word 
		if (attempts == 0) {
			prompt.setText("Spell word " + wordCount + " of " + numWords);
			bashCommand("grep -qxFs "+word+" .QUIZZED_WORDS || echo "+word+" >> .QUIZZED_WORDS"); // append the quizzed word to the QUIZZED_WORDS file 
			bashCommand("echo Please spell | festival --tts");
			createSchemeFile(word, speed); // file to speak the maori word
			bashCommand("festival -b " + schemeFile);	
		} else if (attempts == 1) {
			bashCommand("echo Incorrect, try once more. | festival --tts");
			createSchemeFile(word, speed); // file to speak the maori word
			bashCommand("festival -b " + schemeFile);
			bashCommand("festival -b " + schemeFile);
			prompt.setLayoutX(235);
			prompt.setText("Incorrect, try once more");
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
	 * Function to count the number of lines in a file
	 */
	public int countNumLines(String fileName) {

		int lines = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			while (reader.readLine() != null) lines++;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;

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
		wordpoolSelection.getItems().addAll(wordpool);
		wordpoolSelection.setValue("babies");
		
	}
	
}
