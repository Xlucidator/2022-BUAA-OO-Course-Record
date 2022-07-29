package main;

import com.oocourse.spec1.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int id;
    private static final Counter COUNTER = new Counter();
    private static final HashMap<Integer, Integer> EXCEPT_COUNTER = new HashMap<Integer, Integer>();
    
    public MyEqualGroupIdException(int id) {
        this.id = id;
        if (!EXCEPT_COUNTER.containsKey(id)) {
            EXCEPT_COUNTER.put(id, 0);
        }
    }
    
    @Override
    public void print() {
        COUNTER.increase();
        EXCEPT_COUNTER.put(id, EXCEPT_COUNTER.get(id) + 1);
        System.out.println("egi-" + COUNTER.getCounter() + ", "
                + id + "-" + EXCEPT_COUNTER.get(id));
    }
}
