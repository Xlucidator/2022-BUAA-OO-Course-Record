import java.util.ArrayList;

public class ElevatorThreadQueue {
    private final ArrayList<ElevatorThread> elevators;
    private boolean isEnd;

    public ElevatorThreadQueue() {
        this.elevators = new ArrayList<>();
    }

    public synchronized void addElevator(ElevatorThread elevatorThread) {
        elevators.add(elevatorThread);
        elevatorThread.start();
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return elevators.isEmpty();
    }

    public synchronized void checkElevator() {
        if (elevators.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ElevatorThread get(int i) {
        return elevators.get(i);
    }

    public synchronized int size() {
        return elevators.size();
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

}
