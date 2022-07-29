import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Schedule.getInstance().initial();
        InputThread inputThread = new InputThread();
        inputThread.start();
    }
}
