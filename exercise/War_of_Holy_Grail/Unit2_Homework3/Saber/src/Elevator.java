import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

import com.oocourse.elevator3.Request;

public class Elevator extends Thread {
    private Requestqueue waitQueue;
    private Requestqueue queue;
    private int id;
    private char name;
    private int begin;
    private int size;
    private int status;//0为静止，1为上升，2为下降
    private Requestqueue selfqueue;
    private int number;
    private int capacity;
    private double speed;
    private ArrayList<PersonRequest> customers = new ArrayList<>();
    private ArrayList<PersonRequest> in = new ArrayList<>();

    public Elevator() {
    }

    public int getid() {
        return id;
    }

    public int getcapacity() {
        return capacity;
    }

    public double getspeed() {
        return speed;
    }

    public void setcapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setspeed(double speed) {
        this.speed = speed;
    }

    public int[] getArr() {
        int[] a = new int[5];
        return a;
    }

    public void setnumber() {
        number++;
    }

    public void setnumbero(int num) {
        this.number = num;
    }

    public int getnumber() {
        return number;
    }

    public ArrayList<PersonRequest> getcustomers() {
        return customers;
    }

    public ArrayList<PersonRequest> getin() {
        return in;
    }

    public Requestqueue getQueue() {
        return queue;
    }

    public void setQueue(Requestqueue sselfqueue) {
        this.queue = sselfqueue;
    }

    public Requestqueue getselfqueue() {
        return selfqueue;
    }

