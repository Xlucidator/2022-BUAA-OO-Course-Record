/**
 * 等待人员队列
 */
public class BuildingPersonQueue extends PersonQueue {

    @Override
    protected boolean toUp(Person person) {
        char from = person.getNewRequest().getFromBuilding();
        char to = person.getNewRequest().getToBuilding();
        int direction1 = Math.abs(to - from);
        if (direction1 < 3) {
            return to > from;
        } else {
            return to < from;
        }
    }

    @Override
    protected int getKey(Person person) {
        return person.getNewRequest().getFromBuilding();
    }
}
