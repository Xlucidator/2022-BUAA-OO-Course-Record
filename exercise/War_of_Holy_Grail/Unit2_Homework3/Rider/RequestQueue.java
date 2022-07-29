import java.util.ArrayList;
import java.util.Iterator;

public class RequestQueue {
    private final ArrayList<MyRequest> myRequests;

    public RequestQueue() {
        myRequests = new ArrayList<>();
    }

    public synchronized void addRequest(MyRequest myRequest) {
        myRequests.add(myRequest);
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return myRequests.isEmpty();
    }

    public synchronized ArrayList<MyRequest> getRequests(String flag, int floor,
                                                         int max, int capacity) {
        ArrayList<MyRequest> tempList = null;
        while (!Schedule.getInstance().isEndTag()) {
            if (isEmpty() && max == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                tempList = new ArrayList<>();
                Iterator<MyRequest> requestIterator = myRequests.iterator();
                if (requestIterator.hasNext()) {
                    while (requestIterator.hasNext() && tempList.size() + max < capacity) {
                        MyRequest myRequest = requestIterator.next();
                        if (flag.equals(myRequest.getDirection())) {
                            if ((flag.equals("down") && myRequest.getFromFloor() <= floor) ||
                                    (flag.equals("up") && myRequest.getFromFloor() >= floor)) {
                                tempList.add(myRequest);
                                requestIterator.remove();
                            } else if (flag.equals("building") &&
                                    myRequest.getFromBuilding() - 65 == floor) {
                                tempList.add(myRequest);
                                requestIterator.remove();
                            }
                        }
                    }
                }
                break;
            }
        }
        return tempList;
    }

    public synchronized int findBigRequest(String flag) {
        int max = 1;
        if (isEmpty() && !Schedule.getInstance().isEndTag()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (flag.equals("up")) {
            for (MyRequest myRequest : myRequests) {
                if (myRequest.getDirection().equals("down") && myRequest.getFromFloor() > max) {
                    max = myRequest.getFromFloor();
                }
            }
        } else if (flag.equals("down")) {
            max = 10;
            for (MyRequest myRequest : myRequests) {
                if (myRequest.getDirection().equals("up") && myRequest.getFromFloor() < max) {
                    max = myRequest.getFromFloor();
                }
            }
        }
        return max;
    }

    public synchronized int size() {
        return myRequests.size();
    }
}
