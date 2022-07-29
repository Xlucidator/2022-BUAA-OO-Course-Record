import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputThread extends Thread {
    private final RequestQueue waitQueue;
    private final ElevatorInput elevatorInput;

    public InputThread(RequestQueue waitQueue) {
        this.waitQueue = waitQueue;
        this.elevatorInput = new ElevatorInput(System.in);
    }

    @Override
    public void run() {
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                waitQueue.setEnd(true);
                //System.out.println("Input End");
                break;
            } else {
                waitQueue.putRequest(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
