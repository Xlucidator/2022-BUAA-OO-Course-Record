import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Elevator extends Thread {
    private final ElevatorPattern controller;
    private final PersonList personList;
    private final WaitQueue waitQueue;
    private final int maxFloor = 10;
    private final int minFloor = 1;
    private int floor = 1;
    private final int id;
    private final char buildingSign;
    private int runTime = 400;
    private State state;

    public enum State {
        WAIT, UP, DOWN, IN_AND_OUT
    }

    public Elevator(int id, char buildingSign, WaitQueue waitQueue) {
        this.id = id;
        this.buildingSign = buildingSign;
        this.waitQueue = waitQueue;
        int size = 6;
        this.personList = new PersonList(size);
        this.controller = new NormalPattern(waitQueue, personList, maxFloor, minFloor);
        state = State.WAIT;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (waitQueue) {
                if (waitQueue.isEnd() && waitQueue.isEmpty() && personList.isEmpty()) {
                    break;
                } else if (waitQueue.isEmpty() && personList.isEmpty()) {
                    state = State.WAIT;
                    try {
                        waitQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    state = controller.getState(state, floor);
                }
                try {
                    switch (state) {
                        case UP:
                            moveUp();
                            break;
                        case DOWN:
                            moveDown();
                            break;
                        case IN_AND_OUT:
                            inAndOut();
                            break;
                        default:
                            break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void moveUp() throws InterruptedException {
        sleep(runTime);
        floor++;
        TimableOutput.println("ARRIVE-" + buildingSign + "-" + floor + "-" + id);
    }

    private void moveDown() throws InterruptedException {
        sleep(runTime);
        floor--;
        TimableOutput.println("ARRIVE-" + buildingSign + "-" + floor + "-" + id);
    }

    private void inAndOut() throws InterruptedException {
        doorOpen();
        peopleOut();
        sleep(controller.getOpenTime(floor));
        peopleIn();
        doorClose();
    }

    private void doorOpen() throws InterruptedException {
        TimableOutput.println("OPEN-" + buildingSign + "-" + floor + "-" + id);
        sleep(200);
    }

    private void peopleOut() {
        ArrayList<PersonRequest> temp = new ArrayList<>();
        for (PersonRequest person : personList.getPersonList()) {
            if (person.getToFloor() == floor) {
                temp.add(person);
            }
        }
        for (PersonRequest person : temp) {
            TimableOutput.println("OUT-" + person.getPersonId()
                    + "-" + buildingSign + "-" + floor + "-" + id);
            personList.removePerson(person);
        }
    }

    private void peopleIn() {
        synchronized (waitQueue) {
            ArrayList<PersonRequest> temp = controller.getPeopleIn(floor);
            for (PersonRequest person : temp) {
                TimableOutput.println("IN-" + person.getPersonId()
                        + "-" + buildingSign + "-" + floor + "-" + id);
                personList.addPerson(person);
                waitQueue.removeRequest(person);
            }
        }
    }

    private void doorClose() throws InterruptedException {
        sleep(200);
        TimableOutput.println("CLOSE-" + buildingSign + "-" + floor + "-" + id);
    }
}
