package elevator;

public class Building {
    private final boolean[] reachable = new boolean[5];
    private int elevatorBuilding;
    private final int switchInfo;
    
    public Building(int switchInfo, char elevatorBuilding) {
        this.switchInfo = switchInfo;
        init();
        this.elevatorBuilding = elevatorBuilding - 'A';
    }
    
    public Building(char elevatorBuilding) {
        this.switchInfo = 31;
        init();
        this.elevatorBuilding = elevatorBuilding - 'A';
    }
    
    private void init() {
        // Arrays.fill(reachable, false);
        for (int i = 0; i < 5; i++) {
            if (((switchInfo >> i) & 1) == 1) {
                reachable[i] = true;
            }
        }
    }
    
    public void goLeft() {  // A → E → D → C → B → A
        int i = elevatorBuilding;
        do {
            if (i == 0) {
                i = 4;
            }
            else {
                i--;
            }
        } while (!reachable[i]);
        elevatorBuilding = i;
    }
    
    public boolean goLeftOneBuilding() {
        // 返回当前楼层能否开门
        if (elevatorBuilding == 0) {
            elevatorBuilding = 4;
        } else {
            elevatorBuilding--;
        }
        return canReach((char) (elevatorBuilding + 'A'));
    }
    
    public void goRight() {
        int i = elevatorBuilding;
        do {
            if (i == 4) {
                i = 0;
            }
            else {
                i++;
            }
        } while (!reachable[i]);
        elevatorBuilding = i;
    }
    
    public boolean goRightOneBuilding() {
        // 返回当前楼层能否开门
        if (elevatorBuilding == 4) {
            elevatorBuilding = 0;
        } else {
            elevatorBuilding++;
        }
        return canReach((char) (elevatorBuilding + 'A'));
    }
    
    public char toChar() {
        return (char) ('A' + elevatorBuilding);
    }
    
    public boolean canReach(char building) {
        return reachable[building - 'A'];
    }
    
    public static ElevatorState getDirection(char from, char to) {
        int delta = to - from;
        if (delta > 2) {  // eg: A to D, A to B to C to D, delta = 3
            return ElevatorState.Left;  // A to E to D, distance = 2
        } else if (delta < -2) {  // eg: D to A
            return ElevatorState.Right;
        } else if (delta > 0) {
            return ElevatorState.Right;
        }
        return ElevatorState.Left;
    }
    
    public static int getMinDistance(char from, char to) {
        // 正数：goRight； 负数：goLeft
        int delta = to - from;
        if (delta > 2) {  // eg: A to D, A to B to C to D, delta = 3
            return delta - 5;
        } else if (delta < -2) {  // eg: D to A
            return delta + 5;
        }
        return delta;
    }
    
    @Override
    public String toString() {
        return String.valueOf(toChar());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Building)) {
            return false;
        }
        Building another = (Building) obj;
        return another.elevatorBuilding == this.elevatorBuilding;
    }
    
    public Building deepCopy() {
        return new Building(switchInfo, (char) (elevatorBuilding + 'A'));
    }
    
}
