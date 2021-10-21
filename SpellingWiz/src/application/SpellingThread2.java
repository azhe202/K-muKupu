package application;

public class SpellingThread2 extends Thread{
	@Override
	public void run() {
		Controller speak = new Controller();
		String word = GamesModule.word;
		double voiceSpeed = GamesModule.voiceSpeed;
		
		speak.spellingQuestion(word, 1, voiceSpeed);
	}
}
