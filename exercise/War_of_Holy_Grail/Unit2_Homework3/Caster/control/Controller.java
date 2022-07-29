package control;

import elevator.ElevatorState;
import requests.HorizontalReqQueue;
import requests.Req;
import requests.VerticalReqQueue;

public class Controller {
    private static final Controller CONTROLLER = new Controller();
    
    private Controller() {
    }
    
    // A、B、C、D、E：各楼座纵向请求等待队列
    private VerticalReqQueue[] verticalOutsides;
    // 1-10层楼，各层楼横向请求等待队列
    private HorizontalReqQueue[] horizontalOutsides;
    
    public static Controller getInstance() {
        return CONTROLLER;
    }
    
    public void init(VerticalReqQueue[] verticalOutsides
            , HorizontalReqQueue[] horizontalOutsides) {
        this.horizontalOutsides = horizontalOutsides;
        this.verticalOutsides = verticalOutsides;
    }
    
    public void addRequest(Req req) {
        // 线程不安全，但无需保护
        ElevatorState dir = setTransferWay(req);
        
        if (dir == ElevatorState.Up || dir == ElevatorState.Down) {
            verticalOutsides[req.getBuilding() - 'A'].addRequest(req.getFloor(), req);
        } else {
            horizontalOutsides[req.getFloor() - 1].addRequest(req.getBuilding(), req);
        }
    }
    
    public static ElevatorState setTransferWay(Req req) {
        // TODO: 没有根据电梯运行时间选择，也没有动态更改策略！
        if (!req.needTransfer()) {
            req.updateTempToPos(req.getEndBuilding(), req.getEndFloor());
            return req.getDirection();
        }
        if (req.getFloor() == 1) {
            req.updateTempToPos(req.getEndBuilding(), 1);
            return req.getDirection();
        }
        int downFloor = Math.min(req.getFloor(), req.getEndFloor());
        int upFloor = Math.max(req.getFloor(), req.getEndFloor());
        // 在最终楼层与当前楼层之间是否有横向可达电梯
        if (downFloor != upFloor) {
            for (int i = downFloor; i <= upFloor; i++) {
                if (HorizontalMap.getInstance().canReach(
                        i, req.getBuilding(), req.getEndBuilding())
                ) {
                    if (i == req.getFloor()) {  // 当前楼层就有横向电梯
                        req.updateTempToPos(req.getEndBuilding(), i);
                        return req.getDirection();
                    }
                    req.updateTempToPos(req.getBuilding(), i);
                    return req.getDirection();
                }
            }
        }
        // 在当前楼座的所有楼层查找最近的横向可达电梯
        for (int i = 1; (downFloor - i >= 1) || (upFloor + i <= 10); i++) {
            if (downFloor - i >= 1) {
                if (HorizontalMap.getInstance().canReach(
                        downFloor - i, req.getBuilding(), req.getEndBuilding())
                ) {
                    req.updateTempToPos(req.getBuilding(), downFloor - i);
                    return req.getDirection();
                }
            } else if (upFloor + i <= 10) {
                if (HorizontalMap.getInstance().canReach(
                        upFloor + i, req.getBuilding(), req.getEndBuilding())
                ) {
                    req.updateTempToPos(req.getBuilding(), upFloor + i);
                    return req.getDirection();
                }
            }
        }
        // 下面情况不应该发生，发生则有bug！
        req.updateTempToPos(req.getBuilding(), 1);
        return req.getDirection();
    }
}
