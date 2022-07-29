import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

public class MyExceptionGroupIdNotFound extends GroupIdNotFoundException {
    private final int id;
    private static int cnt = 0;

    public MyExceptionGroupIdNotFound(int id) {
        this.id = id;
        ExceptionCounter.getInstance().update("ginf", id);
        ++ cnt;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + cnt + ", " + id + "-" +
                ExceptionCounter.getInstance().getRecord("ginf", id));
    }
}
