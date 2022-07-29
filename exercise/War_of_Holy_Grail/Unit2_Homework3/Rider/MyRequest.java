public class MyRequest {
    private final int personId;
    private final int fromFloor;
    private final int toFloor;
    private final char fromBuilding;
    private final char toBuilding;
    private boolean in;
    private int tempFloor;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setTempFloor(int tempFloor) {
        this.tempFloor = tempFloor;
    }

    public MyRequest(int personId, int fromFloor, int toFloor,
                     char fromBuilding, char toBuilding) {
        this.personId = personId;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.fromBuilding = fromBuilding;
        this.toBuilding = toBuilding;
        this.in = false;
        this.flag = 0;
    }

    public boolean isIn() {
        return !in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public char getToBuilding() {
        if ((flag >> 2) == 1) {
            return fromBuilding;
        }
        return toBuilding;
    }

    @Override
    public String toString() {
        return "MyRequest{" +
                "personId=" + personId +
                ", fromFloor=" + fromFloor +
                ", toFloor=" + toFloor +
                ", fromBuilding=" + fromBuilding +
                ", toBuilding=" + toBuilding +
                ", in=" + in +
                ", tempFloor=" + tempFloor +
                ", flag=" + flag +
                '}';
    }

    public char getFromBuilding() {
        if (flag == 1) {
            return toBuilding;
        }
        return fromBuilding;
    }

    public int getToFloor() {
        if (flag == 1 || flag == 4 || flag == 0) {
            return toFloor;
        } else {
            return tempFloor;
        }
    }

    public int getFromFloor() {
        if (((flag >> 2) & 1) == 1 || flag == 0) {
            return fromFloor;
        } else {
            return tempFloor;
        }
    }

    public int getPersonId() {
        return personId;
    }

    public String getDirection() {
        if (this.getFromFloor() > this.getToFloor()) {
            return "down";
        } else if (this.getFromFloor() < this.getToFloor()) {
            return "up";
        } else {
            return "building";
        }
    }
}