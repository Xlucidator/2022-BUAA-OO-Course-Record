import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class WaitQueue {
    private ArrayList<PersonRequest> requests;
    private boolean isEnd;

    public WaitQueue() {
        requests = new ArrayList<>();
        isEnd = false;
    }

    public void close() {
        isEnd = true;
    }

    public void addRequest(PersonRequest request) {
        requests.add(request);
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }

    public boolean isEnd() {
        return isEnd;
    }

    public ArrayList<PersonRequest> getRequests() {
        return requests;
    }

    public void removeRequest(PersonRequest request) {
        requests.remove(request);
    }

    public void clear() {
        requests.clear();
    }

    public boolean hasPersonIn(int floor) {
        for (PersonRequest person : requests) {
            if (person.getFromFloor() == floor) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPersonIn(int floor, Elevator.State state) {
        if (state.equals(Elevator.State.UP)) {
            for (PersonRequest person : requests) {
                if (person.getFromFloor() == floor && person.getToFloor() > floor) {
                    return true;
                }
            }
            return false;
        }
        if (state.equals(Elevator.State.DOWN)) {
            for (PersonRequest person : requests) {
                if (person.getFromFloor() == floor && person.getToFloor() < floor) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
