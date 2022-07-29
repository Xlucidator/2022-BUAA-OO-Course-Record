package com.oocourse.spec1.exceptions;

public class MyRelationNotFoundException extends RelationNotFoundException {
    //  2
    private static Count count = new Count();
    private final int id1;
    private final int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        MyRelationNotFoundException.count.setCount(2);
        MyRelationNotFoundException.count.setSum(2, id1);
        MyRelationNotFoundException.count.setSum(2, id2);
    }

    public void print() {
        System.out.printf("rnf-%d, %s-%d, %d-%d\n", count.getCount(2),
                id1, count.getSum(2, id1), id2, count.getSum(2, id2));
    }
}
