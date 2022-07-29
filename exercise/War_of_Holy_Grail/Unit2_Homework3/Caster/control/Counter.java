package control;

public class Counter {
    private static final Counter COUNTER = new Counter();
    private int count;
    private boolean inputEnd = false;
    
    private Counter() {
        count = 0;
    }
    
    public static Counter getInstance() {
        return COUNTER;
    }
    
    public synchronized void acquire() {
        count++;
        //System.out.println("count(after acquire): " + count);
    }
    
    public synchronized void release(int num) {
        count -= num;
        if (count == 0 && inputEnd) {
            notifyAll();
        }
        //System.out.println("count(after release): " + count);
    }
    
    public synchronized boolean allReqFinished() {
        // 仅在主线程调用（输入完成后启动）
        inputEnd = true;
        while (count != 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
