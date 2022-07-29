import java.util.HashMap;

public class ElevatorQueues {
    private HashMap<Character, WaitQueue> elevatorMap;

    public ElevatorQueues() {
        elevatorMap = new HashMap<>();
    }

    public void put(char buildingSign, WaitQueue elevatorQueue) {
        elevatorMap.put(buildingSign, elevatorQueue);
    }

    public WaitQueue get(char buildingSign) {
        return elevatorMap.get(buildingSign);
    }

    public void close() {
        for (Character ch : elevatorMap.keySet()) {
            WaitQueue elevatorQueue = elevatorMap.get(ch);
            synchronized (elevatorQueue) {
                elevatorQueue.close();
                elevatorQueue.notifyAll();
            }
        }
    }
}
