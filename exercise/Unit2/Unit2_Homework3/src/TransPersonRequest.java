import com.oocourse.elevator3.PersonRequest;

public class TransPersonRequest extends PersonRequest {
    private final int throughFloor;
    private int curStage;   // 1: vertical-start 2:horizontal 3:vertical-end

    public TransPersonRequest(PersonRequest req, int through, int stage) {
        super(req.getFromFloor(), req.getToFloor(), req.getFromBuilding(), req.getToBuilding(),
                req.getPersonId());
        this.throughFloor = through;
        this.curStage = stage;
    }

    public int getThroughFloor() {
        return throughFloor;
    }

    public int getCurStage() {
        return curStage;
    }

    public void setCurStage(int stage) {
        this.curStage = stage;
    }

    @Override
    public int getFromFloor() {
        return curStage == 1 ? super.getFromFloor() : throughFloor;
    }

    @Override
    public int getToFloor() {
        return curStage == 3 ? super.getToFloor() : throughFloor;
    }

    @Override
    public char getFromBuilding() {
        return curStage == 3 ? super.getToBuilding() : super.getFromBuilding();
    }

    @Override
    public char getToBuilding() {
        return curStage == 1 ? super.getFromBuilding() : super.getToBuilding();
    }
}
