import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class Controller {
    private Elevator elevator;
    private HashSet<Person> on;
    private HashSet<Person> off;

    public Controller(Elevator elevator) {
        this.elevator = elevator;
    }

    public HashSet<Person> getOn() {
        HashSet<Person> temp;
        temp = new HashSet<Person>();
        int curr = elevator.getCurrFloor();
        on = new HashSet<Person>();
        HashMap<Integer, HashSet<Person>> waiting = elevator.getWaiting();
        if (waiting.containsKey(curr)) {
            temp = waiting.get(curr);
            //grab fastest people in
            on.addAll(grab(temp));
        }
        elevator.changeCurr(on.size());
        return on;
    }

    public HashSet<Person> getOff() {
        HashMap<Integer, HashSet<Person>> passengers = elevator.getPassengers();
        int curr = elevator.getCurrFloor();
        off = new HashSet<Person>();
        if (passengers.containsKey(curr)) {
            off.addAll(passengers.get(curr));
            passengers.get(curr).forEach((p) -> {
                p.setFinished(true);
            });
            elevator.changeCurr(-off.size());
        }
        return off;
    }

    public void setDir() {
        HashMap<Integer, HashSet<Person>> waiting = elevator.getWaiting();
        //System.out.println("setting "+elevator.toString()+" "+waiting.size());
        Integer curr = elevator.getCurrFloor();
        int direction = elevator.getState();
        boolean hasUp = false;
        boolean hasDown = false;
        for (int key : waiting.keySet()) {
            if (key > curr) {
                hasUp = true;
            } else if (key < curr) {
                hasDown = true;
            }
        }
        //System.out.println("hasUp:"+hasUp);
        //System.out.println("hasDown:"+hasDown);
        //time to turn around(look for need)
        if (elevator.getPassengers().isEmpty()) {
            //System.out.println("empty");
            if (!hasDown && !hasUp) {
                synchronized (elevator.getRequest()) {
                    try {
                        synchronized (elevator.getRequest()) {
                            elevator.getRequest().wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!hasDown && hasUp) {
                direction = Constant.UP;
            }
            if (!hasUp && hasDown) {
                direction = Constant.DOWN;
            }
        }
        //arrive at the top
        if (curr == elevator.getMaxFloor()) {
            direction = Constant.DOWN;
        }
        //arrive at the bottom
        if (curr == elevator.getMinFloor()) {
            direction = Constant.UP;
        }
        //System.out.println(direction);
        elevator.setState(direction);
    }

    //whether it's needed to stop the elevator
    public boolean stop() {
        HashMap<Integer, HashSet<Person>> passengers = elevator.getPassengers();
        Integer curr = elevator.getCurrFloor();
        boolean hasOff = passengers.containsKey(curr);
        boolean hasOn = false;
        on = new HashSet<Person>();
        HashMap<Integer, HashSet<Person>> waiting = elevator.getWaiting();
        //if it is empty, same direction as most people
        if (elevator.isEmpty() && waiting.containsKey(curr)) {
            hasOn = true;
        }
        //if it is not empty nor full, grab people with same direction
        else if (waiting.containsKey(curr) & (elevator.getCurNum() -
                this.getOff().size() < elevator.getCapacity())) {
            for (Person p : waiting.get(curr)) {
                if (p.goUp() && elevator.getState() == Constant.UP) {
                    hasOn = true;
                } else if (!p.goUp() && elevator.getState() == Constant.DOWN) {
                    hasOn = true;
                }
            }
        }
        return hasOn || hasOff;
    }

    public HashSet<Person> grab(HashSet<Person> waitlist) {
        HashSet<Person> temp = new HashSet<Person>();
        ArrayList<Person> sameDir = new ArrayList<Person>();
        ArrayList<Person> pplUP = new ArrayList<Person>();
        ArrayList<Person> pplDown = new ArrayList<Person>();
        boolean up;
        for (Person person : waitlist) {
            if (elevator.getPassengers().isEmpty()) {
                if (person.goUp()) {
                    pplUP.add(person);
                } else {
                    pplDown.add(person);
                }
            } else {
                if (elevator.getState() == Constant.UP && person.goUp()) {
                    sameDir.add(person);
                } else if (elevator.getState() == Constant.DOWN && !person.goUp()) {
                    sameDir.add(person);
                }
            }
        }
        //if empty grab most people
        if (elevator.getPassengers().isEmpty()) {
            up = pplDown.size() < pplUP.size();
            sameDir.addAll(up ? pplUP : pplDown);
            if (!sameDir.isEmpty()) {
                elevator.setState(up ? Constant.UP : Constant.DOWN);
            }
        }
        //grab same direction fastest people
        else {
            //sort the same direction people by distance
            Collections.sort(sameDir, new Comparator<Person>() {
                @Override
                public int compare(Person o1, Person o2) {
                    return o1.getDistance() > o2.getDistance() ? 1 : -1;
                }
            });
        }
        int vacancy = elevator.getCapacity() - elevator.getCurNum();
        if (sameDir.size() <= vacancy) {
            temp.addAll(sameDir);
        } else {
            temp.addAll(sameDir.subList(0, vacancy));
        }

        return temp;
    }
}
