import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScanningPolicy extends OrderlyDistributePolicy implements Policy {
    private final Building building;
    private final Map<Elevator, Integer> directions = new HashMap<>();
    private int numOfElevators = 0;

    public ScanningPolicy(Building building) {
        super(building);
        this.building = building;
    }

    @Override
    public void adjust() {
        if (building.getElevators().size() != numOfElevators) {
            directions.put(building.getElevators().get(numOfElevators++), 0);
            //redistribute();
        }
        for (Elevator elevator : building.getElevators()) {
            ElevatorStatus status = elevator.getStatus();
            int floor = status.getPos();
            int direction = directions.get(elevator);
            if (direction == 0) {
                DistributedRequest request = null;
                for (DistributedRequest d : building.getRequestList()) {
                    if (d.getElevatorId().equals(status.getId())) {
                        request = d;
                        break;
                    }
                }
                if (request == null) {
                    status.setDestination(0);
                    continue;
                }
                PersonRequest personRequest = request.getRequest();
                int target = request.getTarget();
                if (personRequest.getFromFloor() == floor) {
                    status.setDestination(floor);
                    direction = (target > floor) ? 1 : -1;
                    directions.put(elevator, direction);
                    continue;
                }
                direction = (personRequest.getFromFloor() > floor) ? 1 : -1;
                directions.put(elevator, direction);
            }
            int destination = 0;
            int found = find(status, floor, direction);
            if (found == 0) {
                direction = -direction;
                directions.put(elevator, direction);
                found = find(status, floor, direction);
                if (found == 0) {
                    direction = 0;
                    directions.put(elevator, direction);
                } else {
                    destination = found;
                }
            } else {
                destination = found;
            }
            status.setDestination(destination);
        }
    }

    private int find(ElevatorStatus status, int floor, int direction) {
        int rst = 0;
        int i = floor;
        // "Waiting" means the furthest position where at least one person is waiting to go to
        // the opposite direction of the elevator. When no passenger is getting out in the current
        // direction and no person going to the current direction is waiting in that direction,
        // the elevator should pick up the person at "waiting".
        int waiting = 0;
        // Iterates the floors in the current direction. Sets the destination to the nearest floor
        // where a passenger gets out or a person going to the same direction gets in. Out of the
        // cases above, tries to get to the "waiting" floor or return 0.
        while ((direction == 1) ? (i <= Building.getMaxFloor()) : (i >= 1)) {
            boolean out = false;
            if (building.isWaiting(i, status.getId()) &&
                    status.getPersons().size() < status.getSpace()) {
                for (DistributedRequest request : building.getRequestMap().get(i)) {
                    if (request.getElevatorId().equals(status.getId())) {
                        int to = request.getTarget();
                        if ((direction == 1) ? (to > i) : (to < i)) {
                            rst = i;
                            out = true;
                            break;
                        }
                    }
                }
                if (out) { break; }
                if (i != floor) { waiting = i; }
            }
            for (DistributedRequest person : status.getPersons()) {
                if (person.getTarget() == i) {
                    rst = i;
                    out = true;
                    break;
                }
            }
            if (out) { break; }
            i = (direction == 1) ? (i + 1) : (i - 1);
        }
        if (rst == 0 && waiting != 0) { rst = waiting; }
        return rst;
    }

    @Override
    // Lets all the waiting persons whose destinations are in the same direction of the elevator
    // get in.
    public void getIn(ElevatorStatus status) {
        int floor = status.getPos();
        ArrayList<DistributedRequest> toBeRemoved = new ArrayList<>();
        for (DistributedRequest request : building.getRequestMap().get(floor)) {
            if (status.getPersons().size() >= status.getSpace()) { break; }
            if (request.getElevatorId().equals(status.getId())) {
                int to = request.getTarget();
                if ((directions.get(status.getElevator()) == 1) ? (to > floor) : (to < floor)) {
                    status.in(request);
                    toBeRemoved.add(request);
                }
            }
        }
        for (DistributedRequest request : toBeRemoved) {
            building.removeRequest(floor, request);
        }
    }

    @Override
    // Lets all the passengers whose destination is this floor get out.
    public void getOut(ElevatorStatus status) {
        int floor = status.getPos();
        ArrayList<DistributedRequest> toBeRemoved = new ArrayList<>();
        for (DistributedRequest person : status.getPersons()) {
            if (person.getTarget() == floor) {
                toBeRemoved.add(person);
            }
        }
        for (DistributedRequest person : toBeRemoved) {
            status.out(person);
        }
    }
}
