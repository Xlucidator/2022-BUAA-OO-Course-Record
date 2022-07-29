import com.oocourse.elevator3.PersonRequest;

public interface Policy {
    void adjust();

    void getOut(ElevatorStatus status);

    void getIn(ElevatorStatus status);

    DistributedRequest distribute(PersonRequest request);
}
