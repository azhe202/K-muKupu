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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The GamesModule class controls all the functionality within the Games Module
 * @author Group 22
 *
 */
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
	private ImageView submitBtn;
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

	public static float score;
	private int totalSeconds = 31;
	public static ArrayList<String> wordsForSummary = new ArrayList<>();
	public static int attempts;
	public static double voiceSpeed;
	public static String word;
	private String englishWord;
	private Boolean skipRequested = false;
	String langExt = Menu.langExt;
	TranslateTransition translate = new TranslateTransition();
	TranslateTransition translateStar = new TranslateTransition();
	Timer timer = new Timer();
	TimerTask timerTask;

	/**
	 * Function sets up the language selected by the user
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
		translate.setByX(122);
		
		translateStar.setNode(star);
		translateStar.setDuration(Duration.millis(1000));
		translateStar.setByY(-170);
		translateStar.setAutoReverse(true);
		translateStar.setCycleCount(2);

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

		// starting score
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
			speakWord.setDaemon(true);
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

			// submit button will check the word and increase the score or ask user to spell again
			submitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override 
				public void handle(MouseEvent e) {
					assess();
				}
			});
			
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
				pauseTimer(); // when skip is requested then pause and delete the old timer
				Sound.playSound("sounds/incorrectSound.mp3");
				wordsForSummary.add(word + "#Incorrect");
				translate.play();
				skipRequested = false;
			}
			
			// remove existing hints
			translationHint.setText("");

		}
		
		enterRewardsScreen(event);
	}
		
	/**
	 * Function will be called upon the submit button being pressed and will 
	 * assess the correctness of the word typed by the user
	 */
	public void assess() {
		String wordEntered = textField.getText().trim();

		// conditional checks to increase user score
		if (word.equalsIgnoreCase(wordEntered) && attempts == 1) {
			wordsForSummary.add(word + "#Correct");
			pauseTimer(); 
			playScoreAnimation();
			translate.play();
			correctSpelling(textField);
			resume(); // resume function after check spelling button has been pressed
		} else if (wordEntered.equalsIgnoreCase(word) && attempts == 2) {
			wordsForSummary.add(word + "#Correct");
			pauseTimer();
			playScoreAnimation();
			translate.play();
			correctSpelling(textField);
			resume(); // resume function after check spelling button has been pressed
		} else if(!wordEntered.equalsIgnoreCase(word) && attempts == 2) {
			pauseTimer();
			wordsForSummary.add(word + "#Incorrect");
			translate.play();
			incorrectSpelling(textField);
			resume(); // resume function after check spelling button has been pressed
		} else if (!wordEntered.equalsIgnoreCase(word)){
			incorrectSpelling(textField);
			voiceSpeed = voiceSpeedSlider.getValue();
			SpellingThreadSecondAttempt repeat = new SpellingThreadSecondAttempt(); // call the function again to ask user to spell word again
			repeat.setDaemon(true);
			repeat.start(); // call the function again to ask user to spell word again 
			attempts++;	
		}
	
	}
	
	/**
	 * Function will play an animation which shows the user their score for the given word
	 */
	public void playScoreAnimation() {
		if (noMoreTimeLabel.getText().equals("Time's Up") || noMoreTimeLabel.getText().equals("Kaore o wa i toe")) {
			score+= 0.5;
			star.setImage(new Image("./images/halfStar.png"));
			translateStar.play();
		} else {
			score++;
			star.setImage(new Image("./images/filledStar.png"));
			translateStar.play();
		}
	}
	
	/**
	 * Function to translate word from Māori to english
	 */
	public void translate(MouseEvent event) {
		if (Menu.isEnglish) {
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

	/**
	 * Function allowing the user to enter a macron
	 */
	public void insertMacron(MouseEvent event) {
		addMacron(event);
	}
	
	/**
	 * Voice speed button functionality
	 */
	public void selectSpeed(MouseEvent event) {
		super.selectSpeed(event, voiceSpeedSlider, speedWindow);
	}
	

	/* *
	 * Help button functionality
	 */
	public void selectHelp(MouseEvent event) {
		super.selectHelp(event, helpWindow, langExt);
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
						
							if(Menu.langExt == "m") {
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
	 * Function to allow user to return to category selection is back arrow is clicked
	 * @param event
	 */
	public void back(MouseEvent event) { 
		FXMLLoader loader = changeScene("./FXML/Menu.fxml", event);
		// access the controller and call function to set up the language
		Menu controller = loader.getController();
		controller.setUpLang(event);
	}
	
	/**
	 * Function is called to enter the Rewards Screen
	 * @param event
	 */
	public void enterRewardsScreen(MouseEvent event) {
		FXMLLoader loader = changeScene("./FXML/RewardScreen.fxml", event);
	}
	
	/**
	 * Helper function to change scenes from current scene
	 * @param nextScene
	 * @param event
	 * @return
	 */
	public FXMLLoader changeScene(String nextScene, MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(nextScene));
			root = loader.load();
			scene = new Scene(root);

			// show GUI to user 
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();

			return loader;

		} catch (IOException e) {
			e.printStackTrace();
			return new FXMLLoader();

		}

	}

}
