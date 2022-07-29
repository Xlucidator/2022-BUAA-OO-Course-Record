import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class InputThread extends Thread {
    private final RequestQueue waitQueue;

    public InputThread(RequestQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        ElevatorInput input = new ElevatorInput(System.in);
        while (true) {
            PersonRequest newRequest = input.nextPersonRequest();
            if (newRequest == null) {
                waitQueue.setEnd(true);
                //System.out.println("InputThread End");
                return; // Thread End
            }
            waitQueue.addRequest(newRequest);
        }
    }
}
