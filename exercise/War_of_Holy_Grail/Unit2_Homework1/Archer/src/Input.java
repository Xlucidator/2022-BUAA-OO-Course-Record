import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class Input extends Thread {
    private Father[] fathers;
    private Elevator[] elevators;

    public Input(Father[] f, Elevator[] e) {
        fathers = f;
        elevators = e;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest r = elevatorInput.nextPersonRequest();
            if (r == null) {
                for (int i = 0; i <= 4; i++) {
                    elevators[i].block();
                    fathers[i].put(null);
                }
                break;
            } else {
                Request request = new Request(r.getFromFloor(), r.getToFloor(),
                        r.getFromBuilding(), r.getToBuilding(), r.getPersonId());
                fathers[r.getFromBuilding() - 'A'].put(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
