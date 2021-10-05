package application;

public class RepeatThread extends Thread {
	@Override 
	public void run() {
		Controller repeat = new Controller();
		double voiceSpeed = GamesModule.voiceSpeed;
		String word = GamesModule.word;
		
		repeat.repeatWord(1, word);
	}
}

