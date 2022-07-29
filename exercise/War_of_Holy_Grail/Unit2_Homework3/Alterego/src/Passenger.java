import com.oocourse.elevator3.PersonRequest;

import java.util.LinkedList;
import java.util.Queue;

public class Passenger {
    private final PersonRequest originRequest;
    private final Queue<PersonRequest> requests;
    private final int id;

    Passenger(PersonRequest request) {
        originRequest = request;
        requests = new LinkedList<>();
        id = request.getPersonId();
        if (request.getFromBuilding() == request.getToBuilding()) {
            addRequest(request);
        } else if (request.getFromFloor() == request.getToFloor() &&
                Elevators.canTrans(request.getFromFloor(),
                        request.getFromBuilding(), request.getToBuilding())) {
            addRequest(request);
        } else {
            int id = request.getPersonId();
            int fromFloor = request.getFromFloor();
            int toFloor = request.getToFloor();
            char fromBuilding = request.getFromBuilding();
            char toBuilding = request.getToBuilding();

            if (Elevators.canTrans(fromFloor, fromBuilding, toBuilding)) {
                PersonRequest requestBuilding = new PersonRequest(fromFloor, fromFloor,
                        fromBuilding, toBuilding, id);
                PersonRequest requestFloor = new PersonRequest(fromFloor, toFloor,
                        toBuilding, toBuilding, id);
                addRequest(requestBuilding);
                addRequest(requestFloor);
            } else if (Elevators.canTrans(toFloor, fromBuilding, toBuilding)) {
                PersonRequest requestFloor = new PersonRequest(fromFloor, toFloor,
                        fromBuilding, fromBuilding, id);
                PersonRequest requestBuilding = new PersonRequest(toFloor, toFloor,
                        fromBuilding, toBuilding, id);
                addRequest(requestFloor);
                addRequest(requestBuilding);
            } else {
                int transFloor = Elevators.transFloor(fromFloor, toFloor, fromBuilding, toBuilding);
                PersonRequest requestFloor1 = new PersonRequest(fromFloor, transFloor,
                        fromBuilding, fromBuilding, id);
                PersonRequest requestBuilding = new PersonRequest(transFloor, transFloor,
                        fromBuilding, toBuilding, id);
                PersonRequest requestFloor2 = new PersonRequest(transFloor, toFloor,
                        toBuilding, toBuilding, id);
                addRequest(requestFloor1);
                addRequest(requestBuilding);
                addRequest(requestFloor2);
            }
        }
    }

    public synchronized boolean achieve() {
        notifyAll();
        return requests.isEmpty();
    }

    public synchronized void accomplishFirst() {
        if (requests.isEmpty()) {
            notifyAll();
            return;
        }
        requests.poll();
        notifyAll();
    }

    public synchronized PersonRequest firstRequest() {
        if (requests.isEmpty()) {
            notifyAll();
            return null;
        }
        PersonRequest r = requests.peek();
        notifyAll();
        return r;
    }

    public synchronized int getId() {
        notifyAll();
        return id;
    }

    private void addRequest(PersonRequest r) {
        if (r.getToFloor() == r.getFromFloor() &&
                r.getFromBuilding() == r.getToBuilding()) {
            return;
        }
        requests.offer(r);
    }
}
