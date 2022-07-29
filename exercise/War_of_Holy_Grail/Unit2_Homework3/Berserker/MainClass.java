import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        RequestController controller = RequestController.getRequestController();
        controller.start();
        //TimableOutput.initStartTimestamp();
        //for (int i = 1; i <= NUM_OF_BUILDINGS; ++i) {
        //    BUILDINGS.add(new Building(i));
        //}
        //RequestController controller = new RequestController(BUILDINGS);
        //new Thread(controller).start();
    }
}
