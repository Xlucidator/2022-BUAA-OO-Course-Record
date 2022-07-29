import com.oocourse.spec2.exceptions.RelationNotFoundException;

public class MyExceptionRelationNotFound extends RelationNotFoundException {
    private final int id1;
    private final int id2;
    private static int cnt = 0;

    public MyExceptionRelationNotFound(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        ExceptionCounter.getInstance().update("rnf", id1);
        ExceptionCounter.getInstance().update("rnf", id2);
        ++ cnt;
    }

    @Override
    public void print() {
        System.out.println("rnf-" + cnt +
                ", " + id1 + "-" + ExceptionCounter.getInstance().getRecord("rnf", id1) +
                ", " + id2 + "-" + ExceptionCounter.getInstance().getRecord("rnf", id2)
        );
    }
}
