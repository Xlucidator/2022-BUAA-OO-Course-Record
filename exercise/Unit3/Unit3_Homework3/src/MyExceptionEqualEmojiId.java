import com.oocourse.spec3.exceptions.EqualEmojiIdException;

public class MyExceptionEqualEmojiId extends EqualEmojiIdException {
    private final int id;
    private static int cnt = 0;

    public MyExceptionEqualEmojiId(int id) {
        this.id = id;
        ExceptionCounter.getInstance().update("eei", id);
        ++ cnt;
    }

    @Override
    public void print() {
        System.out.println("eei-" + cnt + ", " + id + "-" +
                ExceptionCounter.getInstance().getRecord("eei", id));
    }
}
