import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputThread extends Thread {
    private final WaitQueue waitQueue;
    private ElevatorInput input;

    public InputThread(WaitQueue waitQueue, ElevatorInput input) {
        this.waitQueue = waitQueue;
        this.input = input;
    }

    @Override
    public void run() {
        while (true) {
            PersonRequest request = input.nextPersonRequest();
            synchronized (waitQueue) {
                if (request == null) {
                    waitQueue.close();
                    waitQueue.notifyAll();
                    break;
                } else {
                    waitQueue.addRequest(request);
                    waitQueue.notifyAll();
                }
            }
        }
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
