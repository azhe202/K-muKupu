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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Node;

/*
 * The Controller class includes methods which involves changing scenes 
 * and the functions used within the scenes themselves as well as additional
 * helper function
 */

public class Controller implements Initializable{

	@FXML
	private TextField textField;
	@FXML
	private GridPane hintGrid;
	@FXML
	protected Label wordLength;
	
	
	private Stage stage;
	private Scene scene;
	private Parent root;
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

	/**
	 * Function displays to the user how many letters are in the word
	 * @param word
	 */
	public void displayNumLetters(String word) {
		int numLetters = word.length();
		String textToDisplay = "";
		
		for(int i=0; i<numLetters; i++) {
			textToDisplay = textToDisplay + "_ ";
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
			if (word.contains("-")) {
				word = word.replaceAll("-", " ");
			}
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
		createSchemeFile(word, voiceSpeed);
		bashCommand("festival -b " + schemeFile);
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
			textField.positionCaret(textField.getText().length()); // reposition cursor at end of word
		}

	}

	/*
	 * Helper function for newSpellingQuiz 
	 */
	public void spellingQuestion(String word, int attempts, int numWords, double speed) {
		// display the appropriate message according to the number of attempts for a word 
		if (attempts == 0) {
			createSchemeFile(word, speed); // file to speak the maori word
			bashCommand("festival -b " + schemeFile);	
		} else if (attempts == 1) {
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
	
	/*
	 * Function using bash command to return the word 
	 */
	public String returnWord(String command) {
		String word = null;
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			int exitStatus = process.waitFor();
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					word = line;
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

	/**
	 * Function is called when a word is spelled correctly
	 * @param textField
	 */
	public void correctSpelling(TextField textField) {
		textField.clear();
		Sound.playSound("./sounds/correctSound.mp3");
	}
	
	/**
	 * Function is called when a word is spelled incorrectly
	 * @param textField
	 */
	public void incorrectSpelling(TextField textField) {
		textField.clear();
		Sound.playSound("./sounds/incorrectSound.mp3");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}	
}
