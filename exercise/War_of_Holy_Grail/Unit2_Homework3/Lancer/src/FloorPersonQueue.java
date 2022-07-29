/**
 * 等待人员队列
 */
public class FloorPersonQueue extends PersonQueue {
    @Override
    protected boolean toUp(Person person) {
        return person.getNewRequest().getToFloor() > person.getNewRequest().getFromFloor();
    }

    @Override
    protected int getKey(Person person) {
        return person.getNewRequest().getFromFloor();
    }
}
