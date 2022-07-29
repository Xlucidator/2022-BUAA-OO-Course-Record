import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.Request;

public class Input extends Thread {
    private Requestqueue waitQueue;
    private Requestqueue passengers;

    public Input(Requestqueue wait, Requestqueue passengers) {
        this.waitQueue = wait;
        this.passengers = passengers;
    }

    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                waitQueue.setEnd(true);
                break;
            } else {
                waitQueue.addRequest(request);
            }
        }
    }

}
