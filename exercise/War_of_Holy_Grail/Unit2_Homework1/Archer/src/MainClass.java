import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        Father[] fathers = new Father[5];
        Elevator[] elevators = new Elevator[5];
        Input input = new Input(fathers, elevators);
        TimableOutput.initStartTimestamp();
        input.start();
        for (int i = 0; i <= 4; i++) {
            fathers[i] = new Father();
            elevators[i] = new Elevator(fathers[i], i);
            elevators[i].start();
        }
    }
}
