import com.oocourse.elevator2.PersonRequest;

public class ElevatorThread extends Thread {
    private final PersonRequestQueue targetQueue;
    private final int type;
    private final Elevator elevator;

    public ElevatorThread(int id, String type) {
        this.targetQueue = new PersonRequestQueue();
        this.type = type.equals("building") ? 1 : 0;    // building:1 ; floor:0
        this.elevator = new Elevator(id, this.type, (char)(id + 64), 1);
    }

    public ElevatorThread(int id, String type, char buildNo, int floorNo) {
        this.targetQueue = new PersonRequestQueue();
        this.type = type.equals("building") ? 1 : 0;
        this.elevator = new Elevator(id, this.type, buildNo, floorNo);
    }

    public PersonRequestQueue getTargetQueue() {
        return this.targetQueue;
    }

    @Override
    public void run() {
        while (true) {
            if (targetQueue.isEmpty() && targetQueue.isEnd() && elevator.isEmpty()) {
                //System.out.println("ElevatorThread" + elevator.getId() + " End");
                return; // Thread End
            }

            if (type == 1) {
                dealWithBuilding();
            } else {
                dealWithFloor();
            }
        }
    }

    public void dealWithFloor() {
        PersonRequest mainReq;
        if (elevator.isEmpty()) {
            mainReq = targetQueue.getRequest(0);
            if (mainReq == null) {
                return;
            }
            arriveTo(mainReq.getFromBuilding());
            if (targetQueue.containRequest(mainReq)) {
                return;
            }
            arriveTo(mainReq.getToBuilding());
        } else {
            mainReq = elevator.getRequest(0);
            arriveTo(mainReq.getToBuilding());
        }
    }

    public void dealWithBuilding() {
        PersonRequest mainReq;
        if (elevator.isEmpty()) {
            mainReq = targetQueue.getFarthestRequest(elevator.getFloor());
            //System.out.println("mainReq:" + mainReq);
            if (mainReq == null) {
                return;
            }
            arriveTo(mainReq.getFromFloor());
            if (targetQueue.containRequest(mainReq)) {
                return;
            }
            arriveTo(mainReq.getToFloor());
        } else {
            mainReq = elevator.getRequest(0);
            arriveTo(mainReq.getToFloor());
        }
    }

    public int getReqOrient(PersonRequest req) {
        if (type == 1) {
            return req.getFromFloor() < req.getToFloor() ? 1 : -1;
        } else {
            int dis = (req.getToBuilding() - req.getFromBuilding() + 5) % 5;
            return dis < 3 ? 1 : -1;
        }
    }

    /*================= whether or not to get in =====================*/
    public boolean hasIn(Integer orient) {  // orient can be null
        if (elevator.isFull()) {
            return false;
        }
        for (int i = 0; i < targetQueue.size(); ++i) {
            PersonRequest req = targetQueue.getRequest(i);
            if (req.getFromFloor() == elevator.getFloor() &&
                    req.getFromBuilding() == elevator.getBuilding() &&
                    (orient == null || getReqOrient(req) == orient)) {
                return true;
            }
        }
        return false;
    }

    public void getIn(Integer orient) {
        for (int i = 0; i < targetQueue.size(); ++i) {
            if (elevator.isFull()) {
                return;
            }
            PersonRequest req = targetQueue.getRequest(i);
            if (req.getFromFloor() == elevator.getFloor() &&
                    req.getFromBuilding() == elevator.getBuilding() &&
                    (orient == null || getReqOrient(req) == orient)) {
                targetQueue.delRequest(i--);
                elevator.in(req);
            }
        }
    }

    /*======= Do GetIn/Out after Each Movement, until 'to' floor =========*/
    public void arriveTo(int to) {
        int curPos = to < 65 ? elevator.getFloor() : elevator.getBuilding();

        int step;
        if (to < 65) {
            step = curPos < to ? 1 : -1;
        } else {
            int dis = (5 + to - curPos) % 5;
            step = dis < 3 ? 1 : -1;
        }

        while (curPos != to) {
            executeInOut(step);
            elevator.move(step);
            curPos = to < 65 ? elevator.getFloor() : elevator.getBuilding();
        }
        executeInOut(null);
    }

    public void executeInOut(Integer inOrient) {
        if (elevator.hasOut() || hasIn(inOrient)) {
            elevator.open();
            elevator.getOut();
            sleepForInOut(Elevator.OPEN_TIME + Elevator.CLOSE_TIME);
            getIn(inOrient);
            elevator.close();
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
