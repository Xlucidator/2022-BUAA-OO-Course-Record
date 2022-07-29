import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MultiBuilding {
    private static final int MAX_FLOOR = 10;
    private static final int NUM_OF_BUILDING = 5;
    private static final Handler HANDLER = new Handler();
    private static final MultiBuilding MULTI_BUILDING = new MultiBuilding();

    private static class Handler extends Thread {
        private boolean turnedDown = false;
        private final List<PersonRequest> unhandled = new LinkedList<>();

        @Override
        public void run() {
            while (!turnedDown) {
                List<PersonRequest> temp = new ArrayList<>();
                synchronized (unhandled) {
                    try {
                        unhandled.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    while (!unhandled.isEmpty()) {
                        PersonRequest request = unhandled.get(0);
                        unhandled.remove(0);
                        temp.add(request);
                    }
                }
                for (PersonRequest request : temp) {
                    Building building = RequestController.findBuilding(request);
                    building.addRequest(request);
                }
                temp.clear();
            }
        }

        public void turnDown() {
            turnedDown = true;
        }
    }

    private final List<Building> buildings = new ArrayList<>();
    private final List<Building> floors = new ArrayList<>();

    public MultiBuilding() {
        for (int i = 1; i <= NUM_OF_BUILDING; ++i) {
            Building building = new Building(false, i);
            building.addElevator(String.valueOf(i), 8, 600, 0b1111111111);
            buildings.add(building);
        }
        for (int i = 1; i <= MAX_FLOOR; ++i) {
            floors.add(new Building(true, i));
        }
        floors.get(0).addElevator(String.valueOf(6), 8, 600, 0b11111);
        HANDLER.start();
    }

    public static MultiBuilding getMultiBuilding() {
        return MULTI_BUILDING;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public List<Building> getFloors() {
        return floors;
    }

    public static List<PersonRequest> getUnhandled() {
        return HANDLER.unhandled;
    }

    public boolean hasFloorElevator(int floor, char from, char to) {
        boolean rst = false;
        for (Elevator elevator : floors.get(floor - 1).getElevators()) {
            int info = elevator.getStatus().getInfo();
            if (((info >> (from - 'A')) & 1) + ((info >> (to - 'A')) & 1) == 2) {
                rst = true;
                break;
            }
        }
        return rst;
    }

    public void turnDownHandler() {
        synchronized (HANDLER.unhandled) {
            HANDLER.turnDown();
            HANDLER.unhandled.notifyAll();
        }
    }
}
