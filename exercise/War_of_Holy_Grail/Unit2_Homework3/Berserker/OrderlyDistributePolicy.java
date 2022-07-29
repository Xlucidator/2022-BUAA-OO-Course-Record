import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderlyDistributePolicy {
    private final Building building;
    private long max;

    public OrderlyDistributePolicy(Building building) {
        this.building = building;
    }

    // Returns a DistributedRequest of "request" with an elevator id in order.
    public DistributedRequest distribute(PersonRequest request) {
        char fromBuilding = request.getFromBuilding();
        char toBuilding = request.getToBuilding();
        int target = 0;
        int floor = request.getFromFloor();
        boolean horizontal;
        if (request.getFromBuilding() == request.getToBuilding()) {
            target = request.getToFloor();
            horizontal = false;
        } else if (MultiBuilding.getMultiBuilding().hasFloorElevator(
                floor, fromBuilding, toBuilding)) {
            target = request.getToBuilding() - 'A' + 1;
            horizontal = true;
        } else {
            int to = request.getToFloor();
            int low = Math.min(floor, to);
            int high = Math.max(floor, to);
            max = Long.MIN_VALUE;
            for (int i = low; i <= high; ++i) {
                if (findFloor(i, fromBuilding, toBuilding)) {
                    target = i;
                }
            }
            if (target == 0) {
                for (int i = 1; i <= Math.max(low - 1, 10 - high); ++i) {
                    if (low - i >= 1 && findFloor(low - i, fromBuilding, toBuilding)) {
                        target = low - i;
                    }
                    if (high + i <= 10 && findFloor(high + i, fromBuilding, toBuilding)) {
                        target = high + i;
                    }
                }
            }
            horizontal = false;
        }
        String id = null;
        max = Long.MIN_VALUE;
        for (Elevator elevator : building.getElevators()) {
            int info = elevator.getStatus().getInfo();
            int from = horizontal ? request.getFromBuilding() - 'A' : request.getFromFloor() - 1;
            if (((info >> from) & 1) + ((info >> (target - 1)) & 1) == 2) {
                if (elevator.evaluate() > max) {
                    max = elevator.evaluate();
                    id = elevator.getStatus().getId();
                }
            }
        }
        return new DistributedRequest(request, id, target);
    }

    // Redistributes all DistributedRequests of the building in order.
    // Called when a new elevator is added to the building.
    protected void redistribute() {
        Map<Integer, List<DistributedRequest>> requests = new HashMap<>(building.getRequestMap());
        Map<Integer, List<DistributedRequest>> temp = new HashMap<>();
        for (Integer i : requests.keySet()) {
            temp.put(i, new ArrayList<>());
            for (DistributedRequest request : requests.get(i)) {
                temp.get(i).add(request);
            }
        }
        for (Integer i : temp.keySet()) {
            for (DistributedRequest request : temp.get(i)) {
                building.removeRequest(i, request);
            }
        }
        for (Integer i : temp.keySet()) {
            synchronized (MultiBuilding.getUnhandled()) {
                for (DistributedRequest request : temp.get(i)) {
                    MultiBuilding.getUnhandled().add(request.getRequest());
                    MultiBuilding.getUnhandled().notifyAll();
                }
            }
        }

        /*iterator = building.getElevators().iterator();
        for (DistributedRequest request : building.getRequestList()) {
            if (!iterator.hasNext()) {
                iterator = building.getElevators().iterator();
            }
            String id = iterator.next().getStatus().getId();
            request.setElevatorId(id);
        }*/
    }

    private boolean findFloor(int i, char from, char to) {
        boolean rst;
        Building building = MultiBuilding.getMultiBuilding().getFloors().get(i - 1);
        synchronized (building) {
            if (MultiBuilding.getMultiBuilding().hasFloorElevator(i, from, to) &&
                    building.evaluate(from - 'A' + 1, to - 'A' + 1) > max) {
                max = building.evaluate(from - 'A' + 1, to - 'A' + 1);
                rst = true;
            } else {
                rst = false;
            }
        }
        return rst;
    }
}
