public class BuildingPolicy extends Policy {
    private final int[] switchInfo = new int[5];

    public BuildingPolicy(InputThread inputThread, PersonQueue queue, int max, int switchInfo) {
        super(inputThread, queue, max);
        int temp = switchInfo;
        for (int i = 0; i < this.switchInfo.length; i++) {
            this.switchInfo[i] = temp % 2;
            temp /= 2;
            if (temp < 1) {
                break;
            }
        }
    }

    @Override
    protected int toDirection(Person person) {
        return getDirection(person.getNewRequest().getToBuilding(),
                person.getNewRequest().getFromBuilding());
    }

    @Override
    protected int fromDirection(Person person, Elevator elevator) {
        return getDirection(person.getNewRequest().getFromBuilding(), elevator.getBuilding());
    }

    @Override
    protected Direction createDirection(int direction) {
        return new Direction(direction, 0);
    }

    private int getDirection(char to, char from) {
        int direction1 = Math.abs(to - from);
        if (direction1 < 3) {
            return (to < from) ? -1 : ((to == from) ? 0 : 1);
        } else {
            return (to < from) ? 1 : ((to == from) ? 0 : -1);
        }
    }

    @Override
    protected int getKey(Elevator elevator) {
        return elevator.getBuilding();
    }

    @Override
    protected int getFrom(Person person) {
        return person.getNewRequest().getFromBuilding();
    }

    @Override
    protected int getTo(Person person) {
        return person.getNewRequest().getToBuilding();
    }

    @Override
    public boolean canStay(char building, int floor) {
        return switchInfo[building - 'A'] == 1;
    }
}
