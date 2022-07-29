public class Const {
    public static final int OPEN_TIME = 200;
    public static final int CLOSE_TIME = 200;
    public static final int MOVE_ONE_FLOOR_TIME = 400;
    public static final int MAX_PERSON = 6;
    public static final int MAX_FLOOR = 10;

    enum MoveType {
        UP, DOWN, PAUSE, OPEN, CLOSE, OPEN_CLOSE,ERR
    }

    public static final int LAST_UP = 1;
    public static final int LAST_DOWN = 2;
}
