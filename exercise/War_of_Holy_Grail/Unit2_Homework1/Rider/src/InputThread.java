import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

/*
    1. initialize
    2. add elevator
    3. add person request
    4. get personal request & running
    5. input end
    6. elevator stop
 */

public class InputThread extends Thread {
    private final Dispatch dispatch;
    
    public InputThread(Dispatch dispatch) {
        this.dispatch = dispatch;
    }
    
    @Override
    public void run() {
        try {
            executeInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void executeInput() throws IOException {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                dispatch.setEnd();
                break;
            } else {
                //System.out.println("InputThread get a request: " + request);
                dispatch.addRequest(request);
                //System.out.println("Input add a request over.");
            }
        }
        elevatorInput.close();
        //System.out.println("elevatorInput is closed.");
    }
}
