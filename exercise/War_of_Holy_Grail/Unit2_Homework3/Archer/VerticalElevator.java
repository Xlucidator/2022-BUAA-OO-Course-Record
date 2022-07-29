import java.util.ArrayList;
import java.util.Iterator;

public class VerticalElevator extends Elevator {
    private final char building;
    private int floor;
    private int toFloor;
    private boolean direction;

    public VerticalElevator(PersonQueue waitQueue, int id, char building, int limit, int moveTime) {
        super(waitQueue, id, moveTime, limit);
        this.building = building;
        this.floor = 1;
        this.toFloor = floor;
        this.direction = true;
    }

    public void run() {
        while (true) {
            synchronized (super.getWaitQueue()) {
                if (super.getWaitQueue().isEmpty() && super.getPas().isEmpty()
                        && Controller.getInstance().getEndTag()) {
                    return;
                }
            }
            inout();
            look();
            move();
        }
    }

    private void move() {
        if (toFloor != floor) {
            try {
                sleep(super.getMoveTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (toFloor > floor) {
                floor++;
            } else {
                floor--;
            }
            SecureTimableOutput.println("ARRIVE-" + building + "-" + floor + "-" + super.getNum());
        }
    }

    private void changeDirection() {
        if (floor == 1) {
            direction = true;
        } else if (floor == 10) {
            direction = false;
        } else if (direction) {
            direction = false;
        } else {
            direction = true;
        }
        inout();
    }

    private void look() {
        if (super.getPas().isEmpty()) {
            synchronized (super.getWaitQueue()) {
                if (!super.getWaitQueue().isEmpty()) {
                    ArrayList<Person> people = super.getWaitQueue().getPeople();
                    boolean flag = false;
                    for (Person person : people) {
                        MyRequest request = person.getRequests().get(0);
                        if (request.getFromFloor() == floor) {
                            continue;
                        } else if (request.getFromFloor() > floor == direction) {
                            flag = true;
                            toFloor = request.getFromFloor();
                            break;
                        }
                    }
                    if (!flag) {
                        changeDirection();
                    }
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
        } else {
            if (toFloor > floor) {
                for (Person person : super.getPas()) {
                    if (person.getRequests().get(0).getToFloor() > toFloor) {
                        toFloor = person.getRequests().get(0).getToFloor();
                    }
                }
            } else if (toFloor < floor) {
                for (Person person : super.getPas()) {
                    if (person.getRequests().get(0).getToFloor() < toFloor) {
                        toFloor = person.getRequests().get(0).getToFloor();
                    }
                }
            } else {
                toFloor = super.getPas().get(0).getRequests().get(0).getToFloor();
                if (toFloor > floor != direction) {
                    changeDirection();
                }
            }
        }
    }

    private void inout() {
        boolean door = false;
        Iterator<Person> item = super.getPas().iterator();
        while (item.hasNext()) {
            Person person = item.next();
            MyRequest tmp = person.getRequests().get(0);
            if (tmp.getToFloor() == floor) {
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
                    if (direction == tmp.getDirection() && floor == tmp.getFromFloor()) {
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
