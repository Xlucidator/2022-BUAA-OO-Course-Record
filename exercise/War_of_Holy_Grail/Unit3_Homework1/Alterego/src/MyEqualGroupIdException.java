import com.oocourse.spec1.exceptions.EqualGroupIdException;

import java.util.ArrayList;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int id;
    private static int sum = 0;
    private static ArrayList<Count> counts = new ArrayList<>();
    
    public MyEqualGroupIdException(int id) {
        this.id = id;
        sum++;
        for (Count count : counts) {
            if (count.getId() == id) {
                count.setNum(count.getNum() + 1);
                return;
            }
        }
        Count count = new Count();
        count.setId(id);
        count.setNum(1);
        counts.add(count);
    }
    
    public void print() {
        for (Count count : counts) {
            if (count.getId() == id) {
                System.out.println("egi-" + sum + ", " + id + "-" + count.getNum());
                return;
            }
        }
    }
}
