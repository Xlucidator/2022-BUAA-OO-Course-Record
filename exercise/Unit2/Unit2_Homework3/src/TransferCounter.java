public class TransferCounter {
    private static final TransferCounter COUNTER = new TransferCounter();
    private int cnt;
    private RequestQueue waitingQueue;

    public void initial(RequestQueue waitingQueue) {
        this.cnt = 0;
        this.waitingQueue = waitingQueue;
    }

    public static TransferCounter getInstance() {
        return COUNTER;
    }

    public synchronized void add() {
        cnt += 1;
    }

    public synchronized void release() {
        cnt -= 1;
        //System.out.println("Release notify: cnt = " + cnt);
        notifyAll();

        // ScheduleThread may be blocked meanwhile because of RequestQueue.getOneRequest
        waitingQueue.check();
    }

    public synchronized void finish() {
        //System.out.println("Finish notify: cnt = " + cnt);
        notifyAll();

        // There's addRequest before finish, so needn't worry that ScheduleThread is blocked
    }

    public synchronized boolean isEnd() {
        if (cnt != 0) {
            try {
                //System.out.println("Wait... cnt = " + cnt);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("Awake cnt = " + cnt);
        return cnt == 0;
    }
}
