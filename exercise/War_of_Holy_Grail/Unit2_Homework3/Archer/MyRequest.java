public class MyRequest {
    private final int fromFloor;
    private final char fromBuilding;
    private final int toFloor;
    private final char toBuilding;
    private final boolean direction;    //顺时针为正，向上为正
    private final int type;     //1表示竖向，2表示横向

    public MyRequest(int fromFloor, int toFloor, char fromBuilding, char toBuilding) {
        this.fromFloor = fromFloor;
        this.fromBuilding = fromBuilding;
        this.toFloor = toFloor;
        this.toBuilding = toBuilding;
        if (this.fromFloor == this.toFloor) {
            type = 2;
            direction = true;
        } else {
            type = 1;
            direction = (this.fromFloor < this.toFloor);
        }
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public char getFromBuilding() {
        return fromBuilding;
    }

    public int getToFloor() {
        return toFloor;
    }

    public char getToBuilding() {
        return toBuilding;
    }

    public boolean getDirection() {
        return direction;
    }

    public int getType() {
        return type;
    }
}