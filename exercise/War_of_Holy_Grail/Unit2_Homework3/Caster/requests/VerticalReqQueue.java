package requests;

import elevator.ElevatorState;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 纵向电梯等待队列，hashmap按照所在楼层分
 */
public class VerticalReqQueue {
    // 垂直电梯的等待队列
    // 即：请求所在楼层为键，请求为值
    private final HashMap<Integer, ArrayList<Req>> verticalReqs;
    private int reqNum;
    private boolean isEnd;
    
    public VerticalReqQueue() {
        this.verticalReqs = new HashMap<>();
        this.isEnd = false;
        this.reqNum = 0;
    }
    
    public synchronized void outsideWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized ArrayList<Req> getRequestAtFloor(int curFloor
            , int maxNum, ElevatorState elevatorState, boolean innerEmpty) {
        
        ElevatorState elevatorDirection = elevatorState;
        ArrayList<Req> reqAtFloor = verticalReqs.get(curFloor);
        ArrayList<Req> ret = new ArrayList<>();
        int inNum = 0;
        
        if (reqAtFloor == null) {
            return ret;
        }
        if (reqAtFloor.size() == 0) {
            return ret;
        }
        if ((innerEmpty || elevatorState == ElevatorState.Hung)) {
            elevatorDirection = reqAtFloor.get(0).getDirection();
        }
        for (int i = 0; i < reqAtFloor.size(); i++) {
            Req req = reqAtFloor.get(i);
            ElevatorState reqDirection;
            if (req.getTempToFloor() - req.getFloor() > 0) {
                reqDirection = ElevatorState.Up;
            } else {
                reqDirection = ElevatorState.Down;
            }
            if (reqDirection == elevatorDirection || elevatorDirection == ElevatorState.Hung) {
                elevatorDirection = reqDirection;
                ret.add(req);
                inNum++;
                if (inNum >= maxNum) {
                    break;
                }
            }
        }
        reqAtFloor.removeAll(ret);
        reqNum -= inNum;
        return ret;
    }
    
    public boolean hasRequestAtFloor(int floor) {
        ArrayList<Req> reqAtFloor = verticalReqs.get(floor);
        if (reqAtFloor == null) {
            return false;
        }
        return !reqAtFloor.isEmpty();
    }
    
    public synchronized ElevatorState setDirectionByNearest(int curFloor) {
        // 返回**最近**的有请求的楼层
        int minDown = 10;
        int minUp = 10;
        for (int i = curFloor - 1; i > 0; i--) {
            ArrayList<Req> reqAtFloor = verticalReqs.get(i);
            if (reqAtFloor != null) {
                if (reqAtFloor.size() != 0) {
                    minDown = curFloor - i;
                    break;
                }
            }
        }
        for (int i = curFloor + 1; i < 11; i++) {
            ArrayList<Req> reqAtFloor = verticalReqs.get(i);
            if (reqAtFloor != null) {
                if (reqAtFloor.size() != 0) {
                    minUp = i - curFloor;
                    break;
                }
            }
        }
        return (minDown > minUp ? ElevatorState.Up : ElevatorState.Down);
    }
    
    public synchronized void addRequest(int floor, Req req) {
        ArrayList<Req> queue = verticalReqs.get(floor);
        if (queue == null) {
            queue = new ArrayList<>();
        }
        queue.add(req);
        verticalReqs.put(floor, queue);
        reqNum++;
        notifyAll();
    }
    
    public synchronized void setEnd() {
        isEnd = true;
        notifyAll();
    }
    
    public synchronized boolean isEnd() {
        // notifyAll(); 不能加！！！！不然会轮询！！！！
        return isEnd;
    }
    
    public synchronized boolean isEmpty() {
        return reqNum == 0;
    }
}
