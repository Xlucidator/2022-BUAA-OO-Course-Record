import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public interface ElevatorPattern {
    Elevator.State getState(Elevator.State state, int floor);

    ArrayList<PersonRequest> getPeopleIn(int floor);

    int getOpenTime(int floor);
}
