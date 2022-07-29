import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import static java.lang.Thread.sleep;

public class Elevator implements Runnable {
    private int id;
    private char building;
    private boolean full;
    private int numOfPerson;
    private RequestQueue waitQueue;
    private final RequestQueue requests;
    private int floor;
    private Const.MoveType moveType;
    private int direction;

    Elevator(int id, char building) {
        setId(id);
        setBuilding(building);
        setNumOfPerson(0);
        setFull(false);
        requests = new RequestQueue(Const.MAX_PERSON);
        waitQueue = new RequestQueue();
        floor = 1;
        direction = Const.LAST_DOWN;
        setMoveType(Const.MoveType.PAUSE);
    }

    public Const.MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(Const.MoveType moveType) {
        this.moveType = moveType;
    }

    public void setWaitQueue(RequestQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        // TimableOutput.println("Elevator " + id + " Start!");
        while (true) {
            Const.MoveType nextMoveType = null;
            try {
                nextMoveType = look();
                /*synchronized (TimableOutput.class) {
                    TimableOutput.println("Elevator " + id + " nextMoveType : " + nextMoveType);
                }*/
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (nextMoveType == Const.MoveType.UP) {
                    setMoveType(Const.MoveType.UP);
                    up();
                } else if (nextMoveType == Const.MoveType.DOWN) {
                    setMoveType(Const.MoveType.DOWN);
                    down();
                } else if (nextMoveType == Const.MoveType.OPEN) {
                    setMoveType(Const.MoveType.OPEN);
                    open();
                } else if (nextMoveType == Const.MoveType.CLOSE) {
                    setMoveType(Const.MoveType.CLOSE);
                    close();
                } else if (nextMoveType == Const.MoveType.PAUSE) {
                    setMoveType(Const.MoveType.PAUSE);
                    pause();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (waitQueue) {
                if (!waitQueue.isEmpty()) {
                    continue;
                }
                waitQueue.notifyAll();
            }

            synchronized (requests) {
                if (requests.isEnd() && requests.isEmpty() &&
                        (moveType == Const.MoveType.CLOSE || moveType == Const.MoveType.PAUSE)) {
                    // TimableOutput.println("Elevator " + id + " has no request!");
                    break;
                }
                requests.notifyAll();
            }
        }
    }

    public Const.MoveType look() throws InterruptedException {
        if (getMoveType() == Const.MoveType.PAUSE) {
            synchronized (waitQueue) {
                if (waitQueue.size() == 0) {
                    return Const.MoveType.PAUSE;
                } else {
                    PersonRequest request = waitQueue.getOnePersonRequest();
                    return (request.getFromFloor() > floor) ? Const.MoveType.UP :
                            (request.getFromFloor() == floor) ? Const.MoveType.OPEN :
                                    Const.MoveType.DOWN;
                }
            }
        } else if (getMoveType() == Const.MoveType.OPEN) {
            return Const.MoveType.CLOSE;
        } else if (getMoveType() == Const.MoveType.CLOSE && direction == Const.LAST_UP) {
            if (getMaxFloor() == 0) {
                return Const.MoveType.PAUSE;
            } else if (getMaxFloor() == floor) {
                return Const.MoveType.DOWN;
            } else if (getMaxFloor() < floor) {
                return Const.MoveType.DOWN;
            } else {
                return Const.MoveType.UP;
            }
        } else if (getMoveType() == Const.MoveType.CLOSE && direction == Const.LAST_DOWN) {
            if (getMinFloor() == (Const.MAX_FLOOR + 1)) {
                return Const.MoveType.PAUSE;
            } else if (getMinFloor() == floor) {
                return Const.MoveType.UP;
            } else if (getMinFloor() > floor) {
                return Const.MoveType.UP;
            } else {
                return Const.MoveType.DOWN;
            }
        } else if (getMoveType() == Const.MoveType.UP) {
            // TimableOutput.println("NowFloor:" + floor);
            return getTypeNextUp();
        } else if (getMoveType() == Const.MoveType.DOWN) {
            return getTypeNextDown();
        } else {
            return Const.MoveType.ERR;
        }
    }

    private Const.MoveType getTypeNextDown() {
        synchronized (waitQueue) {
            if (!requests.containsFloor(floor) && requests.isFull()) {
                waitQueue.notifyAll();
                return Const.MoveType.DOWN;
            }
            if (containsFloor(floor, Const.MoveType.DOWN)) {
                waitQueue.notifyAll();
                return Const.MoveType.OPEN;
            } else if (floor == getMinFloor() && containsFloor(floor, Const.MoveType.UP)) {
                waitQueue.notifyAll();
                return Const.MoveType.OPEN;
            } else if (floor == getMinFloor() && waitQueue.size() != 0) {
                waitQueue.notifyAll();
                return Const.MoveType.UP;
            } else if (floor == getMaxFloor() && waitQueue.size() == 0) {
                waitQueue.notifyAll();
                return Const.MoveType.PAUSE;
            } else {
                waitQueue.notifyAll();
                return Const.MoveType.DOWN;
            }
        }
    }

    private Const.MoveType getTypeNextUp() {
        synchronized (waitQueue) {
            if (!requests.containsFloor(floor) && requests.isFull()) {
                waitQueue.notifyAll();
                return Const.MoveType.UP;
            }
            if (containsFloor(floor, Const.MoveType.UP)) {
                waitQueue.notifyAll();
                return Const.MoveType.OPEN;
            } else if (floor == getMaxFloor() && containsFloor(floor, Const.MoveType.DOWN)) {
                waitQueue.notifyAll();
                return Const.MoveType.OPEN;
            } else if (floor == getMaxFloor() && waitQueue.size() != 0) {
                waitQueue.notifyAll();
                return Const.MoveType.DOWN;
            } else if (floor == getMaxFloor() && waitQueue.size() == 0) {
                waitQueue.notifyAll();
                return Const.MoveType.PAUSE;
            } else {
                waitQueue.notifyAll();
                return Const.MoveType.UP;
            }
        }
    }

    private int getMinFloor() {
        return Math.min(waitQueue.getMinFloor(), requests.getMinFloor());
    }

    public boolean containsFloor(int nowFloor, Const.MoveType moveType) {
        return requests.containsFloor(nowFloor) |
                waitQueue.containsFloor(nowFloor, moveType);
    }

    public int getMaxFloor() {
        /* TimableOutput.println("maxFloor :" +
                Math.max(waitQueue.getMaxFloor(), requests.getMaxFloor())); */
        return Math.max(waitQueue.getMaxFloor(), requests.getMaxFloor());
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public synchronized void up() throws InterruptedException {
        floor++;
        this.setDirection(Const.LAST_UP);
        // this.setMoveType(Const.MoveType.UP);
        sleep(Const.MOVE_ONE_FLOOR_TIME);
        synchronized (TimableOutput.class) {
            TimableOutput.println("ARRIVE-" +
                    building + "-" + floor + "-" + id);
            TimableOutput.class.notifyAll();
        }
        notifyAll();
    }

    public synchronized void down() throws InterruptedException {
        floor--;
        this.setDirection(Const.LAST_DOWN);
        this.setMoveType(Const.MoveType.DOWN);
        sleep(Const.MOVE_ONE_FLOOR_TIME);
        synchronized (TimableOutput.class) {
            TimableOutput.println("ARRIVE-" +
                    building + "-" + floor + "-" + id);
            TimableOutput.class.notifyAll();
        }
        notifyAll();
    }

    public synchronized void open() throws InterruptedException {
        synchronized (TimableOutput.class) {
            TimableOutput.println("OPEN-" + this.building + "-" + floor + "-" + id);
            TimableOutput.class.notifyAll();
        }
        while (true) {
            // TimableOutput.println("elevator " + id + "start to putDown people!");
            synchronized (requests) {
                PersonRequest request = requests.getDownPersonRequest(floor);
                if (request == null) {
                    break;
                }
                synchronized (TimableOutput.class) {
                    TimableOutput.println("OUT-" +
                            request.getPersonId() + "-" +
                            building + "-" +
                            floor + "-" + id);
                    TimableOutput.class.notifyAll();
                }
                requests.deleteRequest(request);
                requests.notifyAll();
            }
        }
        getRequestBetweenOpenClose();
        sleep(Const.OPEN_TIME);
        notifyAll();
    }

    public synchronized void close() throws InterruptedException {
        getRequestBetweenOpenClose();
        sleep(Const.CLOSE_TIME);
        synchronized (TimableOutput.class) {
            TimableOutput.println("CLOSE-" + this.building + "-" + floor + "-" + id);
            TimableOutput.class.notifyAll();
        }

        notifyAll();
    }

    private void getRequestBetweenOpenClose() throws InterruptedException {
        while (true) {
            // TimableOutput.println("elevator " + id + "start to getUp people!");
            synchronized (waitQueue) {
                PersonRequest request = waitQueue.getUpPersonRequest(floor);
                if (request == null) {
                    break;
                }
                waitQueue.deleteRequest(request);
                if (!requests.isFull()) {
                    addRequest(request);
                    synchronized (TimableOutput.class) {
                        TimableOutput.println("IN-" +
                                request.getPersonId() + "-" +
                                building + "-" +
                                floor + "-" + id);
                        TimableOutput.class.notifyAll();
                    }
                } else {
                    waitQueue.notifyAll();
                    break;
                }
                waitQueue.notifyAll();
            }
        }

    }

    public synchronized void pause() throws InterruptedException {
        synchronized (waitQueue) {
            /*synchronized (TimableOutput.class) {
                TimableOutput.println("elevator " + id + " wait for waitQueue's key");
                TimableOutput.class.notifyAll();
            }*/
            if (waitQueue.isEnd() && waitQueue.isEmpty()) {
                return;
            }
            if (waitQueue.isEmpty()) {
                this.waitQueue.wait();
            }
            /*synchronized (TimableOutput.class){
                TimableOutput.println("elevator " + id + " get waitQueue's key");
            }*/
            this.waitQueue.notifyAll();
        }
        /*synchronized (TimableOutput.class) {
            TimableOutput.println("elevator " + id + " end pausing");
            TimableOutput.class.notifyAll();
        }*/

        this.notifyAll();
    }

    public void setEnd(boolean isEnd) {
        requests.setEnd(true);
    }

    public char getBuilding() {
        return building;
    }

    public void setBuilding(char building) {
        this.building = building;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public int getNumOfPerson() {
        return numOfPerson;
    }

    public void setNumOfPerson(int numOfPerson) {
        this.numOfPerson = numOfPerson;
    }

    public synchronized void addRequest(PersonRequest request) {
        /*synchronized (TimableOutput.class) {
            TimableOutput.println("Elevator " + id + " get a request:" + request.toString());
            TimableOutput.class.notifyAll();
        }*/
        this.requests.addPersonRequest(request);
        this.numOfPerson++;
        notifyAll();
    }
}
