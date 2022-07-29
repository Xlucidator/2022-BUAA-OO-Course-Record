import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.ArrayList;
import java.util.HashSet;

public class ScheduleThread extends Thread {
    private static final ScheduleThread SCHEDULER = new ScheduleThread();

    private RequestQueue waitQueue;
    private ArrayList<PersonRequestQueue> processQueues;
    private ArrayList<ElevatorThreadQueue> elevatorQueues;
    private final ArrayList<HashSet<Integer>> accessMap = new ArrayList<>();

    public void initial(RequestQueue waitQueue, ArrayList<PersonRequestQueue> processQueues,
                          ArrayList<ElevatorThreadQueue> elevatorQueues) {
        this.waitQueue = waitQueue;
        this.processQueues = processQueues;
        this.elevatorQueues = elevatorQueues;
        for (int i = 0; i < 11; ++i) {
            accessMap.add(new HashSet<>());
        }
        accessMap.get(1).add(31);
    }

    public static ScheduleThread getInstance() {
        return SCHEDULER;
    }

    public void addRequest(PersonRequest personRequest) {
        waitQueue.addRequest(personRequest);
    }

    @Override
    public void run() {
        while (true) {
            //System.out.println("New fetch turn!");
            if (waitQueue.isEmpty() && waitQueue.isEnd() && TransferCounter.getInstance().isEnd()) {
                processQueues.forEach(reqs -> reqs.setEnd(true));
                elevatorQueues.forEach(elvs -> elvs.setEnd(true));
                //System.out.println("ScheduleThread End");
                return; // Thread End
            }
            Request request = waitQueue.getOneRequest();
            if (request == null) {
                continue;
            }

            if (request instanceof TransPersonRequest) {        // Detect TransRequest
                distributeTransRequest((TransPersonRequest) request);
            } else if (request instanceof PersonRequest) {      // Detect personRequest
                distributePersonRequest((PersonRequest) request);
            } else if (request instanceof ElevatorRequest) {    // Detect elevatorRequest
                distributeElevatorRequest((ElevatorRequest) request);
            }
        }
    }

    public boolean isAccess(PersonRequest req, int floor) {
        HashSet<Integer> map = accessMap.get(floor);
        for (Integer m : map) {
            if (((m >> (req.getFromBuilding() - 'A')) & 1) == 1 &&
                    ((m >> (req.getToBuilding() - 'A')) & 1) == 1) {
                return true;
            }
        }
        return false;
    }

    public int getNearestAccessFloor(PersonRequest req) {
        int minDis = 20;
        int chosen = 1;

        int dis;
        for (int i = 1; i < 11; ++i) {
            if (isAccess(req, i)) {
                dis = Math.abs(i - req.getFromFloor()) + Math.abs(i - req.getToFloor());
                if (dis > minDis) {
                    break;
                }
                chosen = dis < minDis ? i : chosen;
                minDis = dis;
            }

            if (minDis == 0) {
                break;
            }
        }
        return chosen;
    }

    public void distributeTransRequest(TransPersonRequest transRequest) {
        if (transRequest.getCurStage() == 2) {
            processQueues.get(transRequest.getThroughFloor() + 4)
                    .addRequest(transRequest);
        } else if (transRequest.getCurStage() == 3) {
            processQueues.get(transRequest.getToBuilding() - 'A')
                    .addRequest(transRequest);
        }
    }

    public void distributePersonRequest(PersonRequest personRequest) {
        if (personRequest.getFromBuilding() == personRequest.getToBuilding()) { // Vertical-Req
            processQueues.get(personRequest.getFromBuilding() - 'A')
                    .addRequest(personRequest);
        } else {
            int chosenFloor = getNearestAccessFloor(personRequest);

            if (personRequest.getFromFloor() == personRequest.getToFloor() &&   // Horizontal-Req
                    chosenFloor == personRequest.getFromFloor()) {
                processQueues.get(chosenFloor + 4).addRequest(personRequest);
            } else {                                                            // Transfer-Req
                int fromFloor = personRequest.getFromFloor();
                TransPersonRequest transRequest = new TransPersonRequest(
                        personRequest,
                        chosenFloor,
                        (chosenFloor == fromFloor) ? 2 : 1
                );

                if (chosenFloor == fromFloor) {
                    processQueues.get(chosenFloor + 4).addRequest(transRequest);
                } else {
                    processQueues.get(transRequest.getFromBuilding() - 'A')
                            .addRequest(transRequest);
                }
                TransferCounter.getInstance().add();
            }
        }
    }

    public void distributeElevatorRequest(ElevatorRequest elevatorRequest) {
        ElevatorThread ele = new ElevatorThread(
                elevatorRequest.getElevatorId(),
                elevatorRequest.getType(),
                elevatorRequest.getBuilding(),
                elevatorRequest.getFloor(),
                elevatorRequest.getCapacity(),
                elevatorRequest.getSpeed(),
                elevatorRequest.getSwitchInfo()
        );

        if (elevatorRequest.getType().equals("building")) {
            elevatorQueues.get(elevatorRequest.getBuilding() - 'A').addElevator(ele);
        } else if (elevatorRequest.getType().equals("floor")) {
            elevatorQueues.get(elevatorRequest.getFloor() + 4).addElevator(ele);
            accessMap.get(elevatorRequest.getFloor()).add(elevatorRequest.getSwitchInfo());
        }
    }
}
