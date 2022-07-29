import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Building {
    private static final int MAX_FLOOR = 10;
    private static final int NUM_OF_BUILDINGS = 5;

    private final boolean isFloor;
    private final int pos;
    private final List<Elevator> elevators = new ArrayList<>();
    private final Map<Integer, List<DistributedRequest>> requestMap = new TreeMap<>();
    private final List<DistributedRequest> requestList = new LinkedList<>();
    private Policy policy;
    private final List<PersonRequest> unhandledRequests = new LinkedList<>();
    //private Lock allLock = new ReentrantLock();

    public Building(boolean isFloor, int pos) {
        this.isFloor = isFloor;
        this.pos = pos;
        for (int i = 1; i <= (isFloor ? NUM_OF_BUILDINGS : MAX_FLOOR); ++i) {
            requestMap.put(i, new LinkedList<>());
        }
        policy = isFloor ? new GreedyLoopPolicy(this) : new ScanningPolicy(this);
    }

    public static int getMaxFloor() {
        return MAX_FLOOR;
    }

    public static int getNumOfBuildings() {
        return NUM_OF_BUILDINGS;
    }

    public int getPos() {
        return pos;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public Map<Integer, List<DistributedRequest>> getRequestMap() {
        return requestMap;
    }

    public List<DistributedRequest> getRequestList() {
        return requestList;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public List<PersonRequest> getUnhandledRequests() {
        return unhandledRequests;
    }

    //    public Lock getAllLock() {
    //        return allLock;
    //    }

    public void addElevator(String id, int space, long speed, int info) {
        //allLock.lock();
        synchronized (this) {
            synchronized (Shutter.getShutter()) {
                Shutter.getShutter().increase();
            }
            Elevator elevator = new Elevator(id, isFloor, this, space, speed, info);
            elevators.add(elevator);
            policy.adjust();
            this.notifyAll();
        }
        //allLock.notifyAll();
        //allLock.unlock();
    }

    public void addRequest(PersonRequest request) {
        int pos = isFloor ? request.getFromBuilding() - 'A' + 1 : request.getFromFloor();
        //allLock.lock();
        synchronized (this) {
            DistributedRequest distributedRequest = policy.distribute(request);
            requestMap.get(pos).add(distributedRequest);
            requestList.add(distributedRequest);
            policy.adjust();
            this.notifyAll();
        }
//        allLock.notifyAll();
//        allLock.unlock();
    }

    public void removeRequest(int pos, DistributedRequest request) {
        requestMap.get(pos).remove(request);
        requestList.remove(request);
    }

    public void turnDown() {
        synchronized (this) {
            for (Elevator elevator : elevators) {
                elevator.getStatus().setTurnedDown(true);
            }
            this.notifyAll();
        }
    }

    public boolean isWaiting(int pos, String elevatorId) {
        for (DistributedRequest request : requestMap.get(pos)) {
            if (request.getElevatorId().equals(elevatorId)) {
                return true;
            }
        }
        return false;
    }

    public long evaluate(int from, int to) {
        long rst = 0;
        for (Elevator elevator : elevators) {
            int info = elevator.getStatus().getInfo();
            if (((info >> (from - 1)) & 1) + ((info >> (to - 1)) & 1) == 2) {
                rst += elevator.evaluate();
            }
        }
        rst /= (1 + requestList.size());
        return rst;
    }
}
