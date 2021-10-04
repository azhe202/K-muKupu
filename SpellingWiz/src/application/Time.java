package application;

import java.util.Timer;
import java.util.TimerTask;

public class Time {
	
	int secondsPassed = 0;
	
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		public void run() {
			secondsPassed++;
		}
	};
	
	public void start(javafx.scene.control.Label displayTimer) {
		timer.scheduleAtFixedRate(task, 1000, 1000);
		displayTimer.setText(String.valueOf(secondsPassed));
	}
}
