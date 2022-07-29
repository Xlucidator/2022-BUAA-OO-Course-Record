import com.oocourse.spec2.exceptions.EqualRelationException;

public class MyExceptionEqualRelation extends EqualRelationException {
    private final int id1;
    private final int id2;
    private static int cnt = 0;

    public MyExceptionEqualRelation(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        ExceptionCounter.getInstance().update("er", id1);
        if (id2 != id1) {
            ExceptionCounter.getInstance().update("er", id2);
        }
        ++ cnt;
    }

    @Override
    public void print() {
        System.out.println("er-" + cnt +
                ", " + id1 + "-" + ExceptionCounter.getInstance().getRecord("er", id1) +
                ", " + id2 + "-" + ExceptionCounter.getInstance().getRecord("er", id2)
        );
    }
}
