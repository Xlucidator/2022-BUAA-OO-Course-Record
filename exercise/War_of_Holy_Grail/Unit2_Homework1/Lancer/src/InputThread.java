import com.oocourse.TimableOutput;
import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class InputThread implements Runnable {
    private Request[] requestlist;

    public InputThread(Request[] requestlist) {
        this.requestlist = requestlist;
    }

    @Override
    public void run() {
        //System.out.println("----------start"+Thread.currentThread().getName());
        Person p;
        int start;
        char fromBuild;
        TimableOutput.initStartTimestamp();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest personrequest = elevatorInput.nextPersonRequest();
            if (personrequest == null) {
                for (Request r : requestlist) {
                    synchronized (r) {
                        r.setFinished(true);
                        r.notifyAll();
                    }
                }
                break;
            } else {
                start = personrequest.getFromFloor();
                fromBuild = personrequest.getFromBuilding();
                p = new Person(personrequest);
                Request request = requestlist[fromBuild - 'A'];
                synchronized (request) {
                    request.add(start, p);
                    request.notifyAll();
                }
                //System.out.println("requestlist[D]:
                // "+requestlist[fromBuild-'A'].getWaitingList().size());
            }
        }
        //System.out.println("----------------------------input end");
    }
}
