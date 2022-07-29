import java.util.ArrayList;
import java.util.Iterator;

public class TransverseElevator extends Elevator {
    private final int floor;
    private final int switchInfo;
    private char building;
    private char toBuilding;
    private boolean direction;  //true为顺时针，false为逆时针

    public TransverseElevator(PersonQueue waitQueue, int id, int floor,
                              int limit, int moveTime, int switchInfo) {
        super(waitQueue, id, moveTime, limit);
        this.floor = floor;
        this.building = 'A';
        this.toBuilding = building;
        this.direction = true;
        this.switchInfo = switchInfo;
    }

    public void run() {
        while (true) {
            synchronized (super.getWaitQueue()) {
                if (super.getWaitQueue().isEmpty() && super.getPas().isEmpty()
                        && Controller.getInstance().getEndTag()) {
                    return;
                }
            }
            if (((switchInfo >> (building - 'A')) & 1) == 1) {
                inout();
            }
            look();
            move();
        }
    }

    private void move() {
        if (toBuilding != building) {
            try {
                sleep(super.getMoveTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (direction) {
                if (building == 'E') {
                    building = 'A';
                } else {
                    building++;
                }
            } else {
                if (building == 'A') {
                    building = 'E';
                } else {
                    building--;
                }
            }
            SecureTimableOutput.println("ARRIVE-" + building + "-" + floor + "-" + super.getNum());
        }
    }

    private void look() {
        if (toBuilding == building && super.getPas().isEmpty()) {
            synchronized (super.getWaitQueue()) {
                if (!super.getWaitQueue().isEmpty()) {
                    ArrayList<Person> people = super.getWaitQueue().getPeople();
                    toBuilding = people.get(0).getRequests().get(0).getFromBuilding();
                    direction = judge(toBuilding, building);
                } else if (super.getWaitQueue().isEnd()) {
                    return;
                } else {
                    try {
                        //System.out.println(super.getNum() + "  挂起");
                        super.getWaitQueue().wait();
                        //System.out.println(super.getNum() + "  苏醒");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (!super.getPas().isEmpty()) {
            toBuilding = super.getPas().get(0).getRequests().get(0).getToBuilding();
            direction = judge(toBuilding, building);
        }
    }

    private boolean judge(char to, char now) {
        return (to - now + 5) % 5 < 3;
    }

    private void inout() {
        boolean door = false;
        Iterator<Person> item = super.getPas().iterator();
        while (item.hasNext()) {
            Person person = item.next();
            MyRequest tmp = person.getRequests().get(0);
            if (tmp.getToBuilding() == building) {
                if (!door) {
                    door = true;
                    open();
                }
                SecureTimableOutput.println("OUT-" + person.getId() + "-" +
                        building + "-" + floor + "-" + super.getNum());
                person.getRequests().remove(0);
                if (person.getRequests().isEmpty()) {
                    RequestCounter.getInstance().release();
                } else {
                    Controller.getInstance().addRequest(person);
                }
                item.remove();
            }
        }
        synchronized (super.getWaitQueue()) {
            ArrayList<Person> people = super.getWaitQueue().getPeople();
            ArrayList<Person> addPeople = new ArrayList<>();
            if (!people.isEmpty()) {
                Iterator<Person> iterator = people.iterator();
                int i = super.getLimit() - super.getPas().size();
                while (i > 0 && iterator.hasNext()) {
                    Person person = iterator.next();
                    MyRequest tmp = person.getRequests().get(0);
                    if (building == tmp.getFromBuilding() &&
                            ((switchInfo >> (tmp.getToBuilding() - 'A')) & 1) == 1) {
                        if (!door) {
                            door = true;
                            open();
                        }
                        addPeople.add(person);
                        SecureTimableOutput.println("IN-" + person.getId() + "-" +
                                building + "-" + floor + "-" + super.getNum());
                        iterator.remove();
                        i--;
                    }
                }
            }
            for (Person person : addPeople) {
                super.getPas().add(person);
            }
            super.getWaitQueue().setPeople(people);
        }
        if (door) {
            close();
        }
    }

    private void open() {
        SecureTimableOutput.println("OPEN-" + building + "-" + floor + "-" + super.getNum());
        try {
            Thread.sleep(super.getOpenTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            Thread.sleep(super.getCloseTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SecureTimableOutput.println("CLOSE-" + building + "-" + floor + "-" + super.getNum());
    }
}
