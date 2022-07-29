import com.oocourse.TimableOutput;

import java.util.concurrent.ConcurrentHashMap;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        // waitQueue : a sharedSource
        RequestQueue waitQueue = new RequestQueue();
        ConcurrentHashMap<Character, Building> buildingHashMap = new ConcurrentHashMap<>();
        // ArrayList<Building> buildingQueue = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Building building = new Building((char) ('A' + i));
            building.addElevator(new Elevator(i + 1, (char) ('A' + i)));
            buildingHashMap.put(building.getId(), building);
        }
        // TimableOutput.initStartTimestamp();
        // producer for waitQueue
        Runnable inputThread = new InputThread(waitQueue);
        Thread input = new Thread(inputThread);
        input.start();
        // consumer for waitQueue
        Runnable schedule = new Schedule(waitQueue, buildingHashMap);
        Thread scheduleThread = new Thread(schedule);
        scheduleThread.start();
    }
}
