package control;

// import java.util.Arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 记录横向电梯：
 * 某层是否有横向电梯，若有，可停靠哪些楼座
 */
public class HorizontalMap {
    private static final HorizontalMap MAP = new HorizontalMap();
    // HashMap<floor, ArrayList<switchInfo>>
    private final HashMap<Integer, ArrayList<Integer>> reachArrayForElevator = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    private HorizontalMap() {
        // Arrays.fill(reachArray, false);
    }
    
    public static HorizontalMap getInstance() {
        return MAP;
    }
    
    public boolean canReach(int floor, char from, char to) {
        lock.readLock().lock();
        // 在floor层，从from座到to座是否有**直达**电梯
        if (floor == 1) {
            lock.readLock().unlock();
            return true;
        }
        ArrayList<Integer> elevatorReach = reachArrayForElevator.get(floor);
        if (elevatorReach == null) {
            lock.readLock().unlock();
            return false;
        }
        for (int i = 0; i < elevatorReach.size(); i++) {
            int switchInfo = elevatorReach.get(i);
            if (((switchInfo >> (from - 'A')) & 1) == 1 &&
                    ((switchInfo >> (to - 'A')) & 1) == 1) {
                lock.readLock().unlock();
                return true;
            }
        }
        // TODO:只要没有直达就认为该层不可走，待优化！（没有同层的换乘）
        lock.readLock().unlock();
        return false;
        
    }
    
    public void addElevator(int floor, int switchInfo) {
        lock.writeLock().lock();
        ArrayList<Integer> elevatorReach = reachArrayForElevator.get(floor);
        if (elevatorReach == null) {
            elevatorReach = new ArrayList<>();
        }
        elevatorReach.add(switchInfo);
        reachArrayForElevator.put(floor, elevatorReach);
        lock.writeLock().unlock();
    }
    
}
