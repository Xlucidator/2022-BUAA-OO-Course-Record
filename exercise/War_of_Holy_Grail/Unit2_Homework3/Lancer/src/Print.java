import com.oocourse.TimableOutput;

public class Print {
    public static synchronized void printElevatorState(Elevator elevator, String state) {
        TimableOutput.println(String.format("%s-%s-%d-%d",
                state,
                elevator.getBuilding(),
                elevator.getFloor(),
                elevator.getId()));
    }

    public static synchronized void printPersonState(Person person, Elevator elevator, String state) {
        TimableOutput.println(String.format("%s-%s-%s-%d-%s",
                state,
                person.getId(),
                elevator.getBuilding(),
                elevator.getFloor(),
                elevator.getId()));
    }
}
