import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) throws InterruptedException {
        TimableOutput.initStartTimestamp();
        PersonQueue[] waitQueues = new PersonQueue[15];
        for (int i = 0; i < 15; i++) {
            waitQueues[i] = new PersonQueue();
        }

        InputThread inputThread = new InputThread(waitQueues);
        inputThread.start();

        Factory.getFactory().initial(waitQueues);
    }
}
