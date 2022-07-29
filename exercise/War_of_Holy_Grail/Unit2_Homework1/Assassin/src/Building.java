// import com.oocourse.TimableOutput;
// import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Building {
    private final RequestQueue waitQueue;
    private ArrayList<Elevator> elevators;
    private char id;
    private boolean isEnd = false;

    Building(char id) {
        this.id = id;
        isEnd = false;
        elevators = new ArrayList<>();
        waitQueue = new RequestQueue();
    }

    public synchronized void addRequest(PersonRequest request) {
        synchronized (waitQueue) {
            /*synchronized (TimableOutput.class){
                TimableOutput.println("Building " + id +
                 " receive a request:" + request.toString());
                TimableOutput.class.notifyAll();
            }*/
            waitQueue.addPersonRequest(request);
            waitQueue.notifyAll();
            /*TimableOutput.println("Building " + id + " release the key of request:"
                + request.toString());*/
        }
        // scheduleRequest(request);
        notifyAll();
    }

    // schedule just for one elevator
    public synchronized void scheduleRequest(PersonRequest request) {
        // TimableOutput.println("Building " + id + " schedule the request:" + request.toString());
        elevators.get(0).addRequest(request);
        removeRequest(request);
        notifyAll();
    }

    public synchronized void removeRequest(PersonRequest request) {
        synchronized (waitQueue) {
            waitQueue.deleteRequest(request);
            notifyAll();
        }
        notifyAll();
    }

    public void addElevator(Elevator elevator) {
        Thread thread = new Thread(elevator);
        elevator.setWaitQueue(waitQueue);
        thread.start();
        elevators.add(elevator);
    }

    public boolean isEnd() {
        return isEnd;
    }

    public synchronized void setEnd(boolean end) {
        waitQueue.setEnd(end);
        /* TimableOutput.println("Set building " + id +
                " set End"); */
        isEnd = end;
        for (Elevator e :
                elevators) {
            e.setEnd(end);
        }
        notifyAll();
    }

    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }
}
