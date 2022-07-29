import com.oocourse.elevator3.ElevatorRequest;

import java.util.HashMap;

public class Factory {
    private static volatile Factory FACTORY = null;
    private PersonQueue[] waitQueues = new PersonQueue[15];
    private HashMap<Integer, HashMap<Integer, Double>> map;

    private Factory() {
    }

    public static Factory getFactory() {
        if (FACTORY == null) {
            synchronized (Factory.class) {
                if (FACTORY == null) {
                    FACTORY = new Factory();
                }
            }
        }
        return FACTORY;
    }

    public void initial(PersonQueue[] waitQueues) {
        this.waitQueues = waitQueues;
        for (int i = 0; i < 5; i++) {
            VerticalElevator verticalElevator = new
                    VerticalElevator(waitQueues[i], i + 1, (char) (i + 'A'), 8, 600);
            verticalElevator.start();
        }
        TransverseElevator transverseElevator = new
                TransverseElevator(waitQueues[5], 6, 1, 8, 600, 31);
        transverseElevator.start();
        this.map = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            HashMap<Integer, Double> tmp = new HashMap<>();
            if (i == 1) {
                tmp.put(31, 0.6);
            }
            this.map.put(i, tmp);
        }
    }

    public void addElevator(ElevatorRequest request) {
        if (request.getType().equals("floor")) {
            synchronized (map) {
                HashMap<Integer, Double> tmp = map.get(request.getFloor());
                tmp.put(request.getSwitchInfo(), request.getSpeed());
            }
            TransverseElevator transverseElevator = new
                    TransverseElevator(waitQueues[4 + request.getFloor()],
                    request.getElevatorId(), request.getFloor(), request.getCapacity(),
                    (int) (request.getSpeed() * 1000), request.getSwitchInfo());
            transverseElevator.start();
        } else {
            VerticalElevator verticalElevator = new
                    VerticalElevator(waitQueues[request.getBuilding() - 'A'],
                    request.getElevatorId(), request.getBuilding(),
                    request.getCapacity(), (int) (request.getSpeed() * 1000));
            verticalElevator.start();
        }
    }

    public synchronized HashMap<Integer, Double> getMap(int floor) {
        return map.get(floor);
    }
}
