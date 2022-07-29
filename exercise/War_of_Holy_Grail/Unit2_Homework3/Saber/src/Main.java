import java.util.ArrayList;

import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Requestqueue waitQueue = new Requestqueue();
        Requestqueue passengers = new Requestqueue();
        ArrayList<Elevator> elevators = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Elevator elevator = new Elevator(waitQueue,
                    passengers, i, i - 1, new Requestqueue(), 8, 0.6);
            elevators.add(elevator);
            elevator.start();
        }
        Elevator elevator = new Elevatorcross(waitQueue,
                passengers, 6, 5, new Requestqueue(), 8, 0.6, 31);
        elevators.add(elevator);
        elevator.start();
        Schedule schedule = new Schedule(waitQueue, elevators, passengers);
        schedule.start();
        Input in = new Input(waitQueue, passengers);
        in.start();
    }
}
