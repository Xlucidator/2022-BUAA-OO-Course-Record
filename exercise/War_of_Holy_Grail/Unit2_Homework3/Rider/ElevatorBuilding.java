import java.util.ArrayList;
import java.util.Iterator;

public class ElevatorBuilding extends Thread {
    private final int id;
    private final RequestQueue processingQueue;
    private final int building;
    private final ArrayList<MyRequest> myRequests;
    private int floor;
    private String stateE;
    private final int capacity;
    private final double speed;

    public RequestQueue getProcessingQueue() {
        return processingQueue;
    }

    public int getBuilding() {
        return building;
    }

    public ElevatorBuilding(RequestQueue processingQueue, int building,
                            int id, int capacity, double speed) {
        this.processingQueue = processingQueue;
        this.building = building;
        this.id = id;
        this.myRequests = new ArrayList<>();
        this.floor = 1;
        this.stateE = "static";
        this.capacity = capacity;
        this.speed = speed;
    }

    public void setStateE(String stateE) {
        this.stateE = stateE;
    }

    public void run() {
        while (true) {
            if (floor == 1) {
                setStateE("up");
            } else if (floor == 10) {
                setStateE("down");
            }

            if (processingQueue.isEmpty() && myRequests.isEmpty() &&
                    Schedule.getInstance().isEndTag()) {
                return;
            }
            ArrayList<MyRequest> requests1 = processingQueue.getRequests(stateE,
                    floor, myRequests.size(), capacity);
            if (requests1 != null) {
                myRequests.addAll(requests1);
            }
            boolean flag = openDoor(false);
            requests1 = processingQueue.getRequests(stateE,
                    floor, myRequests.size(), capacity);
            if ((requests1 != null && requests1.size() == 0) && myRequests.size() == 0) {
                if (stateE.equals("up") && processingQueue.findBigRequest(stateE) <= floor) {
                    if (floor != 1) {
                        setStateE("down");
                        addRequests();
                        flag = openDoor(flag);
                    }
                }
                else if (stateE.equals("down") && processingQueue.findBigRequest(stateE) >= floor) {
                    if (floor != 10) {
                        setStateE("up");
                        addRequests();
                        flag = openDoor(flag);
                    }
                }
            } else if ((requests1 != null) && requests1.size() > 0) {
                myRequests.addAll(requests1);
                flag = openDoor(flag);
            }

            if (flag) {
                closeDoor();
            }

            if (processingQueue.isEmpty() && myRequests.isEmpty() &&
                    Schedule.getInstance().isEndTag()) {
                return;
            }
            if (stateE.equals("up")) {
                upFloor();
            } else if (stateE.equals("down")) {
                downFloor();
            }
        }

    }

    public synchronized boolean addRequests() {
        ArrayList<MyRequest> requests1 = processingQueue.getRequests(stateE,
                floor, myRequests.size(), capacity);
        if (requests1 == null || requests1.size() == 0) {
            return false;
        }
        myRequests.addAll(requests1);
        return true;
    }

    public synchronized void upFloor() {
        floor++;
        try {
            sleep(Math.round(1000 * speed));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SafeOutput.println("ARRIVE-" + (char) (building + 65) + "-" + floor + "-" + id);
    }

    public synchronized void downFloor() {
        floor--;
        try {
            sleep(Math.round(1000 * speed));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SafeOutput.println("ARRIVE-" + (char) (building + 65) + "-" + floor + "-" + id);
    }

    private synchronized boolean ifIO() {
        for (MyRequest myRequest : myRequests) {
            if (myRequest.getToFloor() == floor ||
                    (myRequest.getFromFloor() == floor && myRequest.isIn())) {
                return true;
            }
        }
        return false;
    }

    private synchronized boolean openDoor(boolean flag) {
        if (ifIO()) {
            if (!flag) {
                SafeOutput.println("OPEN-" + (char) (building + 65) + "-" + floor + "-" + id);
            }
            doRun();
            return true;
        }
        return flag;
    }

    public int getIdE() {
        return id;
    }

    private synchronized void closeDoor() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SafeOutput.println("CLOSE-" + (char) (building + 65) + "-" + floor + "-" + id);
    }

    private synchronized void doRun() {
        Iterator<MyRequest> requestIterator = myRequests.iterator();
        while (requestIterator.hasNext()) {
            MyRequest myRequest = requestIterator.next();
            if (myRequest.getToFloor() == floor) {
                SafeOutput.println("OUT-" + myRequest.getPersonId() + "-" +
                        (char) (building + 65) + "-" + floor + "-" + id);
                myRequest.setIn(false);
                if (myRequest.getFlag() >= 4) {
                    myRequest.setFlag(myRequest.getFlag() - 4);
                } else {
                    myRequest.setFlag(0);
                }
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
            if (myRequest.getFromFloor() == floor && myRequest.isIn()) {
                SafeOutput.println("IN-" + myRequest.getPersonId() + "-" +
                        (char) (building + 65) + "-" + floor + "-" + id);
                myRequest.setIn(true);
            }
        }
    }
}
