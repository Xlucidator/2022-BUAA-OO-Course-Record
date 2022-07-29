import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

public class MyExceptionEmojiIdNotFound extends EmojiIdNotFoundException {
    private final int id;
    private static int cnt = 0;

    public MyExceptionEmojiIdNotFound(int id) {
        this.id = id;
        ExceptionCounter.getInstance().update("einf", id);
        ++ cnt;
    }

    @Override
    public void print() {
        System.out.println("einf-" + cnt + ", " + id + "-" +
                ExceptionCounter.getInstance().getRecord("einf", id));
    }
}
