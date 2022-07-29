import java.util.ArrayList;

public class Elevator extends Thread {
    private final int num;
    private final int limit;
    private final int openTime;
    private final int closeTime;
    private final int moveTime;
    private ArrayList<Person> pas;
    private final PersonQueue waitQueue;

    public Elevator(PersonQueue waitQueue, int id, int moveTime, int limit) {
        this.openTime = 200;
        this.closeTime = 200;
        this.moveTime = moveTime;
        this.limit = limit;
        this.waitQueue = waitQueue;
        this.num = id;
        this.pas = new ArrayList<>();
    }

    public void run() {
    }

    public int getNum() {
        return num;
    }

    public int getLimit() {
        return limit;
    }

    public int getOpenTime() {
        return openTime;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public int getMoveTime() {
        return moveTime;
    }

    public ArrayList<Person> getPas() {
        return pas;
    }

    public PersonQueue getWaitQueue() {
        return waitQueue;
    }
}
