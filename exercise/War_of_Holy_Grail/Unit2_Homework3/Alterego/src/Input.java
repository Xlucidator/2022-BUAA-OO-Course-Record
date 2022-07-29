import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

public class Input extends Thread {

    @Override
    public void run() {
        //System.out.println("Input running");
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                //System.out.println("Input finish");
                while (!PassengerCount.getInstance().over()) {
                    synchronized (PassengerCount.getInstance()) {
                        try {
                            PassengerCount.getInstance().wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                RequestTables.setEnd();
                break;
            } else {
                if (request instanceof PersonRequest) {
                    PassengerCount.getInstance().add();
                    RequestTables.addPassenger(new Passenger((PersonRequest) request));
                    //System.out.println("A PersonRequest:    " + request);
                } else if (request instanceof ElevatorRequest) {
                    //System.out.println("An ElevatorRequest: " + request);
                    Elevators.addElevator((ElevatorRequest) request);
                }

            }
        }
        try {
            elevatorInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
