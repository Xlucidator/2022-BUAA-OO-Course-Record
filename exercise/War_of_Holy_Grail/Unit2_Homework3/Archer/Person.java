import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Person {
    private ArrayList<MyRequest> requests;
    private final int id;
    private final int fromFloor;
    private final char fromBuilding;
    private final int toFloor;
    private final char toBuilding;

    public Person(PersonRequest personRequest) {
        this.fromBuilding = personRequest.getFromBuilding();
        this.toBuilding = personRequest.getToBuilding();
        this.fromFloor = personRequest.getFromFloor();
        this.toFloor = personRequest.getToFloor();
        this.id = personRequest.getPersonId();
        this.requests = new ArrayList<>();
        if (this.fromBuilding == this.toBuilding) {
            requests.add(new MyRequest(fromFloor, toFloor, fromBuilding, toBuilding));
        } else {
            parseRequest();
        }
    }

    private void checkAdd(int fromFloor, int toFloor, char fromBuilding, char toBuilding) {
        if (!(fromFloor == toFloor && fromBuilding == toBuilding)) {
            requests.add(new MyRequest(fromFloor, toFloor, fromBuilding, toBuilding));
        }
    }

    private void add(int floor) {
        checkAdd(fromFloor, floor, fromBuilding, fromBuilding);
        checkAdd(floor, floor, fromBuilding, toBuilding);
        checkAdd(floor, toFloor, toBuilding, toBuilding);
    }

    private double isReach(int floor) {
        HashMap<Integer, Double> map = Factory.getFactory().getMap(floor);
        int distance = (fromBuilding - toBuilding + 5) % 5;
        double min = 10.0;
        if (!map.isEmpty()) {
            for (Integer integer : map.keySet()) {
                if (((integer >> (fromBuilding - 'A')) & 1) == 1 &&
                        ((integer >> (toBuilding - 'A')) & 1) == 1) {
                    if (distance * map.get(integer) < min) {
                        min = distance * map.get(integer);
                    }
                }
            }
        }
        return min;
    }

    private void parseRequest() {
        double min = 9.0;
        int minFloor = 1;

        for (int floor = 1; floor <= 10; floor++) {
            if ((floor >= fromFloor && floor <= toFloor) ||
                    (floor <= fromFloor && floor >= toFloor)) {
                if (isReach(floor) < min) {
                    min = isReach(floor);
                    minFloor = floor;
                }
            }
        }
        if (min < 8.0) {
            add(minFloor);
            return;
        }


        for (int floor = 1; floor < 10; floor++) {
            if (fromFloor + floor <= 10) {
                if (isReach(fromFloor + floor) < 9.0) {
                    add(fromFloor + floor);
                    return;
                }
            }

            if (fromFloor - floor >= 1) {
                if (isReach(fromFloor - floor) < 9.0) {
                    add(fromFloor - floor);
                    return;
                }
            }
        }
    }

    public ArrayList<MyRequest> getRequests() {
        return requests;
    }

    public int getId() {
        return id;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public char getFromBuilding() {
        return fromBuilding;
    }

    public int getToFloor() {
        return toFloor;
    }

    public char getToBuilding() {
        return toBuilding;
    }
}
