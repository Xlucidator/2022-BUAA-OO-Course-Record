import com.oocourse.elevator3.PersonRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestTables {
    private static HashMap<Character, BuildingRequestTable> buildingRequestTables;
    private static HashMap<Integer, FloorRequestTable> floorRequestTables;

    public static void init() {
        buildingRequestTables = new HashMap<>();
        floorRequestTables = new HashMap<>();
        for (char c = 'A'; c <= 'E'; c++) {
            BuildingRequestTable rt = new BuildingRequestTable();
            buildingRequestTables.put(c, rt);
        }
        for (int i = 0; i <= 10; i++) {
            FloorRequestTable rt = new FloorRequestTable();
            floorRequestTables.put(i, rt);
        }
    }

    public static BuildingRequestTable getBuildingRequestTable(char building) {
        return buildingRequestTables.get(building);
    }

    public static FloorRequestTable getFloorRequestTable(int floor) {
        return floorRequestTables.get(floor);
    }

    public static void setEnd() {
        for (Map.Entry<Character, BuildingRequestTable> entry : buildingRequestTables.entrySet()) {
            entry.getValue().setInputEndTrue();
        }
        for (Map.Entry<Integer, FloorRequestTable> entry : floorRequestTables.entrySet()) {
            entry.getValue().setInputEndTrue();
        }
    }

    public static void addPassenger(Passenger passenger) {
        PersonRequest request = passenger.firstRequest();
        if (request.getFromFloor() == request.getToFloor()) {
            floorRequestTables.get(request.getFromFloor()).addPassenger(passenger);
        } else {
            buildingRequestTables.get(request.getFromBuilding()).addPassenger(passenger);
        }
    }
}
