package application;

public class PractiseThread extends Thread {
	@Override
	public void run() {
		Controller speak = new Controller();
		String word = PractiseModule.word;
		double speed = PractiseModule.voiceSpeed;
		
		speak.spellingQuestion(word, 0, speed);
	}
}
