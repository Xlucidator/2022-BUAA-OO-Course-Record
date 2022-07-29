import com.oocourse.elevator2.PersonRequest;

public class BuildingProcessThread extends Thread {
    private final PersonRequestQueue processQueue;
    private final char buildNo;
    private final ElevatorThreadQueue elevators;

    public BuildingProcessThread(PersonRequestQueue processQueue, char buildNo,
                                 ElevatorThreadQueue elevators) {
        this.processQueue = processQueue;
        this.buildNo = buildNo;
        this.elevators = elevators;
    }

    @Override
    public void run() {
        int t = 0;
        while (true) {
            if (processQueue.isEnd() && processQueue.isEmpty()) {
                //System.out.println("BuildingProcessThread" + buildNo + " End");
                for (int i = 0; i < elevators.size(); ++i) {
                    elevators.get(i).getTargetQueue().setEnd(true);
                }
                return;
            }

            PersonRequestQueue toAdd = elevators.get(t % elevators.size()).getTargetQueue();
            PersonRequest selectReq = processQueue.getOneRequest();
            if (selectReq == null) {
                continue;
            }
            int cnt = 1;
            for (int i = 0; i < processQueue.size(); ++i) {
                PersonRequest req = processQueue.getRequest(i);
                if (req == null) {
                    continue;
                }
                if (getMoveOrient(req) != getMoveOrient(selectReq)) {
                    continue;
                }
                if (selectReq.getFromFloor() == req.getFromFloor() ||
                        getRelevantOrient(selectReq, req) == getMoveOrient(selectReq)) {
                    toAdd.addRequest(req);
                    processQueue.delRequest(i--);
                    ++ cnt;
                }
                if (cnt > 2) {
                    break;
                }
            }
            toAdd.addRequest(selectReq);

            ++ t;
        }
    }

    // up:1 down:0
    public int getMoveOrient(PersonRequest req) {
        return req.getFromFloor() < req.getToFloor() ? 1 : 0;
    }

    public int getRelevantOrient(PersonRequest req1, PersonRequest req2) {
        return req1.getFromFloor() < req2.getFromFloor() ? 1 : 0;
    }
}
