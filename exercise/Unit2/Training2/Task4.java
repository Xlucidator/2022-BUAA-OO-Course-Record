public class Task4 {
    public static Tray TRAY = new Tray();
    public static void main(String[] args) {
        Thread producer = new Producer();
        Thread consumer = new Thread(new Consumer());
        producer.start();
        //System.out.println("\033[32m" + "==== Depart ====" + "\033[0m");
        consumer.start();
    }
}

class Tray {
    private int content;
    private boolean full;

    Tray() {
        content = 0;
        full = false;
    }

    public synchronized void put(int content) {
        while (full) {
            try {
                //System.out.println("\t[Producer] nowhere for content " + content + "! wait for get...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.content = content;
        this.full = true;
        System.out.println("Producer put:" + content);
        notifyAll();
    }

    public synchronized void get() {
        while (!full) {
            try {
                //System.out.println("\t[Consumer] Nothing in tray! wait for put...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.full = false;
        System.out.println("Consumer get:" + content);
        notifyAll();
    }
}

class Producer extends Thread {
    public void run() {
        for (int i = 1; i <= 10; ++i) {
            //System.out.println("\033[32m" + "Start putting " + i + "\033[m");
            Task4.TRAY.put(i);
            try {
                //System.out.println("\033[32m" + "Put finished! sleep for a while..." + "\033[m");
                sleep((int) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("\033[32m" + "[switch to next]" + "\033[m");
        }
    }

}

class Consumer extends Thread {
    public void run() {
        for (int i = 1; i <= 10; ++i) {
            //System.out.println("\033[31m" + "Start Getting" + "\033[0m");
            Task4.TRAY.get();
            //System.out.println("\033[31m" + "Get finished!\n[switch to next]" + "\033[0m");
        }
    }
}