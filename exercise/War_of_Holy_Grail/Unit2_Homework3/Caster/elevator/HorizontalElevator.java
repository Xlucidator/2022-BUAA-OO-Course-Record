package elevator;

import control.Controller;
import control.Counter;
import control.HorizontalMap;
import io.Output;
import requests.HorizontalReqQueue;
import requests.Req;

import java.util.ArrayList;
import java.util.HashMap;

public class HorizontalElevator extends Thread {
    private final Building building;
    private final int elevatorId;
    private int personNum = 0;
    private final int floor;
    private ElevatorState elevatorState = ElevatorState.Hung;
    private boolean formerHung = true;
    private final HorizontalReqQueue outside;  // 同楼层共有
    private ArrayList<Req> privateOutside;  // 本电梯私有
    private final HashMap<Character, ArrayList<Req>> inside;  // 以目的楼座为键
    
    private boolean innerCanGoOut = false;
    private boolean outerCanGetIn = false;
    
    private final int capacity;
    private final int moveSpeed;
    
    public HorizontalElevator(int floor, int elevatorId, HorizontalReqQueue outside
            , int capacity, int speed, int msg) {
        this.building = new Building(msg, 'A');
        this.floor = floor;
        this.elevatorId = elevatorId;
        this.outside = outside;
        this.inside = new HashMap<>();
        this.capacity = capacity;
        this.moveSpeed = speed;
        HorizontalMap.getInstance().addElevator(floor, msg);
    }
    
    @Override
    public void run() {
        while (true) {
            if (outside.isEnd() && outside.isEmpty() && isInnerEmpty()) {
                return;
            }
            if (innerCanGoOut()) {
                openAndClose();
            } else if (outerCanGetIn()) {
                openAndClose();
            } else if (!isInnerEmpty() || hasRequestAhead()) {
                moveAlongOriginalDirection();
            } else if (!isOutsideEmpty()) {
                reverseDirection();
            } else {
                formerHung = true;
                elevatorState = ElevatorState.Hung;
                outside.outsideWait();
            }
        }
        
    }
    
    public void openAndClose() {
        Output.getInstance().println(String.format(
                "OPEN-%s-%d-%d", building, floor, elevatorId)
        );
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean innerEmptyBefore = isInnerEmpty();
        if (innerCanGoOut) {
            innerGoOut();
        }
        if (outerCanGetIn) {
            outerGetIn();
        }
        if (formerHung || innerEmptyBefore) {
            setDirectionByInsiders();
            formerHung = false;
        }
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Output.getInstance().println(String.format(
                "CLOSE-%s-%d-%d", building, floor, elevatorId)
        );
    }
    
