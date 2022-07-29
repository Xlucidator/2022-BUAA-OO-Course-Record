import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class NormalPattern implements ElevatorPattern {
    private PersonRequest mainRequest;
    private Elevator.State insideState;
    private WaitQueue waitQueue;
    private PersonList personList;
    private final int maxFloor;
    private final int minFloor;

    public NormalPattern(WaitQueue waitQueue, PersonList personList, int maxFloor, int minFloor) {
        mainRequest = null;
        insideState = Elevator.State.UP;
        this.waitQueue = waitQueue;
        this.personList = personList;
        this.maxFloor = maxFloor;
        this.minFloor = minFloor;
    }

    @Override
    public Elevator.State getState(Elevator.State state, int floor) {
        switch (state) {
            case UP:
                return dealUp(floor);
            case DOWN:
                return dealDown(floor);
            case WAIT:
                return dealWait(floor);
            case IN_AND_OUT:
                return dealInAndOut();
            default:
                break;
        }
        return Elevator.State.WAIT;
    }

    private Elevator.State dealUp(int floor) {
        if (personList.isEmpty()) {
            if (mainRequest == null) {
                return Elevator.State.WAIT;
            }
            if (floor != mainRequest.getFromFloor()) {
                return Elevator.State.UP;
            }
            return Elevator.State.WAIT;
        }
        if (personList.hasPersonOut(floor)) {
            return Elevator.State.IN_AND_OUT;
        } else if (personList.isFull()) {
            return Elevator.State.UP;
        } else if (waitQueue.hasPersonIn(floor, insideState)) {
            return Elevator.State.IN_AND_OUT;
        } else {
            return Elevator.State.UP;
        }
    }

    private Elevator.State dealDown(int floor) {
        if (personList.isEmpty()) {
            if (mainRequest == null) {
                return Elevator.State.WAIT;
            }
            if (floor != mainRequest.getFromFloor()) {
                return Elevator.State.DOWN;
            }
            return Elevator.State.WAIT;
        }
        if (personList.hasPersonOut(floor)) {
            return Elevator.State.IN_AND_OUT;
        } else if (personList.isFull()) {
            return Elevator.State.DOWN;
        } else if (waitQueue.hasPersonIn(floor, insideState)) {
            return Elevator.State.IN_AND_OUT;
        } else {
            return Elevator.State.DOWN;
        }
    }

    private Elevator.State dealInAndOut() {
        if (!personList.contains(mainRequest)) {
            mainRequest = null;
        }
        if (personList.isEmpty()) {
            return Elevator.State.WAIT;
        }
        if (insideState.equals(Elevator.State.UP)) {
            return Elevator.State.UP;
        } else if (insideState.equals(Elevator.State.DOWN)) {
            return Elevator.State.DOWN;
        }
        return Elevator.State.WAIT;
    }

    private Elevator.State dealWait(int floor) {
        mainRequest = getMainRequest(floor);
        if (mainRequest == null) {
            return Elevator.State.WAIT;
        } else if (mainRequest.getFromFloor() == floor) {
            if (mainRequest.getToFloor() > floor) {
                insideState = Elevator.State.UP;
            } else {
                insideState = Elevator.State.DOWN;
            }
            return Elevator.State.IN_AND_OUT;
        } else if (mainRequest.getFromFloor() > floor) {
            insideState = Elevator.State.UP;
            return Elevator.State.UP;
        } else {
            insideState = Elevator.State.DOWN;
            return Elevator.State.DOWN;
        }
    }

    private PersonRequest getMainRequest(int floor) {
        ArrayList<PersonRequest> temp = waitQueue.getRequests();
        PersonRequest upRequest = null;
        PersonRequest downRequest = null;
        int upCnt = maxFloor + 1;
        int downCnt = maxFloor + 1;
        for (PersonRequest person : temp) {
            if (person.getFromFloor() == floor) {
                return person;
            } else if (person.getFromFloor() > floor) {
                if (person.getFromFloor() - floor < upCnt) {
                    upCnt = person.getFromFloor() - floor;
                    upRequest = person;
                }
            } else {
                if (floor - person.getFromFloor() < downCnt) {
                    downCnt = floor - person.getFromFloor();
                    downRequest = person;
                }
            }
        }
        if (upRequest == null && downRequest == null) {
            return null;
        }
        if (downCnt < upCnt) {
            return downRequest;
        } else {
            return upRequest;
        }
    }

    @Override
    public ArrayList<PersonRequest> getPeopleIn(int floor) {
        ArrayList<PersonRequest> temp = new ArrayList<>();
        int size = personList.size();
        if (insideState.equals(Elevator.State.UP)) {
            for (PersonRequest person : waitQueue.getRequests()) {
                if (person.getFromFloor() == floor
                        && person.getToFloor() > floor
                        && size < personList.getMaxSize()) {
                    temp.add(person);
                    size++;
                }
            }
        } else if (insideState.equals(Elevator.State.DOWN)) {
            for (PersonRequest person : waitQueue.getRequests()) {
                if (person.getFromFloor() == floor
                        && person.getToFloor() < floor
                        && size < personList.getMaxSize()) {
                    temp.add(person);
                    size++;
                }
            }
        }
        return temp;
    }

    @Override
    public int getOpenTime(int floor) {
        return 50;
    }
}
