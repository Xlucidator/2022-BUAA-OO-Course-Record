import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.ArrayList;

public class Elevatorcross extends Elevator {
    private int building;
    private int endbuilding;
    private int floor;
    private int[] arr = new int[5];

    public Elevatorcross(Requestqueue waitQueue,
                         Requestqueue que, int i, int size, Requestqueue selfqueue,
                         int capacity, double speed, int sw) {
        this.setselfqueue(selfqueue);
        this.setQueue(que);
        this.setWaitQueue(waitQueue);
        this.setId(i);
        this.setSize(size);
        this.setcapacity(capacity);
        this.setspeed(speed);
        this.building = 0;
        setnumbero(0);
        this.floor = size - 4;
        int sw2 = sw;
        for (int j = 0; j < 5; j++) {
            arr[j] = sw2 % 2;
            sw2 = sw2 / 2;
        }
    }

    public int[] getArr() {
        return arr;
    }

    public void run() {
        while (true) {
            if (getselfqueue().isEnd() && getselfqueue().isEmpty() && getcustomers().isEmpty()) {
                synchronized (getselfqueue()) {
                    if (!getQueue().isEmpty()) {
                        try {
                            getselfqueue().wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                }
                return;
            }
            ArrayList<PersonRequest> requests = getselfqueue().getRequestscopy();
            if (requests == null) {
                continue;
            }
            tiaodu(requests);
        }
    }

    public void tiaodu(ArrayList<PersonRequest> requests) {
        for (PersonRequest request : requests) {
            if (request.getFromBuilding() - 'A' == this.building &&
                    getcustomers().size() < this.getcapacity()) {
                getcustomers().add(request);
            }
        }
        for (PersonRequest request : getcustomers()) {
            getselfqueue().getRequests().remove(request);
        }
        if (getcustomers().isEmpty()) {
            endbuilding = requests.get(0).getFromBuilding() - 'A';
            arrive();
        }
        while (!getcustomers().isEmpty()) {
            int judge = 0;
            ArrayList<PersonRequest> leave = new ArrayList<>();
            for (PersonRequest request : getcustomers()) {
                if ((request.getFromBuilding() - 'A' == building &&
                        !getin().contains(request)) || request.getToBuilding() - 'A' == building) {
                    judge = 1;
                    break;
                }
            }
            if (judge == 1) {
                open();
                for (PersonRequest request : getcustomers()) {
                    if (request.getFromBuilding() - 'A' == building && !getin().contains(request)) {
                        in(request);
                        getin().add(request);
                    }
                }
                for (PersonRequest request : getcustomers()) {
                    if (request.getToBuilding() - 'A' == building && getin().contains(request)) {
                        out(request);
                        leave.add(request);
                    }
                }
                huan(leave);
                close();
                for (PersonRequest request : leave) {
                    getcustomers().remove(request);
                    setnumbero(getnumber() - 1);
                }
                if (getcustomers().size() < this.getcapacity()) {
                    ArrayList<PersonRequest> requestss = getselfqueue().getRequestscopy();
                    if (requestss != null) {
                        tiaodu(requestss);
                    }
                }
            }
            if (getcustomers().size() != 0) {
                endbuilding = getcustomers().get(0).getToBuilding() - 'A';
                arrive();
            }
        }
    }

    public void in(PersonRequest request) {
        Output.output("IN-" + request.getPersonId() + "-" +
                (char) ('A' + this.building) + "-" + this.floor + "-" + this.getid());
    }

    public void out(PersonRequest request) {
        Output.output("OUT-" + request.getPersonId() + "-" +
                (char) ('A' + this.building) + "-" + this.floor + "-" + this.getid());
    }

    public void open() {
        Output.output("OPEN-" +
                (char) ('A' + this.building) + "-" + this.floor + "-" + this.getid());
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Output.output("CLOSE-" +
                (char) ('A' + this.building) + "-" + this.floor + "-" + this.getid());
    }

    public void arrive() {
        if (building == endbuilding) {
            return;
        }
        if ((endbuilding > building && endbuilding - building <= 2) ||
                (endbuilding < building && building - endbuilding > 2)) {
            try {
                sleep((long) (this.getspeed() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            building = (building + 1) % 5;
            Output.output("ARRIVE-" +
                    (char) ('A' + this.building) + "-" + this.floor + "-" + this.getid());
        } else {
            try {
                sleep((long) (this.getspeed() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            building = building - 1;
            if (building < 0) {
                building = building + 5;
            }
            building = building % 5;
            Output.output("ARRIVE-" +
                    (char) ('A' + this.building) + "-" + this.floor + "-" + this.getid());
        }
        if (getcustomers().size() < this.getcapacity()) {
            ArrayList<PersonRequest> requestss = getselfqueue().getRequestscopy();
            if (requestss != null) {
                tiaodu(requestss);
            }
        }
        arrive();
    }

    public void huan(ArrayList<PersonRequest> leave) {
        synchronized (this.getQueue()) {
            ArrayList<Request> tp = this.getQueue().getRequests();
            for (PersonRequest request : leave) {
                for (Request request2 : tp) {
                    PersonRequest t = (PersonRequest) request2;
                    if (t.getPersonId() == request.getPersonId()) {
                        this.getWaitQueue().addRequest(t);
                        this.getQueue().remove(t);
                        break;
                    }
                }
            }
        }
    }
}
