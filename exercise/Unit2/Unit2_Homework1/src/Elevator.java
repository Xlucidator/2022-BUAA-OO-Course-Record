import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Elevator {
    public static final int CAPACITY = 6;
    public static final int RANGE = 10;
    public static final long MOVE_TIME = 400;
    public static final long OPEN_TIME = 200;
    public static final long CLOSE_TIME = 200;

    private final int id;
    private final char belong;
    private int location;
    private final ArrayList<PersonRequest> passengers;

    public Elevator(int id) {
        this.id = id;
        this.belong = (char) (64 + id);
        this.location = 1;
        this.passengers = new ArrayList<>();
    }

    public synchronized boolean isEmpty() {
        return passengers.isEmpty();
    }

    public synchronized boolean isFull() {
        return passengers.size() == CAPACITY;
    }

    public synchronized int size() {
        return passengers.size();
    }

    public synchronized PersonRequest getRequest(int num) {
        return this.passengers.get(num);
    }

    public synchronized int getLocation() {
        return this.location;
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
        location += step;
        try {
            Thread.sleep(MOVE_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SecureTimableOutput.println("ARRIVE" + toPostString());
    }

    /*============= Combine movement ==============*/
    public synchronized boolean hasOut() {
        for (PersonRequest person : passengers) {
            if (person.getToFloor() == location) {
                return true;
            }
        }
        return false;
    }

    public synchronized void getOut() {
        for (int i = passengers.size() - 1; i >= 0; i--) {
            PersonRequest person = passengers.get(i);
            if (person.getToFloor() == location) {
                out(person);
            }
        }
    }

    public String toPostString() {
        return "-" + belong + "-" + location + "-" + id;
    }
}
