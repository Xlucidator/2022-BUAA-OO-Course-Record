public class ElevatorOperator extends Thread {
    private static final long TIME_TO_OPEN = 200;
    private static final long TIME_TO_CLOSE = 200;
    //private static final long TIME_TO_MOVE_VERTICALLY = 400;
    //private static final long TIME_TO_MOVE_HORIZONTALLY = 200;

    private final ElevatorStatus status;
    private long timeToSleep = 0;
    private final long timeToMove;

    public ElevatorOperator(Elevator elevator, long speed) {
        status = elevator.getStatus();
        timeToMove = speed;
        this.setName(elevator.getStatus().getId());
    }

    public long getSpeed() {
        return timeToMove;
    }

    @Override
    public void run() {
        while (true) {
            boolean out = false;
            timeToSleep = 0;
            //statusLock.lock();
            synchronized (status.getBuilding()) {
                ElevatorStatus.State state = status.getState();
                switch (state) {
                    case closed :
                        out = onClosed();
                        break;
                    case opened:
                        onOpened();
                        break;
                    case opening:
                        onOpening();
                        break;
                    case closing:
                        onClosing();
                        break;
                    case posGoing:
                        onPosGoing();
                        break;
                    case negGoing:
                        onNegGoing();
                        break;
                    default:
                        break;
                }
                //status.getBuilding().notifyAll();
            }
            //statusLock.unlock();
            if (out) {
                break;
            }
            try {
                Thread.sleep(timeToSleep);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Return true if the elevator has been turned down.
    // Wait when no requests are waiting to be handled.
    private boolean onClosed() {
        int pos = status.getPos();
        int destination = status.getDestination();
        if (pos == destination) {
            open();
        } else if (destination == 0) {
            if (status.isTurnedDown()) {
                return true;
            }
            synchronized (Shutter.getShutter()) {
                Shutter.getShutter().decrease();
                Shutter.getShutter().notifyAll();
            }
            try {
                status.getBuilding().wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (Shutter.getShutter()) {
                Shutter.getShutter().increase();
            }
        } else if (pos > destination) {
            if (status.isHorizontal()) {
                int negDistance = pos - destination;
                int posDistance = destination - pos + Building.getNumOfBuildings();
                if (posDistance > negDistance) { goNeg(); }
                else { goPos(); }
            } else { goNeg(); }
        } else {
            if (status.isHorizontal()) {
                int posDistance = destination - pos;
                int negDistance = pos - destination + Building.getNumOfBuildings();
                if (posDistance > negDistance) { goNeg(); }
                else { goPos(); }
            } else { goPos(); }
        }
        return false;
    }

    private void onOpened() {
        /*Building building = elevator.getBuilding();
        synchronized (building) {
            Policy policy = building.getPolicy();
            policy.getOut(status);
            policy.adjust();
            policy.getIn(status);
            policy.adjust();
        }*/
        close();
    }

    private void onOpening() {
        status.setState(ElevatorStatus.State.opened);
    }

    private void onClosing() {
        //synchronized (status.getBuilding()) {
        Policy policy = status.getBuilding().getPolicy();
        policy.getIn(status);
        policy.adjust();
        //}
        status.setState(ElevatorStatus.State.closed);
        status.close();
    }

    private void onPosGoing() {
        status.setState(ElevatorStatus.State.closed);
        status.goPos();
    }

    private void onNegGoing() {
        status.setState(ElevatorStatus.State.closed);
        status.goNeg();
    }

    private void open() {
        status.setState(ElevatorStatus.State.opening);
        status.open();
        timeToSleep = TIME_TO_OPEN;
        //synchronized (status.getBuilding()) {
        Policy policy = status.getBuilding().getPolicy();
        policy.getOut(status);
        policy.adjust();
        //}
    }

    private void close() {
        status.setState(ElevatorStatus.State.closing);
        timeToSleep = TIME_TO_CLOSE;
    }

    private void goNeg() {
        status.setState(ElevatorStatus.State.negGoing);
        timeToSleep = timeToMove;
    }

    private void goPos() {
        status.setState(ElevatorStatus.State.posGoing);
        timeToSleep = timeToMove;
    }
}
