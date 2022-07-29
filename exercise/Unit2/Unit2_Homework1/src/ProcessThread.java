import com.oocourse.elevator1.PersonRequest;

public class ProcessThread extends Thread {
    private final RequestQueue processQueue;
    private final Elevator elevator;

    public ProcessThread(RequestQueue processQueue, Elevator elevator) {
        this.processQueue = processQueue;
        this.elevator = elevator;
    }

    @Override
    public void run() {
        while (true) {
            if (processQueue.isEmpty() && processQueue.isEnd() && elevator.isEmpty()) {
                //System.out.println("ProcessThread" + elevator.getId() + " End");
                return; // Thread End
            }
            dealWithAlsBeta();
        }
    }

    public int getOrient(PersonRequest req) {
        return req.getFromFloor() < req.getToFloor() ? 1 : -1;
    }

    public void dealWithAlsBeta() {
        PersonRequest mainReq;
        if (elevator.isEmpty()) {
            mainReq = processQueue.getNearestRequest(elevator.getLocation());
            if (mainReq == null) {
                return;
            }
            arriveTo(mainReq.getFromFloor());
            if (processQueue.containRequest(mainReq)) {
                return;
            }
            arriveTo(mainReq.getToFloor());
        } else {
            mainReq = elevator.getRequest(0);
            arriveTo(mainReq.getToFloor());
        }
    }

    /*================= whether or not to get in =====================*/
    public boolean hasIn(Integer orient) {  // orient can be null
        if (elevator.isFull()) {
            return false;
        }
        for (int i = 0; i < processQueue.size(); ++i) {
            PersonRequest req = processQueue.getRequest(i);
            if (req.getFromFloor() == elevator.getLocation() &&
                    (orient == null || getOrient(req) == orient)) {
                return true;
            }
        }
        return false;
    }

    public void getIn(Integer orient) {
        for (int i = 0; i < processQueue.size(); ++i) {
            if (elevator.isFull()) {
                return;
            }
            PersonRequest req = processQueue.getRequest(i);
            if (req.getFromFloor() == elevator.getLocation() &&
                    (orient == null || getOrient(req) == orient)) {
                processQueue.delRequest(i--);
                elevator.in(req);
            }
        }
    }

    /*======= Do GetIn/Out after Each Movement, until 'to' floor =========*/
    public void arriveTo(int to) {
        int curPos = elevator.getLocation();
        if (curPos == to) {
            if (elevator.hasOut() || hasIn(null)) {
                elevator.open();
                elevator.getOut();
                sleepForInOut(Elevator.OPEN_TIME + Elevator.CLOSE_TIME);
                getIn(null);
                elevator.close();
            }
            return;
        }
        int step = curPos < to ? 1 : -1;
        while (curPos != to) {
            elevator.move(step);
            curPos += step;
            if (elevator.hasOut() || hasIn(step)) {
                elevator.open();
                elevator.getOut();
                getIn(step);
                sleepForInOut(Elevator.OPEN_TIME + Elevator.CLOSE_TIME);
                elevator.close();
            }
        }
    }

    public void sleepForInOut(long time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
