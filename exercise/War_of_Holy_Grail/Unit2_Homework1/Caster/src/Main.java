import com.oocourse.TimableOutput;
import com.oocourse.elevator1.ElevatorInput;

public class Main {
    public static void main(String[] argv) {
        TimableOutput.initStartTimestamp();
        ElevatorInput input = new ElevatorInput(System.in);
        WaitQueue totalQueue = new WaitQueue();
        ElevatorQueues elevatorQueues = new ElevatorQueues();
        for (int i = 1; i <= 5; i++) {
            WaitQueue elevatorQueue = new WaitQueue();
            Elevator elevator = new Elevator(i, (char) ('A' + i - 1), elevatorQueue);
            elevatorQueues.put((char) ('A' + i - 1), elevatorQueue);
            elevator.start();
        }
        Scheduler scheduler = new Scheduler(elevatorQueues, totalQueue);
        scheduler.start();
        InputThread inputThread = new InputThread(totalQueue, input);
        inputThread.start();
    }
}
