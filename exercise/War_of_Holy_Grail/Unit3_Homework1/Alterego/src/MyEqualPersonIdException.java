import com.oocourse.spec1.exceptions.EqualPersonIdException;

import java.util.ArrayList;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int id;
    private static int sum = 0;
    private static ArrayList<Count> counts = new ArrayList<>();
    
    public MyEqualPersonIdException(int id) {
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
                System.out.println("epi-" + sum + ", " + id + "-" + count.getNum());
                return;
            }
        }
    }
}
