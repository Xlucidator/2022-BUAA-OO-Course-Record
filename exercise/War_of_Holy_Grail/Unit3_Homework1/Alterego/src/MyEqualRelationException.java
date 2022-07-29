import com.oocourse.spec1.exceptions.EqualRelationException;

import java.util.ArrayList;

public class MyEqualRelationException extends EqualRelationException {
    private final int id1;
    private final int id2;
    private static int sum = 0;
    private static ArrayList<Count> counts = new ArrayList<>();
    
    public MyEqualRelationException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        if (id1 == id2) {
            sum++;
            for (Count count : counts) {
                if (count.getId() == id1) {
                    count.setNum(count.getNum() + 1);
                    return;
                }
            }
            Count count = new Count();
            count.setId(id1);
            count.setNum(1);
            counts.add(count);
        } else {
            sum = sum + 1;
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
                count.setNum(1);
                count.setId(id1);
                counts.add(count);
            }
            if (flag2 == 0) {
                Count count = new Count();
                count.setNum(1);
                count.setId(id2);
                counts.add(count);
            }
        }
    }
    
    public void print() {
        if (id1 == id2) {
            for (Count count : counts) {
                if (count.getId() == id1) {
                    int num = count.getNum();
                    System.out.println("er-" + sum + ", " + id1 + "-" +
                            num + ", " + id2 + "-" + num);
                    return;
                }
            }
        } else {
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
                System.out.println("er-" + sum + ", " + id1 + "-" + num1 + ", " + id2 + "-" + num2);
            }
            if (id1 > id2) {
                System.out.println("er-" + sum + ", " + id2 + "-" + num2 + ", " + id1 + "-" + num1);
            }
        }
    }
}
