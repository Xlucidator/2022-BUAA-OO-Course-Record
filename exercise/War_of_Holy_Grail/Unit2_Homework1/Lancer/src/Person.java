import com.oocourse.elevator1.PersonRequest;

public class Person {
    private int id;
    private char fromBuild;
    private char destBuild;
    private int fromFloor;
    private int destFloor;
    private boolean taken;
    private boolean finished;

    //constructor
    public Person(PersonRequest request) {
        id = request.getPersonId();
        fromBuild = request.getFromBuilding();
        destBuild = request.getToBuilding();
        fromFloor = request.getFromFloor();
        destFloor = request.getToFloor();
        taken = false;
        finished = false;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getDistance() {
        return Math.abs(fromFloor - destFloor);
    }

    public boolean goUp() {
        return destFloor > fromFloor;
    }

    public int getId() {
        return id;
    }

    public int getDestFloor() {
        return destFloor;
    }
}
