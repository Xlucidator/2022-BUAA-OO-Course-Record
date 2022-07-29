package com.oocourse.spec1.exceptions;

public class MyEqualRelationException extends EqualRelationException {
    //  3
    private static Count count = new Count();
    private final int id1;
    private final int id2;

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        MyEqualRelationException.count.setCount(3);
        if (id1 != id2) {
            MyEqualRelationException.count.setSum(3, id1);
            MyEqualRelationException.count.setSum(3, id2);
        } else {
            MyEqualRelationException.count.setSum(3, id1);
        }
    }

    public void print() {
        System.out.printf("er-%d, %d-%d, %d-%d\n", count.getCount(3),
                id1, count.getSum(3, id1), id2, count.getSum(3, id2));
    }

}
