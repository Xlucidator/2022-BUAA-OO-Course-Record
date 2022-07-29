import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;

public class InputThread extends Thread {
    private int requestNum;

    public InputThread() {
        this.requestNum = 0;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);

        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                for (int i = 0; i < requestNum; ++i) { //查收所有的任务
                    RequestCounter.getInstance().acquire();
                }
                Schedule.getInstance().setEndTag();
                break;
            } else {
                if (request instanceof  PersonRequest) {
                    MyRequest myRequest = new MyRequest(((PersonRequest) request).getPersonId(),
                            ((PersonRequest) request).getFromFloor(),
                            ((PersonRequest) request).getToFloor(),
                            ((PersonRequest) request).getFromBuilding(),
                            ((PersonRequest) request).getToBuilding());
                    Schedule.getInstance().addRequest(myRequest);
                    this.requestNum++;
                } else if (request instanceof ElevatorRequest) {
                    RequestQueue parallelQueue = new RequestQueue();
                    if (((ElevatorRequest) request).getType().equals("building")) {
                        ElevatorBuilding elevatorBuilding = new ElevatorBuilding(
                                parallelQueue,
                                (int)(((ElevatorRequest) request).getBuilding()) - 65,
                                ((ElevatorRequest) request).getElevatorId(),
                                ((ElevatorRequest) request).getCapacity(),
                                ((ElevatorRequest) request).getSpeed()
                        );
                        Schedule.getInstance().addBuilding(elevatorBuilding);
                    } else if (((ElevatorRequest) request).getType().equals("floor")) {
                        ElevatorFloor elevatorFloor = new ElevatorFloor(
                                parallelQueue,
                                ((ElevatorRequest) request).getFloor(),
                                ((ElevatorRequest) request).getElevatorId(),
                                ((ElevatorRequest) request).getCapacity(),
                                ((ElevatorRequest) request).getSpeed(),
                                ((ElevatorRequest) request).getSwitchInfo());
                        Schedule.getInstance().addFloor(elevatorFloor);
                    }
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}