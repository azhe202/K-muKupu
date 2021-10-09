package application;

public class PractiseThread extends Thread {
	@Override
	public void run() {
		Controller speak = new Controller();
		String word = PractiseModule.word;
		int wordCount = PractiseModule.wordCount;
		double speed = PractiseModule.voiceSpeed;
		
		speak.spellingQuestion(word, wordCount, 0, 5, speed);
	}
}
