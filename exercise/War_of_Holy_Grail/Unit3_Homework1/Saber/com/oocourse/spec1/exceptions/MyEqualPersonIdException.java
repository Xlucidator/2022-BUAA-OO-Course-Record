package com.oocourse.spec1.exceptions;

public class MyEqualPersonIdException extends EqualPersonIdException {
    //  1
    private static Count count = new Count();
    private final int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        MyEqualPersonIdException.count.setCount(1);
        MyEqualPersonIdException.count.setSum(1, id);
    }

    public void print() {
        System.out.printf("epi-%d, %d-%d\n", count.getCount(1), id, count.getSum(1, id));
    }
}
