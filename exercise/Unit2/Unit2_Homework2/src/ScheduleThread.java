import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.util.ArrayList;

public class ScheduleThread extends Thread {
    private final RequestQueue waitQueue;

    private final ArrayList<PersonRequestQueue> processQueues;
    private final ArrayList<ElevatorThreadQueue> elevatorQueues;

    public ScheduleThread(RequestQueue waitQueue, ArrayList<PersonRequestQueue> processQueues,
                          ArrayList<ElevatorThreadQueue> elevatorQueues) {
        this.waitQueue = waitQueue;
        this.processQueues = processQueues;
        this.elevatorQueues = elevatorQueues;
    }

    @Override
    public void run() {
        while (true) {
            if (waitQueue.isEmpty() && waitQueue.isEnd()) {
                processQueues.forEach(reqs -> reqs.setEnd(true));
                elevatorQueues.forEach(elvs -> elvs.setEnd(true));//
                //System.out.println("ScheduleThread End");
                return; // Thread End
            }
            Request request = waitQueue.getOneRequest();
            if (request == null) {
                continue;
            }

            if (request instanceof PersonRequest) {           // Distribute personRequest
                PersonRequest personRequest = (PersonRequest) request;
                if (personRequest.getFromBuilding() == personRequest.getToBuilding()) {
                    processQueues.get(personRequest.getFromBuilding() - 'A')
                            .addRequest(personRequest);
                } else if (personRequest.getFromFloor() == personRequest.getToFloor()) {
                    processQueues.get(personRequest.getFromFloor() + 4)
                            .addRequest(personRequest);
                }
            } else if (request instanceof ElevatorRequest) { // Distribute elevatorRequest
                ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                ElevatorThread ele = new ElevatorThread(elevatorRequest.getElevatorId(),
                                                        elevatorRequest.getType(),
                                                        elevatorRequest.getBuilding(),
                                                        elevatorRequest.getFloor());
                if (elevatorRequest.getType().equals("building")) {
                    elevatorQueues.get(elevatorRequest.getBuilding() - 'A').addElevator(ele);
                } else if (elevatorRequest.getType().equals("floor")) {
                    elevatorQueues.get(elevatorRequest.getFloor() + 4).addElevator(ele);
                }
            }

        }
    }
}
