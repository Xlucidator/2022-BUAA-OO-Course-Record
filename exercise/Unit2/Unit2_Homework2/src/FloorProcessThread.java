import com.oocourse.elevator2.PersonRequest;

public class FloorProcessThread extends Thread {
    private final PersonRequestQueue processQueue;
    private final int floorNo;
    private final ElevatorThreadQueue elevators;

    public FloorProcessThread(PersonRequestQueue processQueue, int floorNo,
                              ElevatorThreadQueue elevators) {
        this.processQueue = processQueue;
        this.floorNo = floorNo;
        this.elevators = elevators;
    }

    @Override
    public void run() {
        //System.out.println("FloorProcessThread" + floorNo + " Start");
        elevators.checkElevator();
        if (elevators.isEmpty()) {
            return;
        }

        int cnt = 0;
        while (true) {
            //System.out.println("FloorProcessThread" + floorNo + " Begin");
            if (processQueue.isEnd() && processQueue.isEmpty()) {
                //System.out.println("FloorProcessThread" + floorNo + " End");
                for (int i = 0; i < elevators.size(); ++i) {
                    elevators.get(i).getTargetQueue().setEnd(true);
                }
                return;
            }

            PersonRequest req = processQueue.getOneRequest();
            if (req == null) {
                continue;
            }
            elevators.get(cnt % elevators.size()).getTargetQueue().addRequest(req);

            ++ cnt;
        }
    }
}
