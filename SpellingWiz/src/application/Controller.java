package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Node;

/*
 * The Controller class includes methods which involves changing scenes 
 * and the functions used within the scenes themselves as well as additional
 * helper function
 */

public class Controller {

	@FXML
	private Label prompt;
	@FXML
	private TextField textField;
	@FXML
	private Button startGame;
	@FXML
	private Button checkSpelling;
	@FXML
	private TextField textField2;
	@FXML
	private Button startReview;
	@FXML
	private TextArea promptReview;
	@FXML
	private Button checkSpellingReview;
	@FXML
	private Button clearStats;
	@FXML 
	private Label promptStats;
	@FXML 
	private Label promptView;
	@FXML 
	private GridPane gridPane;
	@FXML
	private Label[][] gridLabel;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private int attempts;
	private int wordCount;
	private final Object PAUSE_KEY = new Object();

	/*
	 * Function to change scenes to New Spelling Quiz when button is pressed
	 */
	public void newSpellingQuiz(ActionEvent event) { 
		try {
			root = FXMLLoader.load(getClass().getResource("NewSpellingQuiz.fxml"));
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
	 * Function to change scenes to Review Mistakes when button is pressed
	 */
	public void reviewMistakes(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("ReviewMistakes.fxml"));
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
	 * Function to change scenes to View Statistics when button is pressed 
	 */
	public void viewStatistics(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ViewStatistics.fxml"));
			root = loader.load();
			scene = new Scene(root);
			
			// access the controller and call function to view statistics
			Controller controller = loader.getController();
			controller.startViewStatistics();
			
			// show GUI to user 
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Function to change scenes to Clear Statistics when button is pressed
	 */
	public void clearStatistics(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("ClearStatistics.fxml"));
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

		// get three random words from popular file
		String[] words = returnWordList("shuf -n3 words/popular");

		// starting word count
		wordCount = 1;

		// loop through the words the user needs to spell and mark the words accordingly
		for (String word : words) {

			attempts = 0;
			prompt.setLayoutX(255);

			// call method to say the word
			spellingQuestion(word, wordCount, attempts, 3);
			attempts++;

			// checkSpelling button will check the word and append the word to the correct file
			checkSpelling.setOnAction( new EventHandler<ActionEvent>() {

				@Override 
				public void handle(ActionEvent e) {

					String wordEntered = textField.getText();

					// conditional checks to append word entered to the correct files 
					if (word.equalsIgnoreCase(wordEntered) && attempts == 1) {
						bashCommand("echo Correct | festival --tts");
						bashCommand("echo "+word+" >> .MASTERED_WORDS");
						wordCount++;
						resume(); // resume function after check spelling button has been pressed
					} else if (wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						bashCommand("echo Correct | festival --tts");
						bashCommand("echo "+word+" >> .FAULTED_WORDS");	
						wordCount++;
						resume(); // resume function after check spelling button has been pressed
					} else if(!wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						bashCommand("echo Incorrect | festival --tts");
						bashCommand("echo "+word+" >> .FAILED_WORDS");
						bashCommand("grep -qxFs "+word+" .FOR_REVIEW || echo "+word+" >> .FOR_REVIEW");
						wordCount++;
						resume(); // resume function after check spelling button has been pressed
					} else if (!wordEntered.equalsIgnoreCase(word)){
						spellingQuestion(word, 0, 1, 3); // call the function again to ask user to spell word again 
						attempts++;
					}
				}

			});

			// temporarily pause the method and wait for resume to be called
			pause();

		}	
		
		// prompt the user the quiz has finished 
		prompt.setLayoutX(50);
		prompt.setText("Spelling Quiz Completed! Click start quiz to play again or return to main menu to exit quiz");

	}

	/*
	 * Function to start review Mistakes 
	 */
	public void startReviewMistakes(ActionEvent event) {
		// get words from the for review file and assign them to failedWords 
		String[] failedWords = new String[3];
		int numWords = countNumLines(".FOR_REVIEW");
	
		if (numWords >= 3) {
			failedWords = new String[3];
			failedWords = returnWordList("shuf -n3 .FOR_REVIEW"); // return random three words from file 
		} else if (numWords < 3 && numWords > 0){
			failedWords = new String[numWords];
			for (int i = 0; i < numWords; i++) {
				int line = i + 1;
				failedWords[i] = returnWord("sed -n "+line+"p .FOR_REVIEW"); // get all the words from file 
			}
		} else if (numWords == 0){
			prompt.setLayoutX(250);
			prompt.setText("No words to review"); // and exit out of function
			return;
		}

		// starting word count 
		wordCount = 1;
		
		// loop through the words the user needs to spell and mark the words accordingly
		for (String word: failedWords) {

			attempts = 0;
			prompt.setLayoutX(255);

			// call method to say the word to user 
			spellingQuestion(word, wordCount, attempts, numWords);
			attempts++;

			// checkSpelling button will check the word and append the word to the correct file
			checkSpellingReview.setOnAction( new EventHandler<ActionEvent>() {

				@Override 
				public void handle(ActionEvent e) {

					String wordEntered = textField2.getText();

					if (word.equalsIgnoreCase(wordEntered) && attempts == 1) {
						bashCommand("echo Correct | festival --tts");
						bashCommand("sed -i /"+word+"/d .FOR_REVIEW"); 	// remove word from the for review list
						bashCommand("echo "+word+" >> .MASTERED_WORDS"); // append correctly spelled word to mastered
						wordCount++;
						resume(); // resume function after button is pressed
					} else if (wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						bashCommand("echo Correct | festival --tts");
						bashCommand("sed -i /"+word+"/d .FOR_REVIEW"); // remove the word from for review
						bashCommand("echo "+word+" >> .FAULTED_WORDS");	// append correctly spelled word to faulted
						wordCount++;
						resume(); // resume function after button is pressed
					} else if(!wordEntered.equalsIgnoreCase(word) && attempts == 2) {
						bashCommand("echo Incorrect | festival --tts");
						bashCommand("echo "+word+" >> .FAILED_WORDS"); // append word to failed
						wordCount++;
						resume(); // resume function after button is pressed
					} else if (!wordEntered.equalsIgnoreCase(word)){
						spellingQuestion(word, 0, 1, numWords); // call function again to ask user to spell word again 
						attempts++;
					}
				}

			});

			// temporarily pause the method and wait to resume 
			pause();

		}
		
		// prompt the user the review has finished 
		prompt.setLayoutX(45);
		prompt.setText("Review Completed! Click start review to review again or return to main menu to exit review");

	}

	/*
	 * Function to clear statistics 
	 */
	public void startClearStatistics(ActionEvent event) {
		
		// Clear the mastered words file 
		clearContents(".MASTERED_WORDS");
		// Clear the faulted words file 
		clearContents(".FAULTED_WORDS");
		// Clear the failed words file 
		clearContents(".FAILED_WORDS");
		// Clear the quizzed words file 
		clearContents(".QUIZZED_WORDS");
		// Clear the for review words file
		clearContents(".FOR_REVIEW");
		// set the prompt on the screen 
		promptStats.setLayoutX(255);
		promptStats.setText("Statistics Cleared!");

	}

	public void startViewStatistics() {
		// add the words to view into an array
		int numWords = countNumLines(".QUIZZED_WORDS");
		String[] quizzedWords = new String[numWords];
		for(int i = 0; i < numWords; i++) {
			int line = i + 1;
			quizzedWords[i] = returnWord("sed -n "+line+"p .QUIZZED_WORDS");
		}
	 
		// exit out of function and display user message 
		if (numWords == 0) {
			promptView.setLayoutX(250);
			promptView.setText("No statistics to view");
			return;
		}
		
		// add the titles for the grid
		Label mastered = new Label();
		mastered.setText("Mastered");
		gridPane.add(mastered, 1, 0);
		Label faulted = new Label();
		faulted.setText("Faulted");
		gridPane.add(faulted, 2, 0);
		Label failed = new Label();
		failed.setText("Failed");
		gridPane.add(failed, 3, 0);
		
		gridLabel = new Label[numWords][4]; // initialize 
		
		// assign the appropriate word with its statistics 
		for (int i = 0; i < quizzedWords.length; i++) {
				for (int j = 0; j < 4; j++) {
					gridLabel[i][j] = new Label();
				}
				// assign the statistics for the word
				int masteredTimes = bashCommandInt("grep -c "+quizzedWords[i]+" .MASTERED_WORDS");
				int faultedTimes = bashCommandInt("grep -c "+quizzedWords[i]+" .FAULTED_WORDS");
				int failedTimes = bashCommandInt("grep -c "+quizzedWords[i]+" .FAILED_WORDS");
				
				// add statistics to a label 
				gridLabel[i][0].setText(quizzedWords[i]);
				gridLabel[i][1].setText(Integer.toString(masteredTimes));
				gridLabel[i][2].setText(Integer.toString(faultedTimes));
				gridLabel[i][3].setText(Integer.toString(failedTimes));
				
				// add label to gridPane
				gridPane.add(gridLabel[i][0], 0, i+1);
				gridPane.add(gridLabel[i][1], 1, i+1);
				gridPane.add(gridLabel[i][2], 2, i+1);
				gridPane.add(gridLabel[i][3], 3, i+1);
		}
		
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
	 * Helper method for newSpellingQuiz and reviewMistakes
	 */
	public void spellingQuestion(String word, int wordCount, int attempts, int numWords) {
		// display the appropriate message according to the number of attempts for a word 
		if (attempts == 0) {
			prompt.setText("Spell word " + wordCount + " of " + numWords);
			bashCommand("grep -qxFs "+word+" .QUIZZED_WORDS || echo "+word+" >> .QUIZZED_WORDS"); // append the quizzed word to the QUIZZED_WORDS file 
			bashCommand("echo Please spell "+word+" | festival --tts");
		} else if (attempts == 1) {
			bashCommand("echo Incorrect, try once more. "+word+". "+word+" | festival --tts");
			prompt.setLayoutX(235);
			prompt.setText("Incorrect, try once more");
		}

	}
	
	/*
	 * Function to clear the contents of a file
	 */
	public void clearContents(String fileName) {
		File file = new File(fileName);
		if(file.exists()) {
			bashCommand("cat /dev/null > "+fileName+"");
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
	 * Function is used when an integer needs to be stored from bash command
	 */
	public int bashCommandInt(String command) {
		int num = 0;
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			int exitStatus = process.waitFor();

			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					num = Integer.parseInt(line);
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
		
		return num;
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
		String[] word = new String[3];
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
	 * Function is used when a string needs to be stored from the bash command
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


}
