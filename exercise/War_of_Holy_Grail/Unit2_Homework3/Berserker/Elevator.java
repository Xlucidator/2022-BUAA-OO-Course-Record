import java.util.Objects;

public class Elevator {
    private final ElevatorStatus status;
    private final ElevatorOperator operator;

    public Elevator(String id, boolean isHorizontal, Building building, int space, long speed,
                    int info) {
        status = new ElevatorStatus(id, isHorizontal, this, building, space, info);
        operator = new ElevatorOperator(this, speed);
        operator.start();
    }

    public ElevatorStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Elevator elevator = (Elevator) o;
        return status.getId().equals(elevator.getStatus().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, operator);
    }

    public long evaluate() {
        return (status.getSpace() - status.getPersons().size()) * operator.getSpeed();
    }
}
