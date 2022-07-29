import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.Objects;
import java.util.ArrayList;

import com.oocourse.elevator3.ElevatorRequest;

import java.util.Arrays;

public class Schedule extends Thread {
    private Requestqueue waitQueue;
    private ArrayList<Elevator> elevators;
    private Requestqueue passengers;

    public Schedule(Requestqueue wait, ArrayList<Elevator> elevators, Requestqueue passengers) {
        this.waitQueue = wait;
        this.elevators = elevators;
        this.passengers = passengers;
    }

    public void run() {
        while (true) {
            if (this.waitQueue.isEmpty() && this.waitQueue.isEnd()) {
                for (int i = 0; i < elevators.size(); i++) {
                    elevators.get(i).getselfqueue().setEnd(true);
                }
                if (!passengers.isEmpty()) {
                    try {
                        synchronized (waitQueue) {
                            waitQueue.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                return;
            } else {
                Request request = waitQueue.getRequest();
                if (request == null) {
                    continue;
                }
                if (request instanceof PersonRequest) {
                    PersonRequest temp = (PersonRequest) request;
                    if (temp.getFromBuilding() == temp.getToBuilding()) {
                        function(temp.getFromBuilding() - 'A', temp);
                    } else {
                        int floor = temp.getFromFloor();
                        int floorend = temp.getToFloor();
                        char begin = temp.getFromBuilding();
                        char end = temp.getToBuilding();
                        if (floor == floorend) {
                            cal(begin, end, floor, temp);
                        } else {
                            huancheng(temp);
                        }
                    }
                } else if (request instanceof ElevatorRequest) {
                    ElevatorRequest re = (ElevatorRequest) request;
                    if (Objects.equals(re.getType(), "building")) {
                        Elevator elevator = new Elevator(waitQueue, passengers, re.getElevatorId(),
                                re.getBuilding() - 'A',
                                new Requestqueue(), re.getCapacity(), re.getSpeed());
                        elevators.add(elevator);
                        elevator.start();
                    } else {
                        Elevator elevator = new Elevatorcross(waitQueue, passengers,
                                re.getElevatorId(), re.getFloor() + 4, new Requestqueue(),
                                re.getCapacity(), re.getSpeed(), re.getSwitchInfo());
                        elevators.add(elevator);
                        elevator.start();
                    }
                }
            }
        }
    }

    public void cal(int begin, int end, int floor, PersonRequest temp) {
        int i;
        for (i = 5; i < elevators.size(); i++) {
            if (elevators.get(i).getSize() == floor + 4) {
                int[] a = elevators.get(i).getArr();
                if (a[begin - 'A'] == 1 && a[end - 'A'] == 1) {
                    function(floor + 4, temp);
                    break;
                }
            }
        }
        if (i == elevators.size()) {
            huancheng(temp);
        }
    }

    public void function(int i, PersonRequest request) {
        int num = 0;
        int tp;
        int judge = 0;
        Elevator t = elevators.get(0);
        if (i < 5) {
            for (Elevator lift : elevators) {
                if (lift.getSize() == i && judge == 0) {
                    num = lift.getnumber();
                    t = lift;
                    judge = 1;
                } else if (lift.getSize() == i && judge == 1) {
                    if ((tp = lift.getnumber()) < num) {
                        num = tp;
                        t = lift;
                    }
                }
            }
            t.getselfqueue().addRequest(request);
            t.setnumber();
        } else {
            char begin = request.getFromBuilding();
            char end = request.getToBuilding();
            for (Elevator lift : elevators) {
                if (lift.getSize() == i && judge == 0) {
                    int[] a = lift.getArr();
                    if (a[begin - 'A'] == 1 && a[end - 'A'] == 1) {
                        num = lift.getnumber();
                        t = lift;
                        judge = 1;
                    }
                } else if (lift.getSize() == i && judge == 1) {
                    int[] a = lift.getArr();
                    if (a[begin - 'A'] == 1 && a[end - 'A'] == 1) {
                        if ((tp = lift.getnumber()) < num) {
                            num = tp;
                            t = lift;
                        }
                    }
                }
            }
            t.getselfqueue().addRequest(request);
            t.setnumber();
        }
    }

    public void huancheng(PersonRequest request) {
        char begin = request.getFromBuilding();
        char end = request.getToBuilding();
        int floor = request.getFromFloor();
        int floor2 = request.getToFloor();
        int id = request.getPersonId();
        int floormiddle = 0;
        int[] a = new int[10];
        int[] b = new int[10];
        for (int i = 1; i <= 10; i++) {
            a[i - 1] = Math.abs(i - floor) + Math.abs(i - floor2);
        }
        System.arraycopy(a, 0, b, 0, 10);
        Arrays.sort(a);
        int judge = 0;
        for (int i = 0; i < 10; i++) {
            if (judge == 1) {
                break;
            }
            for (int j = 1; j <= 10; j++) {
                if (judge == 1) {
                    break;
                }
                if (a[i] == b[j - 1]) {
                    int size = j + 4;
                    for (Elevator lift : elevators) {
                        if (lift.getSize() == size) {
                            int[] c = lift.getArr();
                            if (c[begin - 'A'] == 1 && c[end - 'A'] == 1) {
                                judge = 1;
                                floormiddle = j;
                                break;
                            }
                        }
                    }
                }
            }
        }
        synchronized (passengers) {
            if (floor == floormiddle) {
                PersonRequest request1 = new PersonRequest(floor, floor, begin, end, id);
                PersonRequest request2 = new PersonRequest(floor, floor2, end, end, id);
                function(floor + 4, request1);
                passengers.addRequest(request2);
            } else if (floor2 == floormiddle) {
                PersonRequest request1 = new PersonRequest(floor, floor2, begin, begin, id);
                PersonRequest request2 = new PersonRequest(floor2, floor2, begin, end, id);
                function(begin - 'A', request1);
                passengers.addRequest(request2);
            } else {
                PersonRequest request1 = new PersonRequest(floor, floormiddle, begin, begin, id);
                PersonRequest request2 =
                        new PersonRequest(floormiddle, floormiddle, begin, end, id);
                PersonRequest request3 = new PersonRequest(floormiddle, floor2, end, end, id);
                function(begin - 'A', request1);
                passengers.addRequest(request2);
                passengers.addRequest(request3);
            }
        }
    }
}