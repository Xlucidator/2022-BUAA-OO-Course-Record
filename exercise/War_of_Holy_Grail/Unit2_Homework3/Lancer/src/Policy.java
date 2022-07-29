import java.util.ArrayList;
import java.util.List;

public abstract class Policy {
    private final InputThread inputThread;
    private final PersonQueue queue;
    private final List<Person> inner = new ArrayList<>();
    private final int max;
    private int direction = 0;

    public Policy(InputThread inputThread, PersonQueue queue, int max) {
        this.inputThread = inputThread;
        this.queue = queue;
        this.max = max;
    }

    protected abstract int toDirection(Person person);

    protected abstract int fromDirection(Person person, Elevator elevator);

    protected abstract Direction createDirection(int direction);

    protected abstract int getKey(Elevator elevator);

    protected abstract int getFrom(Person person);

    protected abstract int getTo(Person person);

    public abstract boolean canStay(char building, int floor);

    public Direction getDirection(Elevator elevator) {
        if (inner.isEmpty()) {
            direction = 0;
        } else {
            //电梯内有人肯定是同向
            Person person = inner.get(0);
            direction = toDirection(person);
        }
        if (direction == 0) {
            //没人需要设置方向
            Person first = queue.getFirst();
            if (first != null) {
                direction = fromDirection(first, elevator); //接人
            }
        }
        return createDirection(direction);
    }

    public boolean needOpen(Elevator elevator) {
        if (!canStay(elevator.getBuilding(), elevator.getFloor())) {
            return false;
        }
        if (inner.stream().anyMatch(person -> getKey(elevator) == getTo(person))) {
            return true;
        }
        if (direction == 0) {
            Person first = queue.getFirst();
            return first != null && getKey(elevator) == getFrom(first);
        }
        return (direction > 0 && queue.hasWaitUp(getKey(elevator)))
                || (direction < 0 && queue.hasWaitDown(getKey(elevator)));
    }

    public List<Person> doAfterOpen(Elevator elevator) {
        boolean empty = inner.isEmpty();
        List<Person> outPerson = new ArrayList<>();
        inner.removeIf(person -> {
            if (getKey(elevator) == getTo(person)) {
                Print.printPersonState(person, elevator, "OUT");
                person.removeOldRequest();
                outPerson.add(person);
                return true;
            }
            return false;
        });
        if (empty) {
            //出人前 为空 必定是前往等待用户的地方
            Person first = queue.getFirst();
            if (first != null && getFrom(first) == getKey(elevator)) {
                direction = toDirection(first);
            }
        }
        List<Person> persons = direction > 0 ?
                queue.getUp(getKey(elevator), max - inner.size()) :
                queue.getDown(getKey(elevator), max - inner.size());
        for (Person person : persons) {
            Print.printPersonState(person, elevator, "IN");
            inner.add(person);
        }
        return outPerson;
    }

    public void doAfterClose(List<Person> outPerson) {
        inputThread.call(outPerson);
    }
}
