import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.ArrayList;

public class Requestqueue {
    private ArrayList<Request> requests = new ArrayList<>();//共享队列
    private boolean isEnd;

    public Requestqueue() {
        this.isEnd = false;
    }

    public synchronized void remove(Request request) {
        requests.remove(request);
        notifyAll();
    }

    public synchronized void addRequest(Request request) {
        requests.add(request);
        notifyAll();
    }

    public synchronized Request getRequest() {
        if (!this.isEnd) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (requests.isEmpty()) {
            return null;
        }
        Request request = requests.get(0);
        requests.remove(0);
        notifyAll();
        return request;
    }

    public synchronized ArrayList<Request> getRequests() {
        return requests;
    }

    public synchronized ArrayList<PersonRequest> getRequestscopy() {
        if (requests.isEmpty() && !this.isEnd) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (requests.isEmpty()) {
            return null;
        }
        notifyAll();
        ArrayList<PersonRequest> requestss = new ArrayList<>();
        for (Request request : this.getRequests()) {
            PersonRequest request1 = (PersonRequest) request;
            requestss.add(new PersonRequest(request1.getFromFloor(), request1.getToFloor(),
                    request1.getFromBuilding(), request1.getToBuilding(), request1.getPersonId()));
        }
        return requestss;
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized void setEnd(boolean a) {
        this.isEnd = a;
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return requests.isEmpty();
    }
}
