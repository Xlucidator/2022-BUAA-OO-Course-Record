import com.oocourse.spec1.exceptions.EqualPersonIdException;

public class MyExceptionEqualPersonId extends EqualPersonIdException {
    private final int id;
    private static int cnt = 0;

    public MyExceptionEqualPersonId(int id) {
        this.id = id;
        ExceptionCounter.getInstance().update("epi", id);
        ++ cnt;
    }

    @Override
    public void print() {
        System.out.println("epi-" + cnt + ", " + id + "-" +
                ExceptionCounter.getInstance().getRecord("epi", id));
    }
}
