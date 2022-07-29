import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class ScheduleThread extends Thread {
    private final RequestQueue waitQueue;
    private final ArrayList<RequestQueue> processQueues;

    public ScheduleThread(RequestQueue waitQueue, ArrayList<RequestQueue> processQueues) {
        this.waitQueue = waitQueue;
        this.processQueues = processQueues;
    }

    @Override
    public void run() {
        while (true) {
            if (waitQueue.isEmpty() && waitQueue.isEnd()) {
                processQueues.forEach(req -> req.setEnd(true));
                //System.out.println("ScheduleThread End");
                return; // Thread End
            }
            PersonRequest request = waitQueue.getOneRequest();
            if (request == null) {
                continue;
            }
            processQueues.get(request.getFromBuilding() - 65).addRequest(request);
        }
    }
}
