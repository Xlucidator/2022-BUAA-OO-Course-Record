import java.util.ArrayList;
import java.util.List;

public class GreedyLoopPolicy extends OrderlyDistributePolicy implements Policy {
    private final Building building;
    private int numOfElevator = 0;

    public GreedyLoopPolicy(Building building) {
        super(building);
        this.building = building;
    }

    @Override
    public void adjust() {
        if (building.getElevators().size() != numOfElevator) {
            ++numOfElevator;
            //redistribute();
        }
        for (Elevator elevator : building.getElevators()) {
            ElevatorStatus status = elevator.getStatus();
            int cur = status.getPos();
            int destination = 0;
            for (int i = 0; i <= Building.getNumOfBuildings() / 2; ++i) {
                int[] pos = new int[]{cur - i, cur + i};
                boolean out = false;
                if (pos[0] < 1) {
                    pos[0] += Building.getNumOfBuildings();
                }
                if (pos[1] > Building.getNumOfBuildings()) {
                    pos[1] -= Building.getNumOfBuildings();
                }
                for (int j = 0; j < 2; ++j) {
                    int p = pos[j];
                    if (status.getPersons().size() < status.getSpace() &&
                            building.isWaiting(p, status.getId())) {
                        destination = p;
                        out = true;
                        break;
                    }
                    boolean oout = false;
                    for (DistributedRequest person : status.getPersons()) {
                        if (person.getTarget() == p) {
                            destination = p;
                            out = true;
                            oout = true;
                            break;
                        }
                    }
                    if (oout) { break; }
                }
                if (out) { break; }
            }
            status.setDestination(destination);
        }
    }

    @Override
    public void getOut(ElevatorStatus status) {
        List<DistributedRequest> toBeRemoved = new ArrayList<>();
        for (DistributedRequest person : status.getPersons()) {
            if (person.getTarget() == status.getPos()) {
                toBeRemoved.add(person);
            }
        }
        for (DistributedRequest person : toBeRemoved) {
            status.out(person);
        }
    }

    @Override
    public void getIn(ElevatorStatus status) {
        List<DistributedRequest> toBeRemoved = new ArrayList<>();
        for (DistributedRequest request : building.getRequestMap().get(status.getPos())) {
            if (status.getPersons().size() >= status.getSpace()) { break; }
            if (request.getElevatorId().equals(status.getId())) {
                status.in(request);
                toBeRemoved.add(request);
            }
        }
        for (DistributedRequest request : toBeRemoved) {
            building.removeRequest(status.getPos(), request);
        }
    }
}
