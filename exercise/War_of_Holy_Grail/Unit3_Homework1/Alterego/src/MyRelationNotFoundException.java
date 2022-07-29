import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.ArrayList;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int id1;
    private final int id2;
    private static int sum = 0;
    private static ArrayList<Count> counts = new ArrayList<>();
    
    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        sum++;
        int flag1 = 0;
        int flag2 = 0;
        for (Count count : counts) {
            if (count.getId() == id1) {
                count.setNum(count.getNum() + 1);
                flag1 = 1;
            }
            if (count.getId() == id2) {
                count.setNum(count.getNum() + 1);
                flag2 = 1;
            }
            if (flag1 == 1 && flag2 == 1) {
                return;
            }
        }
        if (flag1 == 0) {
            Count count = new Count();
            count.setId(id1);
            count.setNum(1);
            counts.add(count);
        }
        if (flag2 == 0) {
            Count count = new Count();
            count.setId(id2);
            count.setNum(1);
            counts.add(count);
        }
    }
    
    public void print() {
        int num1 = 0;
        int num2 = 0;
        for (Count count : counts) {
            if (count.getId() == id1) {
                num1 = count.getNum();
            }
            if (count.getId() == id2) {
                num2 = count.getNum();
            }
        }
        if (id1 <= id2) {
            System.out.println("rnf-" + sum + ", " + id1 + "-" + num1 + ", " + id2 + "-" + num2);
        }
        if (id1 > id2) {
            System.out.println("rnf-" + sum + ", " + id2 + "-" + num2 + ", " + id1 + "-" + num1);
        }
    }
}
