package application;

public class SpellingThread2 extends Thread{
	@Override
	public void run() {
		Controller speak = new Controller();
		String word = GamesModule.word;
		int wordCount = GamesModule.wordCount;
		double voiceSpeed = GamesModule.voiceSpeed;
		
		speak.spellingQuestion(word, wordCount, 1, 5, voiceSpeed);
	}
}
