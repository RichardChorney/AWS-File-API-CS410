package edu.pdx.agileteam7;

import java.util.Timer;
import java.util.TimerTask;

public class Counter {
    Timer timer;

    public Counter(int seconds) {
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds * 1000);
    }

    public int StopClock() {
        timer.cancel();
        return 1;
    }

    class RemindTask extends TimerTask {
        public void run() {
            System.out.println("Idle Timeout: Logging Out...");
            System.exit(1);

        }
    }
}