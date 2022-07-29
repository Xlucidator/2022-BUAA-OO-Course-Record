public class PassengerCount {
    private static int count = 0;
    private static volatile PassengerCount singleTon;

    private PassengerCount() {
    }

    public static PassengerCount getInstance() {
        if (singleTon == null) {
            synchronized (PassengerCount.class) {
                if (singleTon == null) {
                    singleTon = new PassengerCount();
                }
            }
        }
        return singleTon;
    }

    public synchronized void add() {
        count++;
        notifyAll();
    }

    public synchronized void minus() {
        count--;
        notifyAll();
    }

    public synchronized boolean over() {
        notifyAll();
        return count == 0;
    }
}
