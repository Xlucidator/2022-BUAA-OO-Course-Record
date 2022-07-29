import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        // please MUST initialize start timestamp at the beginning
        TimableOutput.initStartTimestamp();
        RequestQueue waitQueue = new RequestQueue();
        ArrayList<RequestQueue> processList = new ArrayList<>();
        ArrayList<Elevator> elevators = new ArrayList<>();
        int eleNum = 5;
        for (int i = 0; i < eleNum; i++) {
            RequestQueue processingQueue = new RequestQueue();
            processList.add(processingQueue);
            Elevator elevator = new Elevator(i + 1, processingQueue);
            elevators.add(elevator);
            elevator.start();
        }

        Scheduler scheduler = new Scheduler(waitQueue, processList, elevators);
        scheduler.start();

        InputThread inputThread = new InputThread(waitQueue);
        inputThread.start();
    }
}
