import com.oocourse.elevator1.PersonRequest;

import java.util.concurrent.LinkedBlockingDeque;

public class RequestQueue {
    private LinkedBlockingDeque<PersonRequest> requests;
    private boolean isEnd;

    public RequestQueue() {
        requests = new LinkedBlockingDeque<>();
        isEnd = false;
    }

    public LinkedBlockingDeque<PersonRequest> getRequests() {
        return requests;
    }

    public synchronized void putRequest(PersonRequest request) {
        try {
            this.requests.put(request);
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized PersonRequest pollRequest() {
        return this.requests.poll();
    }

    public synchronized PersonRequest takeRequest() {
        try {
            return this.requests.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return requests.isEmpty();
    }
}
