import com.oocourse.spec3.exceptions.EqualMessageIdException;

public class MyExceptionEqualMessageId extends EqualMessageIdException {
    private final int id;
    private static int cnt = 0;

    public MyExceptionEqualMessageId(int id) {
        this.id = id;
        ExceptionCounter.getInstance().update("emi", id);
        ++ cnt;
    }

    @Override
    public void print() {
        System.out.println("emi-" + cnt + ", " + id + "-" +
                ExceptionCounter.getInstance().getRecord("emi", id));
    }
}
