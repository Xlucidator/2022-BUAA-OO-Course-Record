import java.util.ArrayList;
import java.util.Iterator;

public class ElevatorFloor extends Thread {
    private final int id;
    private final RequestQueue processingQueue;
    private final ArrayList<MyRequest> myRequests;
    private final int floor;
    private int building;
    private final int capacity;
    private final double speed;

    public int getSwitchInfo() {
        return switchInfo;
    }

    private final int switchInfo;

    public ElevatorFloor(RequestQueue processingQueue,
                         int floor, int id, int capacity, double speed, int switchInfo) {
        this.processingQueue = processingQueue;
        this.id = id;
        this.floor = floor;
        this.myRequests = new ArrayList<>();
        this.building = 0;
        this.capacity = capacity;
        this.speed = speed;
        this.switchInfo = switchInfo;
    }

    public int getIdE() {
        return id;
    }

    public RequestQueue getProcessingQueue() {
        return processingQueue;
    }

    public int getFloor() {
        return floor;
    }

    public void run()
    {
        while (true) {
            ArrayList<MyRequest> requests1 = processingQueue.getRequests("building",
                    building, myRequests.size(), capacity);
            if (requests1 == null) {
                return;
            }
            myRequests.addAll(requests1);
            doRun();
            if (myRequests.isEmpty() && Schedule.getInstance().isEndTag()) {
                return;
            }
            move();
        }
    }

    private synchronized void doRun() {
        boolean flag = false;
        for (MyRequest myRequest : myRequests) {
            if (((switchInfo >> building) & 1) == 1 &&
                    (myRequest.getToBuilding() - 65 == building ||
                    (myRequest.getFromBuilding() - 65 == building && myRequest.isIn()))) {
                flag = true;
                SafeOutput.println("OPEN-" + (char) (building + 65) + "-" + floor + "-" + id);
                break;
            }
        }
        Iterator<MyRequest> requestIterator = myRequests.iterator();
        while (requestIterator.hasNext()) {
            MyRequest myRequest = requestIterator.next();
            if (myRequest.getToBuilding() - 65 == building && ((switchInfo >> building) & 1) == 1) {
                SafeOutput.println("OUT-" + myRequest.getPersonId() + "-" +
                        (char) (building + 65) + "-" + floor + "-" + id);
                myRequest.setFlag(myRequest.getFlag() - 2);
                myRequest.setIn(false);
                if (myRequest.getFlag() == 0) {
                    RequestCounter.getInstance().release();
                } else {
                    Schedule.getInstance().addRequest(myRequest);
                }
                requestIterator.remove();
            }
        }
        requestIterator = myRequests.iterator();
        while (requestIterator.hasNext()) {
            MyRequest myRequest = requestIterator.next();
            if (myRequest.getFromBuilding() - 65 == building && myRequest.isIn() &&
                    ((switchInfo >> building) & 1) == 1) {
                SafeOutput.println("IN-" + myRequest.getPersonId() + "-" +
                        (char) (building + 65) + "-" + floor + "-" + id);
                myRequest.setIn(true);
            }
        }
        if (flag) {
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SafeOutput.println("CLOSE-" + (char) (building + 65) + "-" + floor + "-" + id);
        }
    }

    public synchronized void move() {
        building = (building + 1) % 5;
        try {
            sleep(Math.round(1000 * speed));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SafeOutput.println("ARRIVE-" + (char) (building + 65) + "-" + floor + "-" + id);
    }
}
