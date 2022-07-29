import java.util.ArrayList;

public class Father {
    private ArrayList<Request> acceptedRequests;
    private ArrayList<Request> refusedRequests;
    private ArrayList<Request> newRequests;
    private ArrayList<Request> undicidedRequests;

    public Father() {
        acceptedRequests = new ArrayList<>();
        refusedRequests = new ArrayList<>();
        newRequests = new ArrayList<>();
        undicidedRequests = new ArrayList<>();
    }

    public boolean noRequest() {
        return acceptedRequests.isEmpty()
                && refusedRequests.isEmpty()
                && newRequests.isEmpty()
                && undicidedRequests.isEmpty();
    }

    public synchronized Request judge(
            Elevator elevator, boolean upOrDown, int position) {
        Request temp = null;
        for (Request i : refusedRequests) {
            if (temp == null) {
                temp = i;
            } else if ((i.getFromFloor() > temp.getFromFloor()) ^ upOrDown) {
                temp = i;
            }
        }
        if (temp != null) {
            if ((!upOrDown && temp.getFromFloor() <= position)
                    || (upOrDown && temp.getFromFloor() >= position)) {
                elevator.setUpOrDown(!upOrDown);
                reverse();
            }
            return temp.getFromFloor() == position ? temp : null;
        }
        return null;
    }

    public synchronized Request get(
            Elevator elevator, boolean upOrDown, int position, Request request, boolean full) {
        while (!elevator.getBlock() && noRequest()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Request temp = request;
        if (temp == null) {
            for (Request i : acceptedRequests) {
                if (temp == null) {
                    temp = i;
                } else {
                    int des = i.isAlreadyIn() ? i.getToFloor() : i.getFromFloor();
                    int tempDes = temp.isAlreadyIn() ? temp.getToFloor() : temp.getFromFloor();
                    if ((des < tempDes) ^ upOrDown) {
                        temp = i;
                    }
                }
            }
        }
        for (Request i : newRequests) {
            if ((!upOrDown && (i.getFromFloor() < position || i.getFromFloor() > i.getToFloor()))
                || (upOrDown && (i.getFromFloor() > position
                            || i.getFromFloor() < i.getToFloor()))) {
                refusedRequests.add(i);
            } else if (!i.isAlreadyIn() && full) {
                undicidedRequests.add(i);
            } else {
                acceptedRequests.add(i);
                if (temp == null) {
                    temp = i;
                } else {
                    int des = i.isAlreadyIn() ? i.getToFloor() : i.getFromFloor();
                    int tempDes = temp.isAlreadyIn() ? temp.getToFloor() : temp.getFromFloor();
                    if ((des < tempDes) ^ upOrDown) {
                        temp = i;
                    }
                }
            }
        }
        newRequests.clear();
        if (temp == null) {
            return judge(elevator, upOrDown, position);
        }
        return temp;
    }

    public synchronized void put(Request r) {
        if (r != null) {
            newRequests.add(r);
        }
        notifyAll();
    }

    public synchronized void remove(Request r) {
        if (r.isAlreadyIn()) {
            acceptedRequests.remove(r);
        } else {
            r.setAlreadyIn(true);
        }
    }

    public synchronized void remind() {
        newRequests.addAll(undicidedRequests);
        undicidedRequests.clear();
    }

    public synchronized void reverse() {
        newRequests.addAll(refusedRequests);
        refusedRequests.clear();
    }
}
