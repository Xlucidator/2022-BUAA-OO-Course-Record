import com.oocourse.TimableOutput;

public class MainClass {
    
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        
        Dispatch dispatch = new Dispatch();
        
        for (int i = 1; i <= 5; i++) {
            ElevatorThread elevatorThread = new ElevatorThread(i, 400, 200, 200,
                    6, dispatch, 1, 1, 10);
            elevatorThread.start();
        }
        
        InputThread inputThread = new InputThread(dispatch);
        inputThread.start();
    }
}