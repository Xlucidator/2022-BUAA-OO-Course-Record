package requests;

import com.oocourse.elevator3.PersonRequest;
import control.HorizontalMap;
import elevator.Building;
import elevator.ElevatorState;

public class Req {
    // private final PersonRequest personReq;
    private final int id;
    private final char endBuilding;  // 终点楼座
    private final int endFloor;  // 终点楼层
    
    private char tempToBuilding;  // 暂时要前往的中转楼座
    private int tempToFloor;  // 暂时要前往的中转楼层
    
    private char building;  // 当前所处楼座
    private int floor;  // 当前所处楼层
    
    public Req(PersonRequest personReq) {
        this.endBuilding = personReq.getToBuilding();
        this.endFloor = personReq.getToFloor();
        this.building = personReq.getFromBuilding();
        this.floor = personReq.getFromFloor();
        this.id = personReq.getPersonId();
        
        this.tempToBuilding = endBuilding;
        this.tempToFloor = endFloor;
    }
    
    public boolean isUnfinished() {
        return (endBuilding != building) || (endFloor != floor);
    }
    
    public ElevatorState getDirection() {
        if (building == tempToBuilding) {
            return (floor > tempToFloor) ? ElevatorState.Down : ElevatorState.Up;
        }
        return Building.getDirection(building, tempToBuilding);
    }
    
    public void updateCurrentPos(char building, int floor) {
        this.building = building;
        this.floor = floor;
    }
    
    public void updateTempToPos(char building, int floor) {
        this.tempToBuilding = building;
        this.tempToFloor = floor;
    }
    
    public boolean needTransfer() {
        if (building == endBuilding) {  // 同楼座一定不需要换乘
            return false;
        }
        if (floor != endFloor) {  // 不同楼座不同层，一定需要换乘
            return true;
        }
        // 不同楼座，同层，判断是否可达（同一个电梯）
        if (HorizontalMap.getInstance().canReach(floor, building, endBuilding)) {
            return false;
        }
        return true;
    }
    
    public int getId() {
        return id;
    }
    
    public char getBuilding() {
        return building;
    }
    
    public char getEndBuilding() {
        return endBuilding;
    }
    
    public char getTempToBuilding() {
        return tempToBuilding;
    }
    
    public int getEndFloor() {
        return endFloor;
    }
    
    public int getFloor() {
        return floor;
    }
    
    public int getTempToFloor() {
        return tempToFloor;
    }
}
