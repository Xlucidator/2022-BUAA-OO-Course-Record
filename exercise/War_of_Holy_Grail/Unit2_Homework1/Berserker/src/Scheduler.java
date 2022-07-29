import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Scheduler extends Thread {
    private RequestQueue waitQueue;
    private ArrayList<RequestQueue> processList;
    private ArrayList<Elevator> elevators;

    public Scheduler(RequestQueue waitQueue,
                     ArrayList<RequestQueue> processList,
                     ArrayList<Elevator> elevators) {
        this.waitQueue = waitQueue;
        this.processList = processList;
        this.elevators = elevators;
    }

    public void run() {
        while (true) {
            if (waitQueue.isEnd() && waitQueue.isEmpty()) {
                for (int i = 0; i < processList.size(); i++) {
                    processList.get(i).setEnd(true);
                }
                //System.out.println("Scheduler End");
                return;
            }
            synchronized (waitQueue) {
                if (waitQueue.isEmpty()) {
                    try {
                        //System.out.println("waitQueueWaitBegin");
                        waitQueue.wait();
                        //System.out.println("waitQueueWaitEnd");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //System.out.println("break1");
                PersonRequest request = waitQueue.pollRequest();
                //System.out.println("break2");
                if (request == null) {
                    continue;
                }
                int eleId = request.getFromBuilding() - 'A';
                RequestQueue eleProcess = processList.get(eleId);
                eleProcess.putRequest(request);
            }
            //Elevator elevator = elevators.get(EleId);

            //updateProcess(elevator, eleProcess, request);
        }
    }

    public synchronized void updateProcess(Elevator elevator,
                                           RequestQueue eleProcess,
                                           PersonRequest request) {
        String direction = getRequestDirection(request);
        if (elevator.isEmpty()) {
            eleProcess.putRequest(request);
            elevator.setMoveStatus(getEleDirection(request, elevator));
            notifyAll();
            return;
        }

        if (direction.equals("UP")
                && elevator.getMoveStatus().equals("UP")
                && request.getFromFloor() <= elevator.getNowFloor()) {
            eleProcess.putRequest(request);
        } else if (direction.equals("DOWN")
                && elevator.getMoveStatus().equals("DOWN")
                && request.getFromFloor() >= elevator.getNowFloor()) {
            eleProcess.putRequest(request);
        }
    }

    public synchronized String getRequestDirection(PersonRequest request) {
        return (request.getToFloor() - request.getFromFloor() > 0) ? "UP" :
                (request.getToFloor() - request.getFromFloor() < 0) ? "DOWN" : "STOP";
    }

    public synchronized String getEleDirection(PersonRequest request, Elevator elevator) {
        return (elevator.getNowFloor() - request.getFromFloor() > 0) ? "UP" :
                (elevator.getNowFloor() - request.getFromFloor() < 0) ? "DOWN" : "STOP";
    }
}
