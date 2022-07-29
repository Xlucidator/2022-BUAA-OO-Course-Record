import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class PersonRequestQueue {
    private final ArrayList<PersonRequest> requests;
    private boolean isEnd;

    public PersonRequestQueue() {
        this.requests = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized boolean isEnd() {
        return this.isEnd;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    public synchronized void addRequest(PersonRequest personRequest) {
        requests.add(personRequest);
        notifyAll();
    }

    public synchronized PersonRequest getOneRequest() {
        //System.out.println("Try to fetch one request");
        if (this.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("fetch finish");
        if (this.isEmpty()) {
            return null;
        }

        PersonRequest request = requests.get(0);
        requests.remove(0);
        notifyAll();
        return request;
    }

    /* =========================================================== */
    public synchronized int size() {
        return requests.size();
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

    public synchronized PersonRequest getFarthestRequest(int location) {
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
        int max = -1;
        for (int i = 0; i < size(); ++i) {
            int distance = Math.abs(requests.get(i).getFromFloor() - location);
            if (distance > max) {
                chosen = requests.get(i);
                max = distance;
            }
        }
        return chosen;
    }

    public synchronized void delRequest(int i) {    // make sure it exists
        requests.remove(i);
        notifyAll();
    }
}