    public void setselfqueue(Requestqueue sselfqueue) {
        this.selfqueue = sselfqueue;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Requestqueue getWaitQueue() {
        return this.waitQueue;
    }

    public void setWaitQueue(Requestqueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    public Elevator(Requestqueue waitQueue, Requestqueue que,
                    int i, int size, Requestqueue selfqueue, int capacity, double speed) {
        this.number = 0;
        this.waitQueue = waitQueue;
        this.queue = que;
        this.id = i;
        this.size = size;
        this.selfqueue = selfqueue;
        this.begin = 1;
        this.status = 1;
        this.capacity = capacity;
        this.speed = speed;
        if (this.size == 0) {
            name = 'A';
        } else if (this.size == 1) {
            name = 'B';
        } else if (this.size == 2) {
            name = 'C';
        } else if (this.size == 3) {
            name = 'D';
        } else {
            name = 'E';
        }
    }

    public void run() {
        while (true) {
            if (selfqueue.isEnd() && selfqueue.isEmpty() && customers.isEmpty()) {
                synchronized (selfqueue) {
                    if (!queue.isEmpty()) {
                        try {
                            selfqueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                }
                return;
            } else {
                ArrayList<PersonRequest> requests = selfqueue.getRequestscopy();
                if (requests == null) {
                    continue;
                } else {
                    if (status == 1) {
                        up(requests);
                    } else if (status == 2) {
                        down(requests);
                    } else {
                        for (PersonRequest request : requests) {
                            if (request.getFromFloor() < request.getToFloor()
                                    && request.getFromFloor() >= this.begin) {
                                this.status = 1;
                                break;
                            } else if (request.getFromFloor() > request.getToFloor()
                                    && request.getFromFloor() <= this.begin) {
                                this.status = 2;
                                break;
                            } else if (request.getFromFloor() < request.getToFloor()) {
                                down2(request);
                                break;
                            } else if (request.getFromFloor() > request.getToFloor()) {
                                for (begin = begin + 1; begin <= request.getFromFloor(); begin++) {
                                    arrive();
                                    if (customers.size() < this.capacity) {
                                        ArrayList<PersonRequest> requestss =
                                                selfqueue.getRequestscopy();
                                        if (requestss != null) {
                                            up(requestss);
                                        }
                                    }
                                }
                                begin--;
                                this.status = 2;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void down2(PersonRequest request) {
        for (begin = begin - 1; begin >= request.getFromFloor(); begin--) {
            arrive();
            if (customers.size() < this.capacity) {
                ArrayList<PersonRequest> requestss =
                        selfqueue.getRequestscopy();
                if (requestss != null) {
                    down(requestss);
                }
            }
        }
        begin++;
        this.status = 1;
    }

    public void down(ArrayList<PersonRequest> requests) {
        for (PersonRequest request : requests) {
            if (request.getFromFloor() > request.getToFloor() &&
                    request.getFromFloor() <= this.begin && customers.size() < this.capacity) {
                customers.add(request);
            }
        }
        for (PersonRequest request : customers) {
            selfqueue.getRequests().remove(request);
        }
        while (!customers.isEmpty()) {
            int judge = 0;
            ArrayList<PersonRequest> leave = new ArrayList<>();
            for (PersonRequest request : customers) {
                if ((request.getFromFloor() == begin && !in.contains(request))
                        || request.getToFloor() == begin) {
                    judge = 1;
                    break;
                }
            }
            if (judge == 1) {
                open();
                for (PersonRequest request : customers) {
                    if (request.getFromFloor() == begin && !in.contains(request)) {
                        in(request);
                        in.add(request);
                    }
                }
                for (PersonRequest request : customers) {
                    if (request.getToFloor() == begin) {
                        out(request);
                        leave.add(request);
                    }
                }
                huan(leave);
                close();
                for (PersonRequest request : leave) {
                    customers.remove(request);
                    number--;
                }
                if (customers.size() < this.capacity) {
                    ArrayList<PersonRequest> requestss = selfqueue.getRequestscopy();
                    if (requestss != null) {
                        down(requestss);
                    }
                }
            }
            if (customers.size() != 0) {
                begin--;
                arrive();
            }
        }
        this.status = 0;
    }

    public void up(ArrayList<PersonRequest> requests) {
        for (PersonRequest request : requests) {
            if (request.getFromFloor() < request.getToFloor() &&
                    request.getFromFloor() >= this.begin && customers.size() < this.capacity) {
                customers.add(request);
            }
        }
        for (PersonRequest request : customers) {
            selfqueue.getRequests().remove(request);
        }
        while (!customers.isEmpty()) {
            int judge = 0;
            ArrayList<PersonRequest> leave = new ArrayList<>();
            for (PersonRequest request : customers) {
                if ((request.getFromFloor() == begin && !in.contains(request))
                        || request.getToFloor() == begin) {
                    judge = 1;
                    break;
                }
            }
            if (judge == 1) {
                open();
                for (PersonRequest request : customers) {
                    if (request.getFromFloor() == begin && !in.contains(request)) {
                        in(request);
                        in.add(request);
                    }
                }
                for (PersonRequest request : customers) {
                    if (request.getToFloor() == begin) {
                        out(request);
                        leave.add(request);
                    }
                }
                huan(leave);
                close();
                for (PersonRequest request : leave) {
                    customers.remove(request);
                    number--;
                }
                if (customers.size() < this.capacity) {
                    ArrayList<PersonRequest> requestss = selfqueue.getRequestscopy();
                    if (requestss != null) {
                        up(requestss);
                    }
                }
            }
            if (customers.size() != 0) {
                begin++;
                arrive();
            }
        }
        this.status = 0;
    }

    public void huan(ArrayList<PersonRequest> leave) {
        synchronized (queue) {
            ArrayList<Request> tp = queue.getRequests();
            for (PersonRequest request : leave) {
                for (Request request2 : tp) {
                    PersonRequest t = (PersonRequest) request2;
                    if (t.getPersonId() == request.getPersonId()) {
                        waitQueue.addRequest(t);
                        queue.remove(request2);
                        break;
                    }
                }
            }
        }
    }

    public void in(PersonRequest request) {
        Output.output("IN-" + request.getPersonId() + "-" +
                this.name + "-" + this.begin + "-" + this.id);
    }

    public void out(PersonRequest request) {
        Output.output("OUT-" + request.getPersonId() + "-" +
                this.name + "-" + this.begin + "-" + this.id);
    }

    public void open() {
        Output.output("OPEN-" + this.name + "-" + this.begin + "-" + this.id);
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
        Output.output("CLOSE-" + this.name + "-" + this.begin + "-" + this.id);
    }

    public void arrive() {
        try {
            sleep((long) (this.speed * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Output.output("ARRIVE-" + this.name + "-" + this.begin + "-" + this.id);
    }

}
