package com.oocourse.spec1.exceptions;

public class MyEqualGroupIdException extends EqualGroupIdException {
    //  5
    private static Count count = new Count();
    private final int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        MyEqualGroupIdException.count.setCount(5);
        MyEqualGroupIdException.count.setSum(5, id);
    }

    public void print() {
        System.out.printf("egi-%d, %d-%d\n", count.getCount(5), id, count.getSum(5, id));
    }
}
