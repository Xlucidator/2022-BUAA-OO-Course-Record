import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private final int id;
    private final List<PersonRequest> order = new ArrayList<>();

    public Person(PersonRequest personRequest) {
        id = personRequest.getPersonId();
        order.add(personRequest);
    }

    public Person(PersonRequest personRequest, int transferFloor) {
        id = personRequest.getPersonId();
        if (personRequest.getFromBuilding() != personRequest.getToBuilding()) {
            if (personRequest.getFromFloor() != transferFloor) {
                order.add(new PersonRequest(
                        personRequest.getFromFloor(), transferFloor,
                        personRequest.getFromBuilding(), personRequest.getFromBuilding(),
                        personRequest.getPersonId()
                ));
            }
            order.add(new PersonRequest(
                    transferFloor, transferFloor,
                    personRequest.getFromBuilding(), personRequest.getToBuilding(),
                    personRequest.getPersonId()
            ));
            if (personRequest.getToFloor() != transferFloor) {
                order.add(new PersonRequest(
                        transferFloor, personRequest.getToFloor(),
                        personRequest.getToBuilding(), personRequest.getToBuilding(),
                        personRequest.getPersonId()
                ));
            }
        } else {
            order.add(personRequest);
        }
    }

    public int getId() {
        return id;
    }

    public PersonRequest getNewRequest() {
        return order.isEmpty() ? null : order.get(0);
    }

    public void removeOldRequest() {
        order.remove(0);
    }
}
