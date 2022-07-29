// import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.concurrent.ConcurrentHashMap;

public class Schedule implements Runnable {
    private final RequestQueue waitQueue;
    private final ConcurrentHashMap<Character, Building> buildingHashMap;
    // private ArrayList<Building> buildingQueue;

    Schedule(RequestQueue waitQueue, ConcurrentHashMap<Character, Building> buildingHashMap) {
        this.waitQueue = waitQueue;
        //this.buildingQueue = buildingQueue;
        this.buildingHashMap = buildingHashMap;
    }

    @Override
    public void run() {
        // TimableOutput.println("Schedule Thread Start!");
        while (true) {
            if (waitQueue.isEmpty() && waitQueue.isEnd()) {
                for (char c :
                        buildingHashMap.keySet()) {
                    buildingHashMap.get(c).setEnd(true);
                }
                break;
            }
            PersonRequest personRequest = null;
            try {
                personRequest = waitQueue.getPersonRequest();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (personRequest == null) {
                continue;
            }
            buildingHashMap.get(personRequest.getFromBuilding()).addRequest(personRequest);
        }
        // TimableOutput.println("Schedule Thread Close!");
    }
}
