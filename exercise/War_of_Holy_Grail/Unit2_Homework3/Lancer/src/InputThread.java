import com.oocourse.TimableOutput;
import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InputThread extends Thread {
    private final Map<Character, FloorPersonQueue> floorQueue = new ConcurrentHashMap<>();
    private final Map<Character, List<Elevator>> floorElevator = new ConcurrentHashMap<>();
    private final Map<Integer, BuildingPersonQueue> buildingQueue = new ConcurrentHashMap<>();
    private final Map<Integer, List<Elevator>> buildingElevator = new ConcurrentHashMap<>();
    private boolean end = false;

    private void init() {
        for (int i = 5; i >= 1; i--) {
            addFloorElevator(i, (char) ('A' + i - 1), 0.6, 8);
        }
        addBuildingElevator(6, 1, 0.6, 8, 31);
        TimableOutput.initStartTimestamp();
    }

    private void addFloorElevator(int id, char building, double moveTime, int maxPerson) {
        FloorPersonQueue floorPersonQueue =
                floorQueue.computeIfAbsent(building, o -> new FloorPersonQueue());
        FloorPolicy floorPolicy = new FloorPolicy(this, floorPersonQueue, maxPerson);
        Elevator elevator = new Elevator(id, floorPolicy, moveTime, building, 1);
        floorElevator.computeIfAbsent(building, o -> new ArrayList<>()).add(0, elevator);
        elevator.start();
    }

    private void addBuildingElevator(int id, int floor, double moveTime, int maxPerson,
                                     int switchInfo) {
        BuildingPersonQueue buildingPersonQueue =
                buildingQueue.computeIfAbsent(floor, o -> new BuildingPersonQueue());
        BuildingPolicy buildingPolicy =
                new BuildingPolicy(this, buildingPersonQueue, maxPerson, switchInfo);
        Elevator elevator = new Elevator(id, buildingPolicy, moveTime, 'A', floor);
        buildingElevator.computeIfAbsent(floor, o -> new ArrayList<>()).add(0, elevator);
        elevator.start();
    }

    public void call(List<Person> outPerson) {
        boolean notTransfer = true;
        if (outPerson != null) {
            for (Person person : outPerson) {
                if (call(person)) {
                    notTransfer = false;
                }
            }
        }
        if (notTransfer) {
            synchronized (this) {
                if (end) {
                    for (List<Elevator> elevators : floorElevator.values()) {
                        for (Elevator elevator : elevators) {
                            if (elevator.isRun()) { //有一个没在等待程序就还没结束
                                return;
                            }
                        }
                    }
                    for (List<Elevator> elevators : buildingElevator.values()) {
                        for (Elevator elevator : elevators) {
                            if (elevator.isRun()) {
                                return;
                            }
                        }
                    }
                    floorElevator.values()
                            .forEach(elevators -> elevators.forEach(Elevator::end));
                    buildingElevator.values()
                            .forEach(elevators -> elevators.forEach(Elevator::end));
                }
            }
        }
    }

    private boolean call(Person person) {
        PersonRequest personRequest = person.getNewRequest();
        if (personRequest == null) {
            return false;
        }
        if (personRequest.getFromBuilding() != personRequest.getToBuilding()) {
            buildingQueue.get(personRequest.getFromFloor()).add(person);
            Elevator temp = buildingElevator.get(personRequest.getFromFloor()).remove(0);
            temp.call();
            buildingElevator.get(personRequest.getFromFloor()).add(temp);
        } else {
            floorQueue.get(personRequest.getFromBuilding()).add(person);
            Elevator temp = floorElevator.get(personRequest.getFromBuilding()).remove(0);
            temp.call();
            floorElevator.get(personRequest.getFromBuilding()).add(temp);
        }
        return true;
    }

    private void call(PersonRequest personRequest) {
        Person person;
        if (personRequest.getFromBuilding() == personRequest.getToBuilding()) {
            person = new Person(personRequest);
        } else {
            int transferFloor = 1;
            for (int i = 2; i <= 10; i++) {
                List<Elevator> elevators = buildingElevator.get(i);
                if (elevators == null || elevators.isEmpty()) {
                    continue;
                }
                for (Elevator elevator : elevators) {
                    if (elevator.canStay(personRequest.getFromBuilding(), i)
                            && elevator.canStay(personRequest.getToBuilding(), i)) {
                        transferFloor = getTransferFloor(personRequest, transferFloor, i);
                        break;
                    }
                }
            }
            person = new Person(personRequest, transferFloor);
        }
        call(person);
    }

    private int getTransferFloor(PersonRequest personRequest,
                                 int transferFloor1, int transferFloor2) {
        int i1 = Math.abs(personRequest.getFromFloor() - transferFloor1)
                + Math.abs(personRequest.getToFloor() - transferFloor1);
        int i2 = Math.abs(personRequest.getFromFloor() - transferFloor2)
                + Math.abs(personRequest.getToFloor() - transferFloor2);
        return i1 < i2 ? transferFloor1 : transferFloor2;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        init();
        while (true) {
            Request request = elevatorInput.nextRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                break;
            }
            // a new valid request
            if (request instanceof PersonRequest) {
                // a PersonRequest
                // your code here
                PersonRequest personRequest = (PersonRequest) request;
                call(personRequest);
            } else if (request instanceof ElevatorRequest) {
                // an ElevatorRequest
                // your code here
                ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                if ("building".equals(elevatorRequest.getType())) {
                    addFloorElevator(
                            elevatorRequest.getElevatorId(),
                            elevatorRequest.getBuilding(),
                            elevatorRequest.getSpeed(),
                            elevatorRequest.getCapacity());
                } else {
                    addBuildingElevator(
                            elevatorRequest.getElevatorId(),
                            elevatorRequest.getFloor(),
                            elevatorRequest.getSpeed(),
                            elevatorRequest.getCapacity(),
                            elevatorRequest.getSwitchInfo());
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            end = true;
        }
    }
}
