// import com.oocourse.TimableOutput;

import com.oocourse.elevator1.PersonRequest;

import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueue {
    private final LinkedBlockingQueue<PersonRequest> requestQueue;
    // private ArrayList<PersonRequest> requestQueue;
    private boolean isEnd = false;
    private int requestNum;
    private int maxNum;

    RequestQueue() {
        requestQueue = new LinkedBlockingQueue<>();
        requestNum = 0;
        maxNum = Integer.MAX_VALUE;
        isEnd = false;
    }

    RequestQueue(int num) {
        requestQueue = new LinkedBlockingQueue<>(num);
        requestNum = 0;
        maxNum = Const.MAX_PERSON;
        isEnd = false;
    }

    public synchronized void addPersonRequest(PersonRequest personRequest) {
        requestQueue.add(personRequest);
        requestNum++;
        notifyAll();
    }

    public boolean isFull() {
        return requestNum == maxNum;
    }

    public synchronized PersonRequest getPersonRequest() throws InterruptedException {
        if (isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (requestQueue.isEmpty()) {
            return null;
        }
        // TimableOutput.println("waitQueue has requests and we take one");
        PersonRequest personRequest = requestQueue.take();
        // deleteRequest(personRequest);
        notifyAll();
        return personRequest;
    }

    public synchronized PersonRequest getOnePersonRequest() {
        synchronized (requestQueue) {
            if (isEmpty()) {
                notifyAll();
                requestQueue.notifyAll();
                return null;
            }
            for (PersonRequest request :
                    requestQueue) {
                notifyAll();
                requestQueue.notifyAll();
                return request;
            }
        }
        notifyAll();
        return null;
    }

    public synchronized PersonRequest getUpPersonRequest(int floor) throws InterruptedException {
        synchronized (requestQueue) {
            // TimableOutput.println("start searching people that can get up!");
            if (requestQueue.isEmpty()) {
                notifyAll();
                return null;
            }
            for (PersonRequest request :
                    requestQueue) {
                if (request.getFromFloor() == floor) {
                    requestQueue.notifyAll();
                    notifyAll();
                    return request;
                }
            }
            requestQueue.notifyAll();
        }
        notifyAll();
        return null;
    }

    public synchronized PersonRequest getDownPersonRequest(int floor) throws InterruptedException {
        synchronized (requestQueue) {
            if (requestQueue.isEmpty()) {
                notifyAll();
                return null;
            }
            for (PersonRequest request :
                    requestQueue) {
                if (request.getToFloor() == floor) {
                    return request;
                }
            }
        }
        notifyAll();
        return null;
    }

    public synchronized void deleteRequest(PersonRequest personRequest) {
        requestQueue.remove(personRequest);
        requestNum--;
        notifyAll();
    }

    public synchronized int getMaxFloor() {
        int maxFloor = 0;
        for (PersonRequest request :
                requestQueue) {
            int tmpFloor = Math.max(request.getFromFloor(), request.getToFloor());
            maxFloor = Math.max(tmpFloor, maxFloor);
        }
        notifyAll();
        return maxFloor;
    }

    public synchronized boolean containsFloor(int nowFloor, Const.MoveType moveType) {
        // TimableOutput.println("request size: " + this.size());
        for (PersonRequest request :
                requestQueue) {
            /*TimableOutput.println("test contains floor" +
                    nowFloor + "request: " + request.toString());*/
            if (moveType == Const.MoveType.UP &&
                    request.getFromFloor() == nowFloor &&
                    request.getToFloor() > nowFloor) {
                notifyAll();
                return true;
            } else if (moveType == Const.MoveType.DOWN &&
                    request.getFromFloor() == nowFloor &&
                    request.getToFloor() < nowFloor) {
                notifyAll();
                return true;
            } else if (nowFloor == Const.MAX_FLOOR &&
                    request.getFromFloor() == Const.MAX_FLOOR) {
                notifyAll();
                return true;
            } else if (nowFloor == 1 &&
                    request.getFromFloor() == 1) {
                notifyAll();
                return true;
            }
        }
        notifyAll();
        return false;
    }

    public synchronized boolean containsFloor(int nowFloor) {
        for (PersonRequest request :
                requestQueue) {
            if (request.getToFloor() == nowFloor) {
                notifyAll();
                return true;
            }
        }
        notifyAll();
        return false;
    }

    public synchronized int size() {
        notifyAll();
        return this.requestQueue.size();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return requestQueue.isEmpty();
    }

    public synchronized void setEnd(boolean end) {
        isEnd = end;
        notifyAll();
    }

    public synchronized int getMinFloor() {
        int minFloor = Const.MAX_FLOOR + 1;
        for (PersonRequest request :
                requestQueue) {
            int tmpFloor = Math.min(request.getFromFloor(), request.getToFloor());
            minFloor = Math.min(tmpFloor, minFloor);
        }
        notifyAll();
        return minFloor;
    }

}
