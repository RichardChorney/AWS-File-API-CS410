package edu.pdx.agileteam7;
import java.util.Timer;
import java.util.TimerTask;
public class Counter {
    Timer timer;

    public Counter(int seconds) {
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds * 1000);
    }

    public void StopClock() {
        timer.cancel();
    }

    class RemindTask extends TimerTask {
        public void run() {
            System.out.println("Idle time out");
            System.exit(1);

        }
    }
}
