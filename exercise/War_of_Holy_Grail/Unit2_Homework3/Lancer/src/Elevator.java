import java.util.List;

/**
 * 电梯类
 */
public class Elevator {
    /**
     * 开门时间 ms
     */
    private static final long OPEN_TIME = 200;
    /**
     * 关门时间 ms
     */
    private static final long CLOSE_TIME = 200;

    private final int id; //编号
    private final Policy policy; //策略
    private final long moveTime; //策略
    private char building; //楼栋
    private int floor; //当前楼层
    private int state = 1; //状态 0wait 1运行 -1终止
    private final Thread workThread = new Thread(() -> {
        while (true) {
            try {
                if (!work()) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public Elevator(int id, Policy policy, double moveTime, char building, int floor) {
        this.id = id;
        this.policy = policy;
        this.moveTime = (long) (moveTime * 1000);
        this.building = building;
        this.floor = floor;
    }

    private boolean work() throws InterruptedException {
        Direction direction = policy.getDirection(this);
        if (!direction.notMove()) {
            int buildingMove = direction.getBuildingMove();
            int floorMove = direction.getFloorMove();
            if (buildingMove != 0) {
                building += buildingMove;
                if (building < 'A') {
                    building += 5;
                } else if (building > 'E') {
                    building -= 5;
                }
                Thread.sleep(moveTime * Math.abs(buildingMove));
            } else if (floorMove != 0) {
                floor += floorMove;
                Thread.sleep(moveTime * Math.abs(floorMove));
            }
            Print.printElevatorState(this, "ARRIVE");
        }
        List<Person> outPerson = null;
        if (policy.needOpen(this)) {
            Print.printElevatorState(this, "OPEN");
            Thread.sleep(OPEN_TIME);
            outPerson = policy.doAfterOpen(this);

            Thread.sleep(CLOSE_TIME);
            Print.printElevatorState(this, "CLOSE");
        }
        Direction direction2 = policy.getDirection(this);
        if (direction.notMove() && direction2.notMove()) {
            //可能等待状态后第一个人就在这层
            synchronized (this) {
                state = 0;
                policy.doAfterClose(outPerson);
                if (state == 0) {
                    try {
                        wait(); //电梯进入等待
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (state == -1) {
                    return false;
                }
            }
        } else {
            if (outPerson != null) {
                policy.doAfterClose(outPerson);
            }
        }
        return true;
    }

    public void start() {
        state = 1;
        workThread.start();
    }

    public synchronized void call() {
        state = 1;
        notify();
    }

    public synchronized void end() {
        state = -1;
        notify();
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized char getBuilding() {
        return building;
    }

    public synchronized int getFloor() {
        return floor;
    }

    public synchronized boolean isRun() {
        return state == 1;
    }

    public boolean canStay(char building, int floor) {
        return policy.canStay(building, floor);
    }
}
