import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class BuildingElevator extends Thread {
    private final int id;
    private int capacity;
    private int floor;
    private final char building;
    private boolean up;
    private final BuildingRequestTable buildingRequestTable;
    private final HashMap<Integer, ArrayList<Passenger>> passengers;
    private final int speed;
    private final int maxCapacity;

    @Override
    public void run() {
        while (true) {
            if (buildingRequestTable.isEnd() && !hasPassengers()
                    && PassengerCount.getInstance().over()) {
                //System.out.println("Ele" + id + " end");
                return;
            }
            if (buildingRequestTable.isAllEmpty() && !hasPassengers()) {
                synchronized (buildingRequestTable) {
                    try {
                        buildingRequestTable.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!passengers.get(floor).isEmpty()) {
                try {
                    openDoor();
                    passengerOut();
                    closeDoor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int nextFloor = destination();
            if (nextFloor == floor) {
                if (turn()) {
                    nextFloor = destination();
                }
            }

            if (buildingRequestTable.havePassenger(floor, up) && capacity < maxCapacity) {
                try {
                    openDoor();
                    passengerIn();
                    closeDoor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (nextFloor != floor) {
                try {
                    moveTo(nextFloor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public BuildingElevator(char b, int idPara, int s, int mc) {
        id = idPara;
        capacity = 0;
        floor = 1;
        building = b;
        up = true;
        buildingRequestTable = RequestTables.getBuildingRequestTable(building);
        passengers = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            passengers.put(i, new ArrayList<>());
        }
        speed = s;
        maxCapacity = mc;
    }

    private int destination() {
        int dest = floor;
        if (hasPassengers()) {
            if (up) {
                for (int i = 10; i > floor; i--) {
                    if (!passengers.get(i).isEmpty() || !buildingRequestTable.isEmpty(i)) {
                        dest = i;
                        break;
                    }
                }
            } else {
                for (int i = 1; i < floor; i++) {
                    if (!passengers.get(i).isEmpty() || !buildingRequestTable.isEmpty(i)) {
                        dest = i;
                        break;
                    }
                }
            }
        } else {
            if (up) {
                for (int i = 10; i > floor; i--) {
                    if (!buildingRequestTable.isEmpty(i)) {
                        dest = i;
                        break;
                    }
                }
            } else {
                for (int i = 1; i < floor; i++) {
                    if (!buildingRequestTable.isEmpty(i)) {
                        dest = i;
                        break;
                    }
                }
            }
        }
        return dest;
    }

    private boolean turn() {
        boolean change = true;
        if (up) {
            for (int i = floor + 1; i <= 10; i++) {
                if (!passengers.get(i).isEmpty() || !buildingRequestTable.isEmpty(i)) {
                    change = false;
                }
            }
        } else {
            for (int i = floor - 1; i >= 1; i--) {
                if (!passengers.get(i).isEmpty() || !buildingRequestTable.isEmpty(i)) {
                    change = false;
                }
            }
        }
        if (change) {
            up = !up;
        }
        return change;
    }

    private void moveTo(int destination) throws Exception {
        if (destination == floor) {
            return;
        }
        while (floor != destination) {
            if (up) {
                floor++;
            } else {
                floor--;
            }
            sleep(speed);
            Output.println("ARRIVE-" + building + '-' + floor + '-' + id);
            if (!passengers.get(floor).isEmpty()) {
                openDoor();
                passengerOut();
                closeDoor();
            }
            if (buildingRequestTable.havePassenger(floor, up)) {
                if (capacity < maxCapacity) {
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
        for (Passenger passenger : passengers.get(floor)) {
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
        passengers.put(floor, new ArrayList<>());
    }

    private void passengerIn() {
        while (capacity < maxCapacity && buildingRequestTable.havePassenger(floor, up)) {
            Passenger passenger = buildingRequestTable.deQueue(floor, up);
            if (passenger != null) {
                if (passenger.achieve()) {
                    continue;
                }
                PersonRequest request = passenger.firstRequest();
                Output.println("IN-" + request.getPersonId()
                        + '-' + building + '-' + floor + '-' + id);
                passengers.get(request.getToFloor()).add(passenger);
                capacity++;
            }
        }
    }

    private boolean hasPassengers() {
        for (int i = 1; i <= 10; i++) {
            if (!passengers.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}

