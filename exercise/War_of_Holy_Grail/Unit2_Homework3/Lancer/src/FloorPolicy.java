import com.oocourse.elevator3.PersonRequest;

public class FloorPolicy extends Policy {

    public FloorPolicy(InputThread inputThread, PersonQueue queue, int max) {
        super(inputThread, queue, max);
    }

    @Override
    protected int toDirection(Person person) {
        PersonRequest request = person.getNewRequest();
        return Integer.compare(request.getToFloor(), request.getFromFloor());
    }

    @Override
    protected int fromDirection(Person person, Elevator elevator) {
        return Integer.compare(person.getNewRequest().getFromFloor(), elevator.getFloor());
    }

    @Override
    protected Direction createDirection(int direction) {
        return new Direction(0, direction);
    }

    @Override
    protected int getKey(Elevator elevator) {
        return elevator.getFloor();
    }

    @Override
    protected int getFrom(Person person) {
        return person.getNewRequest().getFromFloor();
    }

    @Override
    protected int getTo(Person person) {
        return person.getNewRequest().getToFloor();
    }

    @Override
    public boolean canStay(char building, int floor) {
        return true;
    }
}
