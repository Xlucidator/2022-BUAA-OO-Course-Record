package requests;

import elevator.Building;
import elevator.ElevatorState;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 横向电梯等待队列，hashmap按照所在楼座分
 */
public class HorizontalReqQueue {
    // 垂直电梯的等待队列
    // 即：请求所在楼座为键，请求为值
    private final HashMap<Character, ArrayList<Req>> horizontalReqs;
    private int reqNum;
    private boolean isEnd;
    
    public HorizontalReqQueue() {
        this.horizontalReqs = new HashMap<>();
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
    
    public synchronized ArrayList<Req> getRequestAtBuilding(Building curBuilding
            , int maxNum, ElevatorState elevatorState, boolean innerEmpty)
    {
        ElevatorState elevatorDirection = elevatorState;
        ArrayList<Req> reqAtBuilding = horizontalReqs.get(curBuilding.toChar());
        ArrayList<Req> ret = new ArrayList<>();
        int inNum = 0;
        if (reqAtBuilding == null) {
            return ret;
        }
        if (reqAtBuilding.size() == 0) {
            return ret;
        }
        if ((innerEmpty || elevatorState == ElevatorState.Hung)) {
            elevatorDirection = reqAtBuilding.get(0).getDirection();
        }
        for (int i = 0; i < reqAtBuilding.size(); i++) {
            Req req = reqAtBuilding.get(i);
            ElevatorState reqDirection;
            reqDirection = Building.getDirection(
                    req.getBuilding(), req.getTempToBuilding());
            
            if (!curBuilding.canReach(req.getTempToBuilding())) {
                // 如果当前电梯没法到达请求的tempToBuilding，则不接！！！
                continue;
            }
            if (reqDirection == elevatorDirection) {
                ret.add(req);
                reqAtBuilding.remove(i);
                i--;
                inNum++;
                if (inNum >= maxNum) {
                    break;
                }
            }
        }
        reqNum -= inNum;
        return ret;
    }
    
    public boolean hasRequestAtBuilding(Building building) {
        ArrayList<Req> reqAtBuilding = horizontalReqs.get(building.toChar());
        if (reqAtBuilding == null) {
            return false;
        }
        for (int i = 0; i < reqAtBuilding.size(); i++) {
            char toBuilding = reqAtBuilding.get(i).getTempToBuilding();
            if (building.canReach(toBuilding)) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized ElevatorState setDirectionByNearest(Building curBuilding) {
        // 返回**最近**的有请求的楼座
        Building building = curBuilding.deepCopy();
        int minRight = 5;
        int minLeft = 5;
        
        building.goRight();
        int distance = Building.getMinDistance(curBuilding.toChar(), building.toChar());
        while (distance > 0) {
            if (hasRequestAtBuilding(building)) {
                minRight = distance;
                break;
            }
            building.goRight();
            distance = Building.getMinDistance(curBuilding.toChar(), building.toChar());
        }
    
        building = curBuilding.deepCopy();
        building.goLeft();
        distance = Building.getMinDistance(curBuilding.toChar(), building.toChar());
        while (distance < 0) {
            if (hasRequestAtBuilding(building)) {
                minLeft = -distance;
                break;
            }
            building.goLeft();
            distance = Building.getMinDistance(curBuilding.toChar(), building.toChar());
        }
        
        return (minRight > minLeft ? ElevatorState.Left : ElevatorState.Right);
    }
    
    public synchronized void addRequest(char building, Req req) {
        ArrayList<Req> queue = horizontalReqs.get(building);
        if (queue == null) {
            queue = new ArrayList<>();
        }
        queue.add(req);
        horizontalReqs.put(building, queue);
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
