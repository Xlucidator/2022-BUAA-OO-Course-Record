import java.util.HashMap;
import java.util.HashSet;

//shared object [tray]
public class Request {
    //(k,v)->v waiting in k floor(k is the starting point)
    private HashMap<Integer, HashSet<Person>> waitingList;
    private boolean finished;

    public Request() {
        this.waitingList = new HashMap<>();
        this.finished = false;
    }

    public synchronized void add(Integer start, Person p) {
        if (waitingList.containsKey(start)) {
            waitingList.get(start).add(p);
        } else {
            HashSet<Person> tmp = new HashSet<>();
            tmp.add(p);
            waitingList.put(start, tmp);
        }
        notifyAll();
    }

    public synchronized void remove(Integer start, Person p) {
        if (waitingList.get(start).size() == 1) {
            waitingList.remove(start);
        } else {
            waitingList.get(start).remove(p);
        }
        notifyAll();
    }

    public synchronized void setFinished(boolean finished) {
        this.finished = finished;
        notifyAll();
    }

    public boolean isFinished() {
        return finished;
    }

    public HashMap<Integer, HashSet<Person>> getWaitingList() {
        return waitingList;
    }
}
