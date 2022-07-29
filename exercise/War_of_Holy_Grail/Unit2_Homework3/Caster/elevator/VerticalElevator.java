package elevator;

import control.Controller;
import control.Counter;
import io.Output;
import requests.Req;
import requests.VerticalReqQueue;

import java.util.ArrayList;
import java.util.HashMap;

public class VerticalElevator extends Thread {
    
    private final Building building;
    private final int elevatorId;
    private int personNum = 0;
    private int floor = BOTTOM;
    private ElevatorState elevatorState = ElevatorState.Up;
    private boolean formerHungOrReverse = true;
    private final VerticalReqQueue outside;  // 同楼座共有
    private ArrayList<Req> privateOutside;  // 本电梯私有
    private final HashMap<Integer, ArrayList<Req>> inside;  // 以目的楼层为键
    
    private boolean innerCanGoOut = false;
    private boolean outerCanGetIn = false;
    
    private static final int TOP = 10;
    private static final int BOTTOM = 1;
    private final int capacity;
    private final int moveSpeed;
    
    public VerticalElevator(char building, int elevatorId, VerticalReqQueue outside
            , int capacity, int speed) {
        this.building = new Building(building);
        this.elevatorId = elevatorId;
        this.outside = outside;
        // this.privateOutside = new ArrayList<>();  no need
        this.inside = new HashMap<>();
        this.capacity = capacity;  // 默认电梯容量
        this.moveSpeed = speed;  // 默认上下楼速度
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
                changeDirection();
            } else {
                //System.out.println(elevatorState);
                formerHungOrReverse = true;
                elevatorState = ElevatorState.Hung;
                outside.outsideWait();
            }
        }
    }
    
    public void openAndClose() {
        Output.getInstance().println(String.format(
                "OPEN-%s-%d-%d", building, floor, elevatorId));
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
        if (formerHungOrReverse || innerEmptyBefore) {
            setDirectionByInsiders();
            formerHungOrReverse = false;
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
    
    public void goUp() {
        if (floor == TOP) {
            // ERROR SITUATION!!!!!
            // System.out.println("ERROR IN " + currentThread().getName() + ".goUp()");
            elevatorState = ElevatorState.Down;
            return;
        }
        elevatorState = ElevatorState.Up;
        floor++;
        try {
            sleep(moveSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Output.getInstance().println(String.format(
                "ARRIVE-%s-%d-%d", building, floor, elevatorId)
        );
    }
    
    public void goDown() {
        if (floor == BOTTOM) {
            // ERROR SITUATION!!!
            // System.out.println("ERROR IN " + currentThread().getName() + ".goDown()");
            elevatorState = ElevatorState.Up;
            return;
        }
        elevatorState = ElevatorState.Down;
        floor--;
        try {
            sleep(moveSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Output.getInstance().println(String.format(
                "ARRIVE-%s-%d-%d", building, floor, elevatorId)
        );
    }
    
    public void moveAlongOriginalDirection() {
        if (elevatorState == ElevatorState.Up) {
            goUp();
        } else if (elevatorState == ElevatorState.Down) {
            goDown();
        }
    }
    
    public void setDirectionByOutsiders() {
        /* function used when inner is empty and state is Hung */
        
        elevatorState = outside.setDirectionByNearest(floor);
    }
    
    public void setDirectionByInsiders() {
        /* function used when personNum change from 0 to positive number */
        int upNum = 0;
        int downNum = 0;
        for (int i = floor + 1; i <= TOP; i++) {
            ArrayList<Req> reqToFloor = inside.get(i);
            if (reqToFloor == null) {
                continue;
            }
            upNum += reqToFloor.size();
        }
        for (int i = floor - 1; i >= BOTTOM; i--) {
            ArrayList<Req> reqToFloor = inside.get(i);
            if (reqToFloor == null) {
                continue;
            }
            downNum += reqToFloor.size();
        }
        if (upNum > downNum) {
            elevatorState = ElevatorState.Up;
        } else {
            elevatorState = ElevatorState.Down;
        }
    }
    
    public void changeDirection() {
        formerHungOrReverse = true;
        if (elevatorState == ElevatorState.Up) {
            elevatorState = ElevatorState.Down;
        } else if (elevatorState == ElevatorState.Down) {
            elevatorState = ElevatorState.Up;
        } else if (elevatorState == ElevatorState.Hung) {
            if (floor == BOTTOM) {
                elevatorState = ElevatorState.Up;
                return;
            } else if (floor == TOP) {
                elevatorState = ElevatorState.Down;
                return;
            }
            setDirectionByOutsiders();
        }
    }
    
    public boolean outerCanGetIn() {
        /* elevator now arrives at the floor that is the
            starting floor of someone outside the elevator */
        if (personNum == capacity) {
            return false;
        }
        if (floor == BOTTOM && elevatorState == ElevatorState.Down) {
            elevatorState = ElevatorState.Up;
            formerHungOrReverse = true;
        } else if (floor == TOP && elevatorState == ElevatorState.Up) {
            elevatorState = ElevatorState.Down;
            formerHungOrReverse = true;
        }
        privateOutside = outside.getRequestAtFloor(
                floor, capacity - personNum, elevatorState, isInnerEmpty()
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
            // System.out.println(privateOutside);
            for (int i = 0; i < privateOutside.size(); i++) {
                Req outsider = privateOutside.get(i);
                ArrayList<Req> toFloor = inside.get(outsider.getTempToFloor());
                if (toFloor == null) {
                    toFloor = new ArrayList<>();
                }
                toFloor.add(outsider);
                inside.put(outsider.getTempToFloor(), toFloor);
                personNum++;
                Output.getInstance().println(String.format("IN-%d-%s-%d-%d"
                        , outsider.getId(), building, floor, elevatorId)
                );
            }
            privateOutside = null;
            outerCanGetIn = outerCanGetIn();
        }
    }
    
    public boolean innerCanGoOut() {
        /* elevator now arrives at the floor that is the
            destination floor of someone inside the elevator */
        if (personNum == 0) {
            return false;
        }
        ArrayList<Req> insideAtFloor = inside.get(floor);
        if (insideAtFloor == null) {
            return false;
        }
        innerCanGoOut = !insideAtFloor.isEmpty();
        return innerCanGoOut;
    }
    
    public void innerGoOut() {
        
        ArrayList<Req> reqAtFloor = inside.get(floor);
        int finishedNum = 0;
        for (int i = 0; i < reqAtFloor.size(); i++) {
            Req req = reqAtFloor.get(i);
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
        reqAtFloor.clear();
        inside.put(floor, reqAtFloor);
        innerCanGoOut = false;
        outerCanGetIn = outerCanGetIn();
    }
    
    public boolean isInnerEmpty() {
        return personNum == 0;
    }
    
    public boolean isOutsideEmpty() {
        return outside.isEmpty();
    }
    
    public boolean hasRequestAhead() {
        /* judge if there is PersonRequest ahead of current floor
            in current direction of the elevator */
        if (outside.isEmpty() || elevatorState == ElevatorState.Hung) {
            return false;
        }
        if (elevatorState == ElevatorState.Up && floor >= TOP) {
            return false;
        }
        if (elevatorState == ElevatorState.Down && floor <= BOTTOM) {
            return false;
        }
        boolean hasRequestAhead;
        int i = floor;
        if (elevatorState == ElevatorState.Up) {
            i++;
        } else {
            i--;
        }
        while (i >= BOTTOM && i <= TOP) {
            hasRequestAhead = outside.hasRequestAtFloor(i);
            if (hasRequestAhead) {
                return true;
            }
            if (elevatorState == ElevatorState.Up) {
                i++;
            } else {
                i--;
            }
        }
        return false;
    }
    
}
