import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.HashMap;
import java.util.Iterator;

public class Elevator extends Thread {
    private final RequestQueue processQueue;
    private HashMap<Integer, Integer> waitTable;
    private char nowBuilding;
    private int nowFloor;
    private int eleId;
    private boolean isEmpty;
    private boolean isFull;
    private String moveStatus;
    private int toTop;
    private int toBeneath;
    private PersonRequest mainRequest = null;
    private int openTag;

    public Elevator(int eleId,
                    RequestQueue processingQueue) {
        this.eleId = eleId;
        this.nowBuilding = (char) ('A' + eleId - 1);
        this.nowFloor = 1;
        this.processQueue = processingQueue;
        this.waitTable = new HashMap<>();
        this.moveStatus = "STOP";
    }

    public boolean isEmpty() {
        isEmpty = (waitTable.size() == 0);
        return isEmpty;
    }

    public boolean isFull() {
        isFull = (waitTable.size() == 6);
        return isFull;
    }

    public char getNowBuilding() {
        return nowBuilding;
    }

    public int getNowFloor() {
        return nowFloor;
    }

    public String getMoveStatus() {
        return moveStatus;
    }

    public void setMoveStatus(String moveStatus) {
        this.moveStatus = moveStatus;
    }

    @Override
    public void run() {
        while (true) {
            /*if (processQueue.isEnd() && processQueue.isEmpty()) {
                System.out.println("Elevator" + EleId + "OVER");
            }
            synchronized (processQueue) {
                if (processQueue.isEmpty()) {
                    try {
                        moveStatus = "STOP";
                        processQueue.wait();
                        System.out.println("processQueueWaitEnd");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

             */
            synchronized (processQueue) {
                while (true) {
                    if (processQueue.isEnd() && processQueue.isEmpty()) {
                        //System.out.println("Elevator" + EleId + "Over");
                        return;
                    } else if (processQueue.isEmpty()) {
                        moveStatus = "STOP";
                        try {
                            processQueue.wait();
                            continue;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            }
            dealIO();
            selectStatus();
            move();
        }
    }

    public synchronized void selectStatus() {
        if (processQueue.isEmpty()) {
            moveStatus = "STOP";
            return;
        }
        int minWay = 11;
        if (this.isEmpty()) {
            mainRequest = processQueue.getRequests().getFirst();
            moveStatus = getEleDirection(mainRequest);
            //System.out.println(mainRequest + ":" + moveStatus);
        } else {
            for (PersonRequest iter : processQueue.getRequests()) {
                if (Math.abs(iter.getFromFloor() - nowFloor) < minWay
                        && waitTable.containsKey(iter.getPersonId())) {
                    minWay = Math.abs(iter.getFromFloor() - nowFloor);
                    mainRequest = iter;
                    //System.out.println(mainRequest);
                }
                moveStatus = getRequestDirection(mainRequest);
            }
        }
    }

    public synchronized void move() {
        switch (moveStatus) {
            case "UP":
                try {
                    sleep(400);
                    nowFloor = Integer.min(nowFloor + 1, 10);
                    if (nowFloor == 10) {
                        moveStatus = "STOP";
                    }
                    TimableOutput.println("ARRIVE-" + nowBuilding +
                            "-" + nowFloor +
                            "-" + eleId);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case "DOWN":
                try {
                    sleep(400);
                    nowFloor = Integer.max(nowFloor - 1, 1);
                    if (nowFloor == 10) {
                        moveStatus = "STOP";
                    }
                    TimableOutput.println("ARRIVE-" + nowBuilding +
                            "-" + nowFloor +
                            "-" + eleId);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public synchronized int personIn(PersonRequest iter) {
        if (iter.getFromFloor() == nowFloor
                && !waitTable.containsKey(iter.getPersonId())
                && (getRequestDirection(iter).equals(moveStatus)
                || getRequestDirection(iter).equals("STOP")
                || moveStatus.equals("STOP"))
                && !this.isFull) {
            if (openTag == 0) {
                TimableOutput.println("OPEN-" + nowBuilding +
                        "-" + nowFloor +
                        "-" + eleId);
                try {
                    sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                openTag = 1;
            }
            //[时间戳]IN-乘客ID-所在座-所在层-电梯ID
            TimableOutput.println("IN-" + iter.getPersonId() +
                    "-" + nowBuilding +
                    "-" + nowFloor +
                    "-" + eleId);
            waitTable.put(iter.getPersonId(), 1);
        }
        return openTag;
    }

    public synchronized void dealIO() {
        openTag = 0;
        Iterator<PersonRequest> iterator = processQueue.getRequests().iterator();
        while (iterator.hasNext()) {
            //if(getRequestDirection(iter).equals(moveStatus)) {

            //}
            PersonRequest iter = iterator.next();
            //System.out.println(iter);
            openTag = personIn(iter);
            //openTag = PersonOut(iter, openTag);
            if (iter.getToFloor() == nowFloor
                    && waitTable.containsKey(iter.getPersonId())) {
                if (openTag == 0) {
                    TimableOutput.println("OPEN-" + nowBuilding +
                            "-" + nowFloor +
                            "-" + eleId);
                    try {
                        sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    openTag = 1;
                }
                TimableOutput.println("OUT-" + iter.getPersonId() +
                        "-" + nowBuilding +
                        "-" + nowFloor +
                        "-" + eleId);
                waitTable.remove(iter.getPersonId());
                iterator.remove();
                notifyAll();
            }
        }

        if (openTag == 1) {
            TimableOutput.println("CLOSE-" + nowBuilding +
                    "-" + nowFloor +
                    "-" + eleId);
        }
    }

    public synchronized String getRequestDirection(PersonRequest request) {
        return (request.getToFloor() - request.getFromFloor() > 0) ? "UP" :
                (request.getToFloor() - request.getFromFloor() < 0) ? "DOWN" : "STOP";
    }

    public synchronized String getEleDirection(PersonRequest request) {
        return (nowFloor - request.getFromFloor() < 0) ? "UP" :
                (nowFloor - request.getFromFloor() > 0) ? "DOWN" : "STOP";
    }

    public synchronized int personOut(PersonRequest iter) {
        if (iter.getToFloor() == nowFloor
                && waitTable.containsKey(iter.getPersonId())) {
            if (openTag == 0) {
                TimableOutput.println("OPEN-" + nowBuilding +
                        "-" + nowFloor +
                        "-" + eleId);
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                openTag = 1;
            }
            //[时间戳]OUT-乘客ID-所在座-所在层-电梯ID
            TimableOutput.println("OUT-" + iter.getPersonId() +
                    "-" + nowBuilding +
                    "-" + nowFloor +
                    "-" + eleId);
            waitTable.remove(iter.getPersonId());
            processQueue.getRequests().remove(iter);
        }
        return openTag;
    }
}
