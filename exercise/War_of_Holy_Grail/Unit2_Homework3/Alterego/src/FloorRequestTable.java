import com.oocourse.elevator3.PersonRequest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class FloorRequestTable {
    private final HashMap<Character, HashMap<Character, Queue<Passenger>>> requestTable;
    private boolean inputEnd;

    public FloorRequestTable() {
        requestTable = new HashMap<>();
        for (char i = 'A'; i <= 'E'; i++) {
            HashMap<Character, Queue<Passenger>> map = new HashMap<>();
            for (char j = 'A'; j <= 'E'; j++) {
                map.put(j, new LinkedList<>());
            }
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
        for (char i = 'A'; i <= 'E'; i++) {
            for (char j = 'A'; j <= 'E'; j++) {
                if (requestTable.get(i).get(j).isEmpty()) {
                    count++;
                }
            }
        }
        notifyAll();
        return (count == 25 && inputEnd);
    }

    public synchronized void addPassenger(Passenger passenger) {
        PersonRequest request = passenger.firstRequest();
        //System.out.println(request+" added");
        char from = request.getFromBuilding();
        char to = request.getToBuilding();
        requestTable.get(from).get(to).add(passenger);
        notifyAll();
    }

    public synchronized boolean havePassenger(char building, char toBuilding) {
        notifyAll();
        return !requestTable.get(building).get(toBuilding).isEmpty();
    }

    public synchronized boolean havePassenger(char building, boolean up) {
        char next1 = up ? getNext(building) : getFront(building);
        char next2 = up ? getNext(next1) : getFront(next1);
        notifyAll();
        return !requestTable.get(building).get(next1).isEmpty()
                || !requestTable.get(building).get(next2).isEmpty();
    }

    public synchronized boolean isEmpty(char building) {
        int count = 0;
        for (char j = 'A'; j <= 'E'; j++) {
            if (requestTable.get(building).get(j).isEmpty()) {
                count++;
            }
        }
        notifyAll();
        return count == 5;
    }

    public synchronized boolean isAllEmpty() {
        int count = 0;
        for (char i = 'A'; i <= 'E'; i++) {
            if (isEmpty(i)) {
                count++;
            }
        }
        notifyAll();
        return count == 5;
    }

    public synchronized Passenger deQueue(char building, char to) {
        if (!isEnd() && requestTable.get(building).get(to).isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (requestTable.get(building).get(to).isEmpty()) {
            notifyAll();
            return null;
        }
        Passenger r = requestTable.get(building).get(to).poll();
        notifyAll();
        return r;
    }

    private char getNext(char building) {
        if (building == 'E') {
            return 'A';
        } else {
            return (char) ((int) building + 1);
        }
    }

    private char getFront(char building) {
        if (building == 'A') {
            return 'E';
        } else {
            return (char) ((int) building - 1);
        }
    }
}