    public void goLeft() {
        elevatorState = ElevatorState.Left;
        boolean canOpen;
        do {
            try {
                sleep(moveSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canOpen = building.goRightOneBuilding();
            Output.getInstance().println(String.format(
                    "ARRIVE-%s-%d-%d", building, floor, elevatorId)
            );
        } while (!canOpen);
    }
    
    public void goRight() {
        elevatorState = ElevatorState.Right;
        boolean canOpen;
        do  {
            try {
                sleep(moveSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canOpen = building.goRightOneBuilding();
            Output.getInstance().println(String.format(
                    "ARRIVE-%s-%d-%d", building, floor, elevatorId)
            );
        } while (!canOpen);
    }
    
    public void moveAlongOriginalDirection() {
        if (elevatorState == ElevatorState.Left) {
            goLeft();
        } else if (elevatorState == ElevatorState.Right) {
            goRight();
        }
        // if elevatorState == ElevatorState.Hung, do nothing
        // 上面这个情况出现在outsides被其他电梯抢先取走，导致本来有请求，现在没了，于是继续保持挂起
    }
    
    public boolean outerCanGetIn() {
        if (personNum == capacity) {
            return false;
        }
        privateOutside = outside.getRequestAtBuilding(
                building, capacity - personNum, elevatorState, isInnerEmpty()
        );
        if (!privateOutside.isEmpty()) {
            outerCanGetIn = true;
            return true;
        }
        outerCanGetIn = false;
        return false;
    }
    
    public void outerGetIn() {
        while (outerCanGetIn) {
            personNum += privateOutside.size();
            for (int i = 0; i < privateOutside.size(); i++) {
                Req outsider = privateOutside.get(i);
                ArrayList<Req> toBuilding = inside.get(outsider.getTempToBuilding());
                if (toBuilding == null) {
                    toBuilding = new ArrayList<>();
                }
                toBuilding.add(outsider);
                inside.put(outsider.getTempToBuilding(), toBuilding);
                Output.getInstance().println(String.format("IN-%d-%s-%d-%d"
                        , outsider.getId(), building, floor, elevatorId)
                );
            }
            privateOutside = null;
            outerCanGetIn = outerCanGetIn();
        }
        
    }
    
    public boolean innerCanGoOut() {
        if (personNum == 0) {
            return false;
        }
        ArrayList<Req> insideAtFloor = inside.get(building.toChar());
        if (insideAtFloor == null) {
            return false;
        }
        innerCanGoOut = !insideAtFloor.isEmpty();
        return innerCanGoOut;
    }
    
    public void innerGoOut() {
        
        ArrayList<Req> reqAtBuilding = inside.get(building.toChar());
        int finishedNum = 0;
        for (int i = 0; i < reqAtBuilding.size(); i++) {
            Req req = reqAtBuilding.get(i);
            personNum--;
            Output.getInstance().println(String.format("OUT-%d-%s-%d-%d"
                    , req.getId(), building, floor, elevatorId)
            );
            req.updateCurrentPos(building.toChar(), floor);
            if (req.isUnfinished()) {
                Controller.getInstance().addRequest(req);
            } else {
                finishedNum++;
            }
        }
        if (finishedNum != 0) {
            Counter.getInstance().release(finishedNum);
        }
        reqAtBuilding.clear();
        inside.put(building.toChar(), reqAtBuilding);
        innerCanGoOut = false;
        outerCanGetIn = outerCanGetIn();
    }
    
    public boolean isInnerEmpty() {
        return personNum == 0;
    }
    
    public boolean isOutsideEmpty() {
        return outside.isEmpty();
    }
    
    public void reverseDirection() {
        // 标准：距离电梯最近的outsider
        if (elevatorState == ElevatorState.Hung) {
            elevatorState = outside.setDirectionByNearest(building);
        } else if (elevatorState == ElevatorState.Left) {
            elevatorState = ElevatorState.Right;
        } else {
            elevatorState = ElevatorState.Left;
        }
    }
    
    public void setDirectionByInsiders() {
        // 标准：最多insider去的方向
        int goLeftNum = 0;
        int goRightNum = 0;
        
        Building building = this.building.deepCopy();
        building.goRight();
        while (Building.getMinDistance(
                this.building.toChar(), building.toChar()) > 0
        ) {
            ArrayList<Req> reqToBuilding = inside.get(building.toChar());
            if (reqToBuilding != null) {
                goRightNum += reqToBuilding.size();
            }
            building.goRight();
        }
    
        building = this.building.deepCopy();
        building.goLeft();
        while (Building.getMinDistance(
                this.building.toChar(), building.toChar()) < 0
        ) {
            ArrayList<Req> reqToBuilding = inside.get(building.toChar());
            if (reqToBuilding != null) {
                goLeftNum += reqToBuilding.size();
            }
            building.goLeft();
        }
        if (goLeftNum > goRightNum) {
            elevatorState = ElevatorState.Left;
        } else {
            elevatorState = ElevatorState.Right;
        }
    }
    
    public boolean hasRequestAhead() {
        
        if (outside.isEmpty() || elevatorState == ElevatorState.Hung) {
            return false;
        }
        Building building = this.building.deepCopy();
        if (elevatorState == ElevatorState.Left) {
            building.goLeft();
            while (Building.getMinDistance(
                    this.building.toChar(), building.toChar()) < 0
            ) {
                if (outside.hasRequestAtBuilding(building)) {
                    return true;
                }
                building.goLeft();
            }
        } else {
            building.goRight();
            while (Building.getMinDistance(
                    this.building.toChar(), building.toChar()) > 0
            ) {
                if (outside.hasRequestAtBuilding(building)) {
                    return true;
                }
                building.goRight();
            }
        }
        return false;
    }
}
