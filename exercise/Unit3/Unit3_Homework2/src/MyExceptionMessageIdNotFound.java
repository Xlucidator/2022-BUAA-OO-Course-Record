import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

public class MyExceptionMessageIdNotFound extends MessageIdNotFoundException {
    private final int id;
    private static int cnt = 0;

    public MyExceptionMessageIdNotFound(int id) {
        this.id = id;
        ExceptionCounter.getInstance().update("minf", id);
        ++ cnt;
    }

    @Override
    public void print() {
        System.out.println("minf-" + cnt + ", " + id + "-" +
                ExceptionCounter.getInstance().getRecord("minf", id));
    }
}
