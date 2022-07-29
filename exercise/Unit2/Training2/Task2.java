public class Task2 {
    public static void main(String[] args) {
        Process1 process1 = new Process1();
        process1.start();
        System.out.println("Task2: " + Process1.staticVar + " " + process1.classVar);
        
        Process1 process11 = new Process1();
        process11.start();
        System.out.println("Task2: " + Process1.staticVar + " " + process11.classVar);

    }
}

class Process1 extends Thread {
    static int staticVar = 0;
    int classVar = 0;

    @Override
    public void run() {
        System.out.println("I'm running");
        add();
        System.out.println(staticVar + " " + classVar);
        add();
        System.out.println(staticVar + " " + classVar);
        System.out.println("I'm finished");
    }

    private void add() {
        int localVar = 0;
        staticVar++;
        classVar++;
        localVar++;
    }
}