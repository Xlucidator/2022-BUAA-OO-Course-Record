import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        RequestTables.init();
        Elevators.init();
        Input input = new Input();
        input.start();
    }
}
