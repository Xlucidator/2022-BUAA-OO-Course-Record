import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

public class Controller { //控制器的单例模式
    private static final Controller CONTROLLER = new Controller();
    private PersonQueue[] waitQueues;
    private boolean endTag;

    public static Controller getInstance() {
        return CONTROLLER;
    }

    public void initial(PersonQueue[] waitQueues) {
        this.waitQueues = waitQueues;
        endTag = false;
    }

    public void parse(Request request) {
        if (request instanceof PersonRequest) {
            Person person = new Person((PersonRequest) request);
            addRequest(person);
        } else if (request instanceof ElevatorRequest) {
            Factory.getFactory().addElevator((ElevatorRequest) request);
        }
    }

    public void addRequest(Person person) { //增添新的请求，并唤醒所有等待线程
        MyRequest tmp = person.getRequests().get(0);
        if (tmp.getType() == 1) {
            waitQueues[tmp.getFromBuilding() - 'A'].addRequest(person);
        } else {
            waitQueues[tmp.getFromFloor() + 4].addRequest(person);
        }
    }

    public synchronized void setEndTag() { //标记所有任务完成，唤醒所有线程
        endTag = true;
        notifyAllWaitQueue();
    }

    public synchronized boolean getEndTag() {
        return endTag;
    }

    private void notifyAllWaitQueue() {
        for (int i = 0; i < waitQueues.length; i++) {
            synchronized (waitQueues[i]) {
                waitQueues[i].setEnd(true);
                waitQueues[i].notifyAll();
            }
        }
    }
}