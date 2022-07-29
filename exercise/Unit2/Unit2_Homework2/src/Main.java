import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        RequestQueue waitQueue = new RequestQueue();
        ArrayList<PersonRequestQueue> processQueues = new ArrayList<>();
        ArrayList<ElevatorThreadQueue> elevatorQueues = new ArrayList<>();

        /* ======== InputThread Start ======== */
        InputThread inputThread = new InputThread(waitQueue);
        inputThread.start();    // START

        /* ======= ProcessThread Start ======= */
        for (int i = 1; i <= 15; ++i) {
            // Dynamic processQueue
            PersonRequestQueue processQueue = new PersonRequestQueue();
            processQueues.add(processQueue);

            // Dynamic elevators
            ElevatorThreadQueue elevators = new ElevatorThreadQueue();
            if (i <= 5) {
                elevators.addElevator(new ElevatorThread(i, "building"));
            }
            elevatorQueues.add(elevators);

            if (i <= 5) {
                BuildingProcessThread processThread =
                        new BuildingProcessThread(processQueue, (char)(64 + i), elevators);
                processThread.start();  // START
            } else {
                FloorProcessThread processThread =
                        new FloorProcessThread(processQueue, i - 5, elevators);
                processThread.start();  // START
            }
        }

        /* ======= ScheduleThread Start ====== */
        ScheduleThread scheduleThread = new ScheduleThread(waitQueue,
                processQueues, elevatorQueues);
        scheduleThread.start(); // START
    }
}
