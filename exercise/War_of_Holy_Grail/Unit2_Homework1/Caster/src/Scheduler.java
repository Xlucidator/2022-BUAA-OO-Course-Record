import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Scheduler extends Thread {
    private final ElevatorQueues elevatorQueues;
    private final WaitQueue totalQueue;

    public Scheduler(ElevatorQueues elevatorQueues, WaitQueue waitQueue) {
        this.elevatorQueues = elevatorQueues;
        this.totalQueue = waitQueue;
    }

    @Override
    public void run() {
        ArrayList<PersonRequest> temp = new ArrayList<>();
        while (true) {
            synchronized (totalQueue) {
                if (totalQueue.isEnd() && totalQueue.isEmpty()) {
                    elevatorQueues.close();
                    return;
                }
                if (totalQueue.isEmpty()) {
                    try {
                        totalQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    temp.addAll(totalQueue.getRequests());
                    while (!temp.isEmpty()) {
                        PersonRequest request = temp.get(0);
                        WaitQueue elevatorQueue = getQueue(request);
                        synchronized (elevatorQueue) {
                            elevatorQueue.addRequest(request);
                            elevatorQueue.notifyAll();
                        }
                        temp.remove(request);
                    }
                    totalQueue.clear();
                }

            }
        }
    }

    private WaitQueue getQueue(PersonRequest request) {
        char ch = request.getFromBuilding();
        return elevatorQueues.get(ch);
    }
}
