package io;

import com.oocourse.TimableOutput;
import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import control.Controller;
import control.Counter;
import elevator.HorizontalElevator;
import elevator.VerticalElevator;
import requests.HorizontalReqQueue;
import requests.Req;
import requests.VerticalReqQueue;

import java.io.IOException;

public class Main {
    // A、B、C、D、E：各楼座纵向请求等待队列
    private static final VerticalReqQueue[] VERTICAL_OUTSIDES = new VerticalReqQueue[5];
    
    // 1-10层楼，各层楼横向请求等待队列
    private static final HorizontalReqQueue[] HORIZONTAL_OUTSIDES = new HorizontalReqQueue[10];
    
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        
        // 分配初始电梯
        Controller.getInstance().init(VERTICAL_OUTSIDES, HORIZONTAL_OUTSIDES);
        initialVertical();
        initialHorizontal();
        
        // 重复读取输入
        readIn();
        
        // 输入终止，等到所有任务处理完毕再对每个共享队列setEnd
        if (Counter.getInstance().allReqFinished()) {
            setEnd();
        }
    }
    
    public static void setEnd() {
        for (VerticalReqQueue queue : VERTICAL_OUTSIDES) {
            queue.setEnd();
        }
        for (HorizontalReqQueue queue : HORIZONTAL_OUTSIDES) {
            queue.setEnd();
        }
    }
    
    public static void readIn() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            } else {
                if (request instanceof PersonRequest) {  /* 乘客请求（包括横纵） */
                    Req personRq = new Req((PersonRequest) request);
                    Controller.getInstance().addRequest(personRq);
                    Counter.getInstance().acquire();
                } else if (request instanceof ElevatorRequest) {  /* 电梯请求（包括横纵） */
                    ElevatorRequest elevatorRq = (ElevatorRequest) request;
                    
                    if (elevatorRq.getType().equals("building")) {  // 如果是纵向电梯
                        VerticalReqQueue outsides = VERTICAL_OUTSIDES[
                                elevatorRq.getBuilding() - 'A'];
                        
                        VerticalElevator verticalElevator = new VerticalElevator(
                                elevatorRq.getBuilding(), elevatorRq.getElevatorId(), outsides
                                , elevatorRq.getCapacity(), (int) (elevatorRq.getSpeed() * 1000)
                        );
                        verticalElevator.setName("VerticalElevator-" + elevatorRq.getBuilding()
                                + "-" + elevatorRq.getElevatorId());
                        verticalElevator.start();
                    } else {  // 如果是横向电梯
                        int floor = elevatorRq.getFloor();
                        
                        HorizontalReqQueue outsides = HORIZONTAL_OUTSIDES[floor - 1];
                        HorizontalElevator horizontalElevator = new HorizontalElevator(
                                floor, elevatorRq.getElevatorId(), outsides
                                , elevatorRq.getCapacity(), (int) (elevatorRq.getSpeed() * 1000)
                                , elevatorRq.getSwitchInfo()
                        );
                        horizontalElevator.setName("HorizontalElevator-" + elevatorRq.getFloor()
                                + "-" + elevatorRq.getElevatorId());
                        horizontalElevator.start();
                    }
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void initialVertical() {
        /* 初始的5部纵向电梯 */
        char[] building = {'A', 'B', 'C', 'D', 'E'};
        for (int i = 0; i < 5; i++) {
            VerticalReqQueue outside = new VerticalReqQueue();
            VerticalElevator verticalElevator = new VerticalElevator(
                    building[i], i + 1, outside, 8, 600
            );
            verticalElevator.setName("VerticalElevator-" + building[i] + "-" + (i + 1));
            verticalElevator.start();
            VERTICAL_OUTSIDES[i] = outside;
        }
    }
    
    public static void initialHorizontal() {
        /* 初始的10层横向电梯等待队列，但初始横向电梯只有一层有，其他需要动态申请 */
        for (int i = 0; i < 10; i++) {
            HorizontalReqQueue horizontalOutside = new HorizontalReqQueue();
            HORIZONTAL_OUTSIDES[i] = horizontalOutside;
        }
        HorizontalElevator horizontalElevator = new HorizontalElevator(
                1, 6, HORIZONTAL_OUTSIDES[0], 8, 600, 31
        );
        horizontalElevator.setName("HorizontalElevator-1-6");
        horizontalElevator.start();
    }
}
