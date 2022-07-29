import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.Request;

import java.io.IOException;

public class InputThread extends Thread {
    private final RequestQueue waitQueue;

    public InputThread(RequestQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        ElevatorInput input = new ElevatorInput(System.in);
        while (true) {
            Request newRequest = input.nextRequest();
            if (newRequest == null) {
                waitQueue.setEnd(true);
                //System.out.println("InputThread End");
                break; // Thread End
            }
            waitQueue.addRequest(newRequest);
        }
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
