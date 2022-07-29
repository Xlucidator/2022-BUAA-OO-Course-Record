import com.oocourse.elevator1.PersonRequest;

public class Request extends PersonRequest {
    private boolean alreadyIn;

    public Request(int fromFloor, int toFloor, char fromBuilding, char toBuilding, int personId) {
        super(fromFloor, toFloor, fromBuilding, toBuilding, personId);
        alreadyIn = false;
    }

    public boolean isAlreadyIn() {
        return alreadyIn;
    }

    public void setAlreadyIn(boolean alreadyIn) {
        this.alreadyIn = alreadyIn;
    }

    public int getPurpose() {
        return alreadyIn ? this.getToFloor() : this.getFromFloor();
    }
}
