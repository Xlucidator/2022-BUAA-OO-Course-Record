import com.oocourse.TimableOutput;

import java.util.HashMap;
import java.util.HashSet;

public class Elevator implements Runnable {
    private int state;
    private int currFloor;
    private int minFloor;
    private int maxFloor;
    private char nameBuild;
    private int eleID;
    private int capacity;//6
    private int curNum;//current number of people in ele
    private long speed;//ms between floor 400
    private HashMap<Integer, HashSet<Person>> passengers;
    //passengers:key:destFloor value:people in the elevator
    private Controller controller;
    private Request request;

    public Elevator(int currFloor, int eleID, char nameBuild,
                    int capacity, long speed, Request request) {
        this.currFloor = currFloor;
        this.eleID = eleID;
        this.capacity = capacity;
        this.speed = speed;
        this.request = request;
        this.nameBuild = nameBuild;
        this.passengers = new HashMap<Integer, HashSet<Person>>();
        this.state = Constant.WAIT;
        this.minFloor = 1;
        this.maxFloor = 10;
    }

    @Override
    public void run() {
        //System.out.println("----------start---------"+Thread.currentThread().getName());
        while (true) {
            //System.out.println(this.request.getWaitingList().size());
            //no one in nor waiting the elevator
            if (passengers.isEmpty() && request.getWaitingList().isEmpty()
                    && request.isFinished()) {
                break;
            }
            if (passengers.isEmpty() && request.getWaitingList().isEmpty()) {
                synchronized (request) {
                    try {
                        //no further request
                        //System.out.println("----waiting----"+Thread.currentThread().getName());
                        request.wait();
                        if (request.isFinished()) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //System.out.println(this.getWaiting().size());
            controller = new Controller(this);
            if (controller.stop()) {
                gap();
            }
            if (passengers.isEmpty() && request.getWaitingList().isEmpty()
                    && request.isFinished()) {
                break;
            }
            //next move
            if (passengers.isEmpty()
                    && request.getWaitingList().isEmpty()) {
                //System.out.println("-----------nonextmove-------"
                // +Thread.currentThread().getName());
                //System.out.println("finished: "+request.isFinished()
                // +Thread.currentThread().getName());
                synchronized (request) {
                    try {
                        request.wait();
                        if (request.isFinished()) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            controller.setDir();
            move();
        }
        //System.out.println("-------------------end"+Thread.currentThread().getName());
    }

    public void gap() {
        HashMap<Integer, HashSet<Person>> waiting = request.getWaitingList();
        //people waiting for this elevator
        open();
        sleep(200);
        if (!controller.getOff().isEmpty()) {
            controller.getOff().forEach((p) -> {
                out(p);
                if (passengers.get(currFloor).size() == 1) {
                    passengers.remove(currFloor);
                } else {
                    passengers.get(currFloor).remove(p);
                }
            });
        }
        if (!controller.getOn().isEmpty()) {
            controller.getOn().forEach((p) -> {
                in(p);
                if (waiting.get(currFloor).size() == 1) {
                    waiting.remove(currFloor);
                } else {
                    waiting.get(currFloor).remove(p);
                }
                if (passengers.containsKey(p.getDestFloor())) {
                    passengers.get(p.getDestFloor()).add(p);
                } else {
                    HashSet<Person> set = new HashSet<Person>();
                    set.add(p);
                    passengers.put(p.getDestFloor(), set);
                }
            });
        }
        sleep(200);
        close();
    }

    public int getMinFloor() {
        return minFloor;
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public Request getRequest() {
        return request;
    }

    public HashMap<Integer, HashSet<Person>> getWaiting() {
        return request.getWaitingList();
    }

    public int getCurNum() {
        return curNum;
    }

    public int getState() {
        return state;
    }

    public boolean isFull() {
        return curNum == capacity;
    }

    public boolean isEmpty() {
        return passengers.isEmpty();
    }

    public void changeCurr(int delta) {
        curNum += delta;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCapacity() {
        return capacity;
    }

    public HashMap<Integer, HashSet<Person>> getPassengers() {
        return passengers;
    }

    public int getCurrFloor() {
        return currFloor;
    }

    public void open() {
        //make sure output is in time order
        synchronized (TimableOutput.class) {
            TimableOutput.println(String.format("OPEN-"
                    + nameBuild + "-" + currFloor + "-" + eleID));
        }
    }

    public void in(Person person) {
        synchronized (TimableOutput.class) {
            TimableOutput.println(String.format("IN-" +
                    person.getId() + "-" + nameBuild + "-" + currFloor + "-" + eleID));
        }
    }

    public void out(Person person) {
        synchronized (TimableOutput.class) {
            TimableOutput.println(String.format("OUT-" +
                    person.getId() + "-" + nameBuild + "-" + currFloor + "-" + eleID));
        }
    }

    public void close() {
        synchronized (TimableOutput.class) {
            TimableOutput.println(String.format("CLOSE-" +
                    nameBuild + "-" + currFloor + "-" + eleID));
        }
    }

    public void arrive() {
        synchronized (TimableOutput.class) {
            TimableOutput.println(String.format("ARRIVE-" +
                    nameBuild + "-" + currFloor + "-" + eleID));
        }
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void move() {
        sleep(speed);
        if (state == Constant.DOWN) {
            currFloor -= 1;
            arrive();
        } else if (state == Constant.UP) {
            currFloor += 1;
            arrive();
        }
    }

}
