public class Main {
    public static void main(String[] args) {
        //tray
        Request[] requestlist = new Request[5];
        for (int i = 0; i < 5; i++) {
            requestlist[i] = new Request();
        }
        InputThread input = new InputThread(requestlist);
        //consumer
        Elevator ele1 = new Elevator(1,1,'A',6,400,requestlist[0]);
        Elevator ele2 = new Elevator(1,2,'B',6,400,requestlist[1]);
        Elevator ele3 = new Elevator(1,3,'C',6,400,requestlist[2]);
        Elevator ele4 = new Elevator(1,4,'D',6,400,requestlist[3]);
        Elevator ele5 = new Elevator(1,5,'E',6,400,requestlist[4]);
        new Thread(ele1,"ele1").start();
        new Thread(ele2,"ele2").start();
        new Thread(ele3,"ele3").start();
        new Thread(ele4,"ele4").start();
        new Thread(ele5,"ele5").start();
        Thread inputThread = new Thread(input,"input");//Producer
        inputThread.start();
    }
}
