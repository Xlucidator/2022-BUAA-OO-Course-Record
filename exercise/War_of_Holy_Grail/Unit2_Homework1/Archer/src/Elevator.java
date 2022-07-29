import com.oocourse.TimableOutput;

public class Elevator extends Thread {
    private Father father;
    private int position;
    private boolean upOrDown;
    private int id;
    private Request nowRequest;
    private int num;
    private boolean blocked;

    public Elevator(Father f, int i) {
        father = f;
        position = 1;
        upOrDown = false;
        id = i;
        nowRequest = null;
        num = 0;
        blocked = false;
    }

    public void open() {
        TimableOutput.println(String.format("OPEN-%c-%d-%d", (char)(id + 'A'), position, id + 1));
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
        TimableOutput.println(String.format("CLOSE-%c-%d-%d", (char)(id + 'A'), position, id + 1));
    }

    public void inOrOut() {
        if (nowRequest.isAlreadyIn()) {
            num--;
            TimableOutput.println(String.format("OUT-%d-%c-%d-%d",
                    nowRequest.getPersonId(), (char)(id + 'A'), position, id + 1));
            father.remind();
        } else {
            num++;
            TimableOutput.println(String.format("IN-%d-%c-%d-%d",
                    nowRequest.getPersonId(), (char)(id + 'A'), position, id + 1));
        }
    }

    public void move() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        position += upOrDown ? -1 : 1;
        TimableOutput.println(String.format("ARRIVE-%c-%d-%d", (char)(id + 'A'), position, id + 1));
    }

    @Override
    public void run() {
        while (true) {
            if (blocked && father.noRequest()) {
                break;
            }
            nowRequest = father.get(this, upOrDown, position, nowRequest, num == 6);
            /*if(nowRequest != null)
                System.out.println(position + " " + nowRequest.getFromFloor() + " "
                 + nowRequest.getToFloor() + " " + nowRequest.isAlreadyIn());
            else
                System.out.println(position + " null");*/
            if (blocked && father.noRequest()) {
                break;
            }
            if (nowRequest != null && nowRequest.getPurpose() == position) {
                open();
                inOrOut();
                father.remove(nowRequest);
                nowRequest = father.get(this, upOrDown, position, null, num == 6);
                while (nowRequest != null && nowRequest.getPurpose() == position) {
                    inOrOut();
                    father.remove(nowRequest);
                    nowRequest = father.get(this, upOrDown, position, null, num == 6);
                }
                close();
            } else if ((!upOrDown && position == 10) || (upOrDown && position == 1)) {
                father.reverse();
                upOrDown = !upOrDown;
            } else {
                move();
            }
        }
    }

    public boolean getBlock() {
        return blocked;
    }

    public void block() {
        blocked = true;
    }

    public void setUpOrDown(boolean upOrDown) {
        this.upOrDown = upOrDown;
    }
}
