import java.util.ArrayList;

public class Schedule {
    private ArrayList<ElevatorBuilding> elevatorBuildings;
    private ArrayList<ElevatorFloor> elevatorFloors;
    private static final Schedule SCHEDULE = new Schedule();
    private boolean endTag;

    public void addBuilding(ElevatorBuilding elevatorBuilding) {
        this.elevatorBuildings.add(elevatorBuilding);
        elevatorBuilding.start();
    }

    public void addFloor(ElevatorFloor elevatorFloor) {
        this.elevatorFloors.add(elevatorFloor);
        elevatorFloor.start();
    }

    public static Schedule getInstance() {
        return SCHEDULE;
    }

    public void initial() {
        elevatorBuildings = new ArrayList<>();
        elevatorFloors = new ArrayList<>();
        endTag = false;

        for (int i = 0; i < 5; i++) {
            RequestQueue parallelQueue = new RequestQueue();
            ElevatorBuilding elevatorBuilding = new ElevatorBuilding(
                    parallelQueue, i, i + 1, 8, 0.6);
            elevatorBuildings.add(elevatorBuilding);
            elevatorBuilding.start();
        }

        RequestQueue parallelQueue = new RequestQueue();
        ElevatorFloor elevatorFloor = new ElevatorFloor(parallelQueue,
                1, 6, 8, 0.6, 31);
        elevatorFloors.add(elevatorFloor);
        elevatorFloor.start();
    }

    public void addRequest(MyRequest myRequest) {
        if (myRequest.getFlag() == 0) {
            setFlag(myRequest);
        }
        if (((myRequest.getFlag() >> 2) & 1) == 1) {
            putBuilding(myRequest);
        } else if (((myRequest.getFlag() >> 1) & 1) == 1) {
            putFloor(myRequest);
        } else if (((myRequest.getFlag()) & 1) == 1) {
            putBuilding(myRequest);
        }
    }

    private void putBuilding(MyRequest myRequest) {
        int min = 210;
        int id = 0;
        for (ElevatorBuilding elevatorBuilding : elevatorBuildings) {
            if (elevatorBuilding.getBuilding() == (int) myRequest.getToBuilding() - 65) {
                int size = elevatorBuilding.getProcessingQueue().size();
                if (min > size) {
                    min = size;
                    id = elevatorBuilding.getIdE();
                }
            }
        }
        for (ElevatorBuilding elevatorBuilding : elevatorBuildings) {
            if (elevatorBuilding.getIdE() == id) {
                elevatorBuilding.getProcessingQueue().addRequest(myRequest);
                break;
            }
        }
    }

    private void putFloor(MyRequest myRequest) {
        int min = 210;
        int id = 0;
        for (ElevatorFloor elevatorFloor : elevatorFloors) {
            if (myRequest.getFromFloor() == elevatorFloor.getFloor() &&
                    ((elevatorFloor.getSwitchInfo() >>
                            (myRequest.getToBuilding() - 'A')) & 1) == 1) {
                int size = elevatorFloor.getProcessingQueue().size();
                if (min > size) {
                    min = size;
                    id = elevatorFloor.getIdE();
                }
            }
        }
        for (ElevatorFloor elevatorFloor : elevatorFloors) {
            if (elevatorFloor.getIdE() == id) {
                elevatorFloor.getProcessingQueue().addRequest(myRequest);
                break;
            }
        }
    }

    private void notifyElevators() {
        for (ElevatorBuilding elevatorBuilding : elevatorBuildings) {
            synchronized (elevatorBuilding.getProcessingQueue()) {
                elevatorBuilding.getProcessingQueue().notifyAll();
            }
        }
        for (ElevatorFloor elevatorFloor : elevatorFloors) {
            synchronized (elevatorFloor.getProcessingQueue()) {
                elevatorFloor.getProcessingQueue().notifyAll();
            }
        }
    }

    public synchronized boolean isEndTag() {
        notifyAll();
        return endTag;
    }

    public synchronized void setEndTag() {
        this.endTag = true;
        notifyElevators();
    }

    private void setFlag(MyRequest myRequest) {
        if (myRequest.getFromBuilding() == myRequest.getToBuilding()) {
            myRequest.setFlag(4);
        } else {
            int minDis = Math.abs(myRequest.getFromFloor() - 1) +
                    Math.abs(myRequest.getToFloor() - 1);
            int tempFloor = 1;
            for (ElevatorFloor elevatorFloor : elevatorFloors) {
                if ((((elevatorFloor.getSwitchInfo() >>
                        (myRequest.getToBuilding() - 'A')) & 1) == 1) &&
                        ((elevatorFloor.getSwitchInfo() >>
                                (myRequest.getFromBuilding() - 'A')) & 1)  == 1) {
                    int way = Math.abs(myRequest.getFromFloor() - elevatorFloor.getFloor()) +
                            Math.abs(myRequest.getToFloor() - elevatorFloor.getFloor());
                    if (minDis > way) {
                        minDis = way;
                        tempFloor = elevatorFloor.getFloor();
                        myRequest.setTempFloor(tempFloor);
                    }
                }
            }
            if (tempFloor == 1) {
                myRequest.setTempFloor(1);
            }
            if (tempFloor == myRequest.getFromFloor()) {
                if (tempFloor == myRequest.getToFloor()) {
                    myRequest.setFlag(2);
                } else {
                    myRequest.setFlag(3);
                }
            } else {
                if (tempFloor == myRequest.getToFloor()) {
                    myRequest.setFlag(6);
                } else {
                    myRequest.setFlag(7);
                }
            }
        }
    }
}


