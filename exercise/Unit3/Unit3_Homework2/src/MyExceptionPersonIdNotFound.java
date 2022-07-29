import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

public class MyExceptionPersonIdNotFound extends PersonIdNotFoundException {
    private final int id;
    private static int cnt = 0;

    public MyExceptionPersonIdNotFound(int id) {
        this.id = id;
        ExceptionCounter.getInstance().update("pinf", id);
        ++ cnt;
    }

    @Override
    public void print() {
        System.out.println("pinf-" + cnt + ", " + id + "-" +
                ExceptionCounter.getInstance().getRecord("pinf", id));
    }
}
