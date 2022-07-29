import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class ElevatorThread extends Thread {
    private final Dispatch dispatch;
    
    private final int lowestFloor;
    private final int highestFloor;
    private final int type;
    private final char name;
    private final int moveTime;
    private final int openTime;
    private final int closeTime;
    
    private int curFloor;
    private boolean isUp;
    private boolean isOpen;
    private final ArrayList<PersonRequest> requests;
    private long lastOpenTime;
    
    private final int capacity;
    
    public ElevatorThread(int type, int moveTime, int openTime, int closeTime,
                          int capacity, Dispatch dispatch, int curFloor, int lowestFloor,
                          int highestFloor) {
        this.type = type;
        this.name = (char) (type - 1 + 'A');
        this.moveTime = moveTime;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.capacity = capacity;
        this.dispatch = dispatch;
        this.curFloor = curFloor;
        this.lowestFloor = lowestFloor;
        this.highestFloor = highestFloor;
        this.isUp = true;
        this.isOpen = false;
        this.requests = new ArrayList<>();
    }
    
    @Override
    public void run() {
        //System.out.println("Elevator " + name + " is begin.");
        execute();
        //System.out.println("Elevator " + name + " is over.");
    }
    
    public synchronized void execute() {
        while (true) {
            if (this.checkEnd()) {
                break;
            }
            String execute = look();
            //System.out.println("Elevator " + name + " has gotten the execute " + execute);
            if (execute.equals("UP")) {
                up();
            } else if (execute.equals("DOWN")) {
                down();
            } else if (execute.equals("OPEN")) {
                open();
            } else {
                dispatch.waitTheNotify();
            }
        }
        if (isOpen) {
            close();
        }
        dispatch.notifyAllWait();
    }
    
    private synchronized boolean shouldOpen(HashMap<Integer, ArrayList<PersonRequest>> waitQueue) {
        boolean someoneOn = false;
        for (PersonRequest personRequest : waitQueue.get(this.curFloor)) {
            if ((personRequest.getToFloor() - personRequest.getFromFloor() >= 0) == isUp) {
                someoneOn = true;
                break;
            }
        }
        boolean someoneOff = false;
        for (PersonRequest personRequest : requests) {
            if (personRequest.getToFloor() == curFloor) {
                someoneOff = true;
            }
            break;
        }
        return someoneOff || (someoneOn && !isFull());
    }
    
    private synchronized String look() {
        synchronized (dispatch) {
            HashMap<Integer, ArrayList<PersonRequest>> waitQueue = dispatch.getCurRequest(name);
            //判断有无外请求 内请求是否完成
            boolean noReq = true;
            for (Integer integer : waitQueue.keySet()) {
                if (!waitQueue.get(integer).isEmpty()) {
                    noReq = false;
                    break;
                }
            }
            if (noReq && requests.isEmpty()) {
                return "PAUSE";
            }
            
            int maxRequest = lowestFloor;
            int minRequest = highestFloor;
            
            for (PersonRequest personRequest : requests) {
                if (personRequest.getToFloor() > maxRequest) {
                    maxRequest = personRequest.getToFloor();
                }
                if (personRequest.getToFloor() < minRequest) {
                    minRequest = personRequest.getToFloor();
                }
            }
            for (Integer integer : waitQueue.keySet()) {
                if (!waitQueue.get(integer).isEmpty() && integer > maxRequest) {
                    maxRequest = integer;
                }
                if (!waitQueue.get(integer).isEmpty() && integer < minRequest) {
                    minRequest = integer;
                }
            }
            
            if (shouldOpen(waitQueue)) {
                return "OPEN";
            }
            boolean shouldTurn = (isUp && (curFloor >= highestFloor || curFloor >= maxRequest)) ||
                    (!isUp && (curFloor <= lowestFloor || curFloor <= minRequest));
            if (shouldTurn) {
                isUp = !isUp;
                return "OPEN";
            }
            
            if (isUp) {
                return "UP";
            } else {
                return "DOWN";
            }
        }
        
    }
    
    private synchronized void getOff() {
        ArrayList<PersonRequest> remove = new ArrayList<>();
        for (PersonRequest personRequest : requests) {
            if (personRequest.getToFloor() == curFloor) {
                remove.add(personRequest);
            }
        }
        for (PersonRequest personRequest : remove) {
            synchronized (TimableOutput.class) {
                TimableOutput.println(
                        "OUT-" + personRequest.getPersonId() +
                                "-" + name + "-" + curFloor + "-" + type
                );
                TimableOutput.class.notifyAll();
            }
            requests.remove(personRequest);
        }
    }
    
    private synchronized void getOn() {
        synchronized (dispatch) {
            ArrayList<PersonRequest> curRequests = dispatch.getCurRequest(name).get(curFloor);
            ArrayList<PersonRequest> remove = new ArrayList<>();
            if (isFull()) {
                return;
            }
            for (PersonRequest personRequest : curRequests) {
                if (isUp == (personRequest.getToFloor() > personRequest.getFromFloor())) {
                    remove.add(personRequest);
                    requests.add(personRequest);
                    if (isFull()) {
                        break;
                    }
                }
            }
            for (PersonRequest personRequest : remove) {
                curRequests.remove(personRequest);
                TimableOutput.println(
                        "IN-" + personRequest.getPersonId() + "-" +
                                this.name + "-" + this.curFloor + "-" + this.type
                );
            }
        }
    }
    
    private synchronized void open() {
        if (!isOpen) {
            synchronized (TimableOutput.class) {
                TimableOutput.println(
                        "OPEN-" + this.name + "-" + this.curFloor + "-" + this.type
                );
                TimableOutput.class.notifyAll();
            }
        }
        this.isOpen = true;
        getOff();
        getOn();
        try {
            sleep(openTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.lastOpenTime = System.currentTimeMillis();
        
    }
    
    private synchronized void close() {
        boolean shouldWait = System.currentTimeMillis() <= closeTime + this.lastOpenTime;
        long waitTime = closeTime + lastOpenTime - System.currentTimeMillis();
        if (shouldWait) {
            try {
                sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.isOpen = false;
        synchronized (TimableOutput.class) {
            TimableOutput.println(
                    "CLOSE-" + this.name + "-" + this.curFloor + "-" + this.type
            );
            TimableOutput.class.notifyAll();
        }
    }
    
    private synchronized void up() {
        closeAnyway();
        
        try {
            sleep(moveTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        ++curFloor;
        synchronized (TimableOutput.class) {
            TimableOutput.println(
                    "ARRIVE-" + this.name + "-" + this.curFloor + "-" + this.type
            );
            TimableOutput.class.notifyAll();
        }
    }
    
    private synchronized void down() {
        closeAnyway();
        
        try {
            sleep(moveTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        --curFloor;
        synchronized (TimableOutput.class) {
            TimableOutput.println(
                    "ARRIVE-" + this.name + "-" + this.curFloor + "-" + this.type
            );
            TimableOutput.class.notifyAll();
        }
        
    }
    
    private synchronized boolean isFull() {
        return this.requests.size() == capacity;
    }
    
    private synchronized boolean checkEnd() {
        return dispatch.isEnd() && this.requests.isEmpty();
    }
    
    private void closeAnyway() {
        if (isOpen) {
            close();
        }
    }
}
