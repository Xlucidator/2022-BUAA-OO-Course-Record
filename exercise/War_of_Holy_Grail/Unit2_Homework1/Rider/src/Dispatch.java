import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Dispatch {
    private final HashMap<Character, HashMap<Integer, ArrayList<PersonRequest>>> waitQueue;
    private boolean isEnd;
    
    public Dispatch() {
        waitQueue = new HashMap<>();
        for (char i = 'A'; i <= 'E'; i++) {
            HashMap<Integer, ArrayList<PersonRequest>> tmp = new HashMap<>();
            for (int j = 1; j <= 10; j++) {
                tmp.put(j, new ArrayList<>());
            }
            waitQueue.put(i, tmp);
        }
        isEnd = false;
    }
    
    public synchronized void addRequest(PersonRequest personRequest) {
        waitQueue.get(personRequest.getFromBuilding())
                .get(personRequest.getFromFloor()).add(personRequest);
        notifyAll();
    }
    
    public synchronized HashMap<Integer, ArrayList<PersonRequest>> getCurRequest(char type) {
        return waitQueue.get(type);
    }
    
    public synchronized void setEnd() {
        isEnd = true;
        notifyAll();
    }
    
    public synchronized boolean isEnd() {
        boolean allEmpty = true;
        for (Character c : waitQueue.keySet()) {
            for (Integer i : waitQueue.get(c).keySet()) {
                if (!waitQueue.get(c).get(i).isEmpty()) {
                    allEmpty = false;
                    break;
                }
            }
            if (!allEmpty) {
                break;
            }
        }
        return this.isEnd && allEmpty;
    }
    
    public synchronized void waitTheNotify() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void notifyAllWait() {
        notifyAll();
    }
}
