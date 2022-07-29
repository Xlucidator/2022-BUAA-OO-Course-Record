import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;

public class InputThread extends Thread {
    private final PersonQueue[] waitQueues;

    public InputThread(PersonQueue[] waitQueues) {
        this.waitQueues = waitQueues;
    }

    @Override
    public void run() {
        int requestNum = 0;
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        Controller.getInstance().initial(waitQueues);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            }
            Controller.getInstance().parse(request);
            if (request instanceof PersonRequest) {
                requestNum++;
            }
        }
        for (int i = 0; i < requestNum; ++i) { //查收所有的任务
            RequestCounter.getInstance().acquire();
        }
        Controller.getInstance().setEndTag(); //标记所有任务完成
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}