import com.oocourse.TimableOutput;
import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ElevatorStatus {
    enum State {
        opened,
        closed,
        opening,
        closing,
        posGoing,
        negGoing;
    }

    private final String id;
    private final boolean isHorizontal;
    private final Elevator elevator;
    private final Building building;
    private final int space;
    private int pos = 1;
    private State state = State.closed;
    private int destination = 0;
    private final List<DistributedRequest> persons = new LinkedList<>();
    private boolean turnedDown = false;
    private final int info;

    public ElevatorStatus(String id, boolean isHorizontal, Elevator elevator, Building building,
                          int space, int info) {
        this.id = id;
        this.isHorizontal = isHorizontal;
        this.elevator = elevator;
        this.building = building;
        this.space = space;
        this.info = info;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public String getId() {
        return id;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public Building getBuilding() {
        return building;
    }

    public int getSpace() {
        return space;
    }

    public int getPos() {
        return pos;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<DistributedRequest> getPersons() {
        return persons;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public boolean isTurnedDown() {
        return turnedDown;
    }

    public void setTurnedDown(boolean turnedDown) {
        this.turnedDown = turnedDown;
    }

    public int getInfo() {
        return info;
    }

    public List<DistributedRequest> needOut() {
        List<DistributedRequest> rst = new ArrayList<>();
        for (DistributedRequest person : persons) {
            if (person.getTarget() == pos) {
                rst.add(person);
            }
        }
        return rst;
    }

    private String basicInformation() {
        char building = (char) ('A' + (isHorizontal ? pos : this.building.getPos()) - 1);
        int floor = isHorizontal ? this.building.getPos() : pos;
        return String.format("%c-%d-%s", building, floor, id);
    }

    public void out(DistributedRequest person) {
        synchronized (TimableOutput.class) {
            TimableOutput.println(
                    String.format(
                            "OUT-%d-%s", person.getRequest().getPersonId(), basicInformation()));
        }
        persons.remove(person);
        char building = (char) ('A' + (isHorizontal ? pos : this.building.getPos()) - 1);
        int floor = isHorizontal ? this.building.getPos() : pos;
        if (building != person.getRequest().getToBuilding() ||
                floor != person.getRequest().getToFloor()) {
            PersonRequest newRequest = new PersonRequest(floor,
                    person.getRequest().getToFloor(),
                    building,
                    person.getRequest().getToBuilding(),
                    person.getRequest().getPersonId());
            synchronized (MultiBuilding.getUnhandled()) {
                MultiBuilding.getUnhandled().add(newRequest);
                MultiBuilding.getUnhandled().notifyAll();
            }
        }
    }

    public void in(DistributedRequest person) {
        synchronized (TimableOutput.class) {
            TimableOutput.println(
                    String.format(
                            "IN-%d-%s", person.getRequest().getPersonId(), basicInformation()));
        }
        persons.add(person);
    }

    public void open() {
        synchronized (TimableOutput.class) {
            TimableOutput.println(String.format("OPEN-%s", basicInformation()));
        }
    }

    public void close() {
        synchronized (TimableOutput.class) {
            TimableOutput.println(String.format("CLOSE-%s", basicInformation()));
        }
    }

    private void arrive() {
        synchronized (TimableOutput.class) {
            TimableOutput.println(String.format("ARRIVE-%s", basicInformation()));
        }
    }

    public void goPos() {
        ++pos;
        if (isHorizontal && pos > Building.getNumOfBuildings()) {
            pos = 1;
        }
        arrive();
    }

    public void goNeg() {
        --pos;
        if (isHorizontal && pos < 1) {
            pos = Building.getNumOfBuildings();
        }
        arrive();
    }
}
