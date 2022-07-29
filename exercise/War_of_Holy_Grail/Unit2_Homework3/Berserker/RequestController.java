import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

public class RequestController extends Thread {
    private static final RequestController REQUEST_CONTROLLER = new RequestController();

    public RequestController() {
        setName("Input");
    }

    public static RequestController getRequestController() {
        return REQUEST_CONTROLLER;
    }

    @Override
    public void run() {
        synchronized (Shutter.getShutter()) {
            Shutter.getShutter().increase();
        }
        ElevatorInput input = new ElevatorInput(System.in);
        while (true) {
            Request request = input.nextRequest();
            if (request == null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (Shutter.getShutter()) {
                    Shutter.getShutter().decrease();
                    Shutter.getShutter().notifyAll();
                }
                break;
            }
            if (request instanceof PersonRequest) {
                onPersonRequest(request);
            } else if (request instanceof ElevatorRequest) {
                onElevatorRequest(request);
            }
        }
    }

    private void onPersonRequest(Request request) {
        PersonRequest personRequest = (PersonRequest) request;
        Building building = findBuilding(personRequest);
        building.addRequest(personRequest);
    }

    private void onElevatorRequest(Request request) {
        ElevatorRequest elevatorRequest = (ElevatorRequest) request;
        MultiBuilding multiBuilding = MultiBuilding.getMultiBuilding();
        String id = String.valueOf(elevatorRequest.getElevatorId());
        int space = elevatorRequest.getCapacity();
        long speed = (long) (elevatorRequest.getSpeed() * 1000);
        int info = elevatorRequest.getType().equals("building") ?
                0b1111111111 :
                elevatorRequest.getSwitchInfo();
        if (elevatorRequest.getType().equals("building")) {
            int index = elevatorRequest.getBuilding() - 'A';
            multiBuilding.getBuildings().get(index).addElevator(id, space, speed, info);
        } else if (elevatorRequest.getType().equals("floor")) {
            int index = elevatorRequest.getFloor() - 1;
            multiBuilding.getFloors().get(index).addElevator(id, space, speed, info);
        }
    }

    public static Building findBuilding(PersonRequest request) {
        synchronized (MultiBuilding.getMultiBuilding()) {
            PersonRequest personRequest = (PersonRequest) request;
            MultiBuilding multiBuilding = MultiBuilding.getMultiBuilding();
            boolean horizontal;
            int pos;
            char fromBuilding = personRequest.getFromBuilding();
            char toBuilding = personRequest.getToBuilding();
            if (fromBuilding == toBuilding) {
                horizontal = false;
                pos = fromBuilding - 'A' + 1;
            } else {
                int fromFloor = personRequest.getFromFloor();
                int toFloor = personRequest.getToFloor();
                if (multiBuilding.hasFloorElevator(fromFloor, fromBuilding, toBuilding)) {
                    horizontal = true;
                    pos = fromFloor;
                } else {
                    horizontal = false;
                    pos = fromBuilding - 'A' + 1;
                }
            }
            if (horizontal) {
                return multiBuilding.getFloors().get(pos - 1);
            } else {
                return multiBuilding.getBuildings().get(pos - 1);
            }
        }
    }
}
