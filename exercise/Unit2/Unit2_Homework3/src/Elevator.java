import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class Elevator {
    public static final long OPEN_TIME = 200;
    public static final long CLOSE_TIME = 200;

    private final int id;
    private final int type;
    private final int capacity;
    private final long moveTime;
    private final int switchInfo;

    private char buildNo;
    private int posBuilding;
    private int posFloor;
    private final ArrayList<PersonRequest> passengers;

    public Elevator(int id, int type, char buildNo, int floorNo,
                    int capacity, double speed, int switchInfo) {
        this.id = id;
        this.type = type;    // 1: building; 0:floor
        this.posBuilding = buildNo - 65;
        this.buildNo = buildNo;
        this.posFloor = floorNo;
        this.capacity = capacity;
        this.moveTime = (long) (speed * 1000);
        this.switchInfo = switchInfo;

        this.passengers = new ArrayList<>();
    }

    public synchronized boolean isEmpty() {
        return passengers.isEmpty();
    }

    public synchronized boolean isFull() {
        return passengers.size() == capacity;
    }

    public synchronized int size() {
        return passengers.size();
    }

    public synchronized PersonRequest getRequest(int num) {
        return this.passengers.get(num);
    }

    public synchronized int getFloor() {
        return this.posFloor;
    }

    public synchronized char getBuilding() {
        return buildNo;
    }

    public int getId() {
        return id;
    }

    /*============== Basic Movement ===============*/
    public synchronized void open() {
        SecureTimableOutput.println("OPEN" + toPostString());
    }

    public synchronized void in(PersonRequest person) {
        passengers.add(person);
        SecureTimableOutput.println("IN-" + person.getPersonId() + toPostString());
    }

    public synchronized void out(PersonRequest person) {
        passengers.remove(person);
        SecureTimableOutput.println("OUT-" + person.getPersonId() + toPostString());
    }

    public synchronized void close() {
        SecureTimableOutput.println("CLOSE" + toPostString());
    }

    public synchronized void move(int step) {
        if (type == 1) {
            posFloor += step;
        } else {
            posBuilding = (5 + posBuilding + step) % 5;
            buildNo = (char) (posBuilding + 65);
        }

        try {
            Thread.sleep(moveTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SecureTimableOutput.println("ARRIVE" + toPostString());
    }

    /*============= Combine movement ==============*/
    public synchronized boolean hasOut() {
        for (PersonRequest person : passengers) {
            if (type == 1 && person.getToFloor() == posFloor) {
                return true;
            }
            if (type == 0 && person.getToBuilding() == buildNo) {
                return true;
            }
        }
        return false;
    }

    public synchronized void getOut() {
        for (int i = passengers.size() - 1; i >= 0; i--) {
            PersonRequest person = passengers.get(i);
            if (
                    (type == 1 && person.getToFloor() == posFloor) ||
                    (type == 0 && person.getToBuilding() == buildNo)
            ) {
                out(person);
                if (person instanceof TransPersonRequest) {
                    TransPersonRequest p = (TransPersonRequest) person;
                    if (p.getCurStage() == 3) {
                        TransferCounter.getInstance().release();
                    } else {
                        p.setCurStage(p.getCurStage() + 1);
                        if (p.getFromFloor() == p.getToFloor() &&
                            p.getFromBuilding() == p.getToBuilding()) {
                            TransferCounter.getInstance().release();
                        } else {
                            ScheduleThread.getInstance().addRequest(p);
                            TransferCounter.getInstance().finish();
                        }
                    }
                }
            }
        }
    }

    public String toPostString() {
        return "-" + buildNo + "-" + posFloor + "-" + id;
    }
}
