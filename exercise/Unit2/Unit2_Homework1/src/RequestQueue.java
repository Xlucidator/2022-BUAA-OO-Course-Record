import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class RequestQueue {
    private final ArrayList<PersonRequest> requests;
    private boolean isEnd;

    public RequestQueue() {
        this.requests = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized int size() {
        notifyAll();
        return requests.size();
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return requests.isEmpty();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized void addRequest(PersonRequest request) {
        this.requests.add(request);
        notifyAll();
    }

    public synchronized boolean containRequest(PersonRequest request) {
        return requests.contains(request);
    }

    public synchronized PersonRequest getRequest(int i) {
        if (i >= size()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (i >= size()) {
            return null;
        }
        notifyAll();
        return requests.get(i);
    }

    public synchronized PersonRequest getNearestRequest(int location) {
        if (this.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.isEmpty()) {
            return null;
        }
        PersonRequest chosen = null;
        int min = Elevator.RANGE;
        for (int i = 0; i < size(); ++i) {
            int distance = Math.abs(requests.get(i).getFromFloor() - location);
            if (distance < min) {
                chosen = requests.get(i);
                min = distance;
            }
        }
        return chosen;
    }

    public synchronized void delRequest(int i) {
        requests.remove(i);
        notifyAll();
    }

    /* Will Modify RequestQueue Meanwhile. Used mainly in ScheduleThread */
    public synchronized PersonRequest getOneRequest() {
        if (this.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (this.isEmpty()) {
            return null;
        }

        PersonRequest request = requests.get(0);
        requests.remove(0);
        notifyAll();
        return request;
    }
}
