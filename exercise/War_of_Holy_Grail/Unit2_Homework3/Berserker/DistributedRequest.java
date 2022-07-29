import com.oocourse.elevator3.PersonRequest;

public class DistributedRequest {
    private String elevatorId;
    private final PersonRequest request;
    private int target;

    public DistributedRequest(PersonRequest request, String id, int target) {
        this.request = request;
        this.elevatorId = id;
        this.target = target;
    }

    public PersonRequest getRequest() {
        return request;
    }

    public String getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorId = elevatorId;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
