// import com.oocourse.TimableOutput;

import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputThread implements Runnable {
    private final RequestQueue waitQueue;

    InputThread(RequestQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        // TimableOutput.println("Input Thread Start!");
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        // TimableOutput.println(elevatorInput);
        while (true) {
            // TimableOutput.println(elevatorInput);
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                synchronized (waitQueue) {
                    waitQueue.setEnd(true);
                }
                // TimableOutput.println(String.format("Input End"));
                // System.out.println("Input End");
                break;
            } else {
                // a new valid request
                // System.out.println(request);
                // TimableOutput.println("waitQueue get a request:" + request.toString());
                synchronized (waitQueue) {
                    waitQueue.addPersonRequest(request);
                }
            }
        }
        try {
            // TimableOutput.println("Input Thread Closed!");
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
