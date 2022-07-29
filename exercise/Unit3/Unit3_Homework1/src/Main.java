import com.oocourse.spec1.main.Runner;

public class Main {
    public static void main(String[] args) throws Exception {
        ExceptionCounter.getInstance().initial();
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class);
        runner.run();
    }
}
