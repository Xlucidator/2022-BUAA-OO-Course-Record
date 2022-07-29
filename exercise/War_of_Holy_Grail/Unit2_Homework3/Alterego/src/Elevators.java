import com.oocourse.elevator3.ElevatorRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Elevators {
    private static HashMap<Character, ArrayList<BuildingElevator>> buildingElevators;
    private static HashMap<Integer, ArrayList<FloorElevator>> floorElevators;

    private Elevators() {
    }

    public static void init() {
        buildingElevators = new HashMap<>();
        for (char c = 'A'; c <= 'E'; c++) {
            buildingElevators.put(c, new ArrayList<>());
            buildingElevators.get(c).add(
                    new BuildingElevator(c, c - 'A' + 1, 600, 8));
        }
        floorElevators = new HashMap<>();
        for (int i = 0; i <= 10; i++) {
            floorElevators.put(i, new ArrayList<>());
        }
        floorElevators.get(1).add(new FloorElevator(1, 6, 600, 8, 31));
        runElevators();
    }

    private static void runElevators() {
        for (char c = 'A'; c <= 'E'; c++) {
            for (BuildingElevator be : buildingElevators.get(c)) {
                be.start();
            }
        }
        for (int i = 0; i <= 10; i++) {
            for (FloorElevator fe : floorElevators.get(i)) {
                fe.start();
            }
        }
    }

    public static void addElevator(ElevatorRequest request) {
        String type = request.getType();
        int id = request.getElevatorId();
        double s = request.getSpeed();
        int speed = s == 0.2 ? 200 :
                s == 0.4 ? 400 : 600;
        if (type.equals("building")) {
            char building = request.getBuilding();
            BuildingElevator e = new BuildingElevator(building, id, speed, request.getCapacity());
            buildingElevators.get(building).add(e);
            e.start();
        } else {
            int floor = request.getFloor();
            FloorElevator e = new FloorElevator(request.getFloor(), request.getElevatorId(), speed,
                    request.getCapacity(), request.getSwitchInfo());
            floorElevators.get(floor).add(e);
            e.start();
        }
    }

    public static boolean canTrans(int floor, char fromBuilding, char toBuilding) {
        if (floorElevators.get(floor).isEmpty()) {
            return false;
        }
        boolean ret = false;
        for (FloorElevator e : floorElevators.get(floor)) {
            if (e.canAchieve(fromBuilding) && e.canAchieve(toBuilding)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public static int transFloor(int fromFloor, int toFloor, char fromBuilding, char toBuilding) {
        int ret = 1;
        int cost = Math.abs(fromFloor - 1) + Math.abs(toFloor - 1);
        for (int i = 1; i <= 10; i++) {
            if (canTrans(i, fromBuilding, toBuilding)) {
                //if (((i >> (fromBuilding - 'A')) & 1) + ((i >> (toBuilding - 'A')) & 1) == 2) {
                int tempCost = Math.abs(fromFloor - i) + Math.abs(toFloor - i);
                if (cost > tempCost) {
                    ret = i;
                    cost = tempCost;
                } else if (cost == tempCost) {
                    if (i >= fromFloor && i <= toFloor) {
                        ret = i;
                    }
                }
                //}
            }
        }
        return ret;
    }
}
