import com.oocourse.spec3.exceptions.EqualGroupIdException;

public class MyExceptionEqualGroupId extends EqualGroupIdException {
    private final int id;
    private static int cnt = 0;

    public MyExceptionEqualGroupId(int id) {
        this.id = id;
        ExceptionCounter.getInstance().update("egi", id);
        ++ cnt;
    }

    @Override
    public void print() {
        System.out.println("egi-" + cnt + ", " + id + "-" +
                ExceptionCounter.getInstance().getRecord("egi", id));
    }
}
