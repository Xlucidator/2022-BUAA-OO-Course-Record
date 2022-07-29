import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FloorElevator extends Thread {
    private final int id;
    private int capacity;
    private final int floor;
    private char building;
    private boolean up;
    private final FloorRequestTable floorRequestTable;
    private final HashMap<Character, ArrayList<Passenger>> passengers;
    private final HashSet<Character> canAchieve;
    private final int speed;
    private final int maxCapacity;

    @Override
    public void run() {
        while (true) {
            if (floorRequestTable.isEnd() && !hasPassengers()
                    && PassengerCount.getInstance().over()) {
                //System.out.println("Ele" + id + " end");
                return;
            }
            if (floorRequestTable.isAllEmpty() && !hasPassengers()) {
                synchronized (floorRequestTable) {
                    try {
                        floorRequestTable.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!passengers.get(building).isEmpty() && canAchieve(building)) {
                try {
                    openDoor();
                    passengerOut();
                    closeDoor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            char nextBuilding = destination();
            if (nextBuilding == building) {
                if (turn()) {
                    nextBuilding = destination();
                }
            }

            if (floorRequestTable.havePassenger(building, up)
                    && capacity < maxCapacity && canAchieve(building)) {
                try {
                    openDoor();
                    passengerIn();
                    closeDoor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (nextBuilding != building) {
                try {
                    moveTo(nextBuilding);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public FloorElevator(int f, int idPara, int s, int mc, int m) {
        id = idPara;
        capacity = 0;
        floor = f;
        building = 'A';
        up = true;
        floorRequestTable = RequestTables.getFloorRequestTable(floor);
        passengers = new HashMap<>();
        for (char i = 'A'; i <= 'E'; i++) {
            passengers.put(i, new ArrayList<>());
        }
        speed = s;
        maxCapacity = mc;
        canAchieve = new HashSet<>();

        for (char c = 'A'; c <= 'E'; c++) {
            if (((m >> (c - 'A')) & 1) == 1) {
                canAchieve.add(c);
            }
        }
    }

    private char destination() {
        char dest = building;
        if (hasPassengers()) {
            if (up) {
                for (char i = getTop(); i != building; i = getFront(i)) {
                    if (!passengers.get(i).isEmpty() || !floorRequestTable.isEmpty(i)) {
                        dest = i;
                        break;
                    }
                }
            } else {
                for (char i = getBottom(); i != building; i = getNext(i)) {
                    if (!passengers.get(i).isEmpty() || !floorRequestTable.isEmpty(i)) {
                        dest = i;
                        break;
                    }
                }
            }
        } else {
            if (floorRequestTable.isEmpty(building)) {
                if (up) {
                    for (char i = getTop(); i != building; i = getFront(i)) {
                        if (!floorRequestTable.isEmpty(i)) {
                            dest = i;
                            break;
                        }
                    }
                } else {
                    for (char i = getBottom(); i != building; i = getNext(i)) {
                        if (!floorRequestTable.isEmpty(i)) {
                            dest = i;
                            break;
                        }
                    }
                }
            } else {
                if (!floorRequestTable.havePassenger(building, up)) {
                    up = !up;
                }
                try {
                    if (canAchieve(building)) {
                        openDoor();
                        passengerIn();
                        closeDoor();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dest = destination();
            }
        }
        return dest;
    }

    private boolean turn() {
        boolean change = true;
        if (up) {
            char top = getTop();
            for (char i = getNext(building); i != top; i = getNext(i)) {
                if (!passengers.get(i).isEmpty() || !floorRequestTable.isEmpty(i)) {
                    change = false;
                }
            }
        } else {
            char bottom = getBottom();
            for (char i = getFront(building); i != bottom; i = getFront(i)) {
                if (!passengers.get(i).isEmpty() || !floorRequestTable.isEmpty(i)) {
                    change = false;
                }
            }
        }
        if (change) {
            up = !up;
        }
        return change;
    }

    private void moveTo(char destination) throws Exception {
        if (destination == building) {
            return;
        }
        while (building != destination) {
            if (up) {
                building = getNext(building);
            } else {
                building = getFront(building);
            }
            sleep(speed);
            Output.println("ARRIVE-" + building + '-' + floor + '-' + id);
            if (!passengers.get(building).isEmpty() && canAchieve(building)) {
                openDoor();
                passengerOut();
                closeDoor();
            }

            if (floorRequestTable.havePassenger(building, up)) {
                if (capacity < maxCapacity && canAchieve(building)) {
                    openDoor();
                    passengerIn();
                    closeDoor();
                }
            }
        }
    }

    private void openDoor() throws Exception {
        Output.println("OPEN-" + building + '-' + floor + '-' + id);
        sleep(200);
    }

    private void closeDoor() throws Exception {
        sleep(200);
        Output.println("CLOSE-" + building + '-' + floor + '-' + id);
    }

    private void passengerOut() {
        for (Passenger passenger : passengers.get(building)) {
            Output.println("OUT-" + passenger.getId() +
                    '-' + building + '-' + floor + '-' + id);
            capacity--;
            passenger.accomplishFirst();
            if (!passenger.achieve()) {
                RequestTables.addPassenger(passenger);
            } else {
                PassengerCount.getInstance().minus();
            }
        }
        passengers.put(building, new ArrayList<>());
    }

    private void passengerIn() {
        char toBuilding = building;
        for (int i = 0; i < 2; i++) {
            toBuilding = up ? getNext(toBuilding)
                    : getFront(toBuilding);
            while (capacity < maxCapacity
                    && floorRequestTable.havePassenger(building, toBuilding)) {
                Passenger passenger = floorRequestTable.deQueue(building, toBuilding);
                if (passenger != null) {
                    if (passenger.achieve()) {
                        continue;
                    }
                    PersonRequest request = passenger.firstRequest();
                    Output.println("IN-" + request.getPersonId()
                            + '-' + building + '-' + floor + '-' + id);
                    passengers.get(request.getToBuilding()).add(passenger);
                    capacity++;
                }
            }
        }
    }

    private boolean hasPassengers() {
        for (char i = 'A'; i <= 'E'; i++) {
            if (!passengers.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private char getNext(char building) {
        if (building == 'E') {
            return 'A';
        } else {
            return (char) ((int) building + 1);
        }
    }

    private char getFront(char building) {
        if (building == 'A') {
            return 'E';
        } else {
            return (char) ((int) building - 1);
        }
    }

    private char getTop() {
        return getNext(getNext(building));
    }

    private char getBottom() {
        return getFront(getFront(building));
    }

    public boolean canAchieve(char building) {
        return canAchieve.contains(building);
    }
}
