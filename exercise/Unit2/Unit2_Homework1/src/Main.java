import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        RequestQueue waitQueue = new RequestQueue();
        ArrayList<RequestQueue> processQueues = new ArrayList<>();

        /* ======== InputThread Start ======== */
        InputThread inputThread = new InputThread(waitQueue);
        inputThread.start();    // START

        /* ======= ProcessThread Start ======= */
        for (int i = 1; i <= 5; i++) {
            RequestQueue processQueue = new RequestQueue();
            processQueues.add(processQueue);

            ProcessThread processThread = new ProcessThread(processQueue, new Elevator(i));
            processThread.start();  // START
        }

        /* ======= ScheduleThread Start ====== */
        ScheduleThread scheduleThread = new ScheduleThread(waitQueue, processQueues);
        scheduleThread.start(); // START
    }
}
