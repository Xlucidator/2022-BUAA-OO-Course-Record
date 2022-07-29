import com.oocourse.elevator3.Request;

import java.util.ArrayList;

public class RequestQueue {
    private final ArrayList<Request> requests;
    private boolean isEnd;

    public RequestQueue() {
        this.requests = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized void addRequest(Request request) {
        this.requests.add(request);
        notifyAll();
    }

    /* Will Modify RequestQueue Meanwhile. Used mainly in ScheduleThread */
    public synchronized Request getOneRequest() {
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

        Request request = requests.get(0);
        requests.remove(0);
        notifyAll();
        return request;
    }

    public synchronized void check() {
        notifyAll();
    }
}
