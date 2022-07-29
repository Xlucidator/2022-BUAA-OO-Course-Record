import com.oocourse.elevator3.PersonRequest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BuildingRequestTable {
    private final HashMap<Integer, HashMap<Boolean, Queue<Passenger>>> requestTable;
    private boolean inputEnd;

    public BuildingRequestTable() {
        requestTable = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            HashMap<Boolean, Queue<Passenger>> map = new HashMap<>();
            map.put(true, new LinkedList<>());
            map.put(false, new LinkedList<>());
            requestTable.put(i, map);
        }
        inputEnd = false;
    }

    public synchronized void setInputEndTrue() {
        inputEnd = true;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        int count = 0;
        for (int i = 1; i <= 10; i++) {
            if (requestTable.get(i).get(true).isEmpty()
                    && requestTable.get(i).get(false).isEmpty()) {
                count++;
            }
        }
        notifyAll();
        return (count == 10 && inputEnd);
    }

    public synchronized void addPassenger(Passenger passenger) {
        //System.out.println(request+" added");
        PersonRequest request = passenger.firstRequest();
        boolean up = request.getFromFloor() < request.getToFloor();
        requestTable.get(request.getFromFloor()).get(up).add(passenger);
        notifyAll();
    }

    public synchronized boolean havePassenger(int floor, boolean up) {
        notifyAll();
        return !requestTable.get(floor).get(up).isEmpty();
    }

    public synchronized boolean isEmpty(int floor) {
        notifyAll();
        return requestTable.get(floor).get(true).isEmpty()
                && requestTable.get(floor).get(false).isEmpty();
    }

    public synchronized boolean isAllEmpty() {
        int count = 0;
        for (int i = 1; i <= 10; i++) {
            if (isEmpty(i)) {
                count++;
            }
        }
        notifyAll();
        return count == 10;
    }

    public synchronized Passenger deQueue(int floor, boolean up) {
        if (!isEnd() && requestTable.get(floor).get(up).isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isEmpty(floor)) {
            notifyAll();
            return null;
        }
        Passenger r = requestTable.get(floor).get(up).poll();
        notifyAll();
        return r;
    }
}
