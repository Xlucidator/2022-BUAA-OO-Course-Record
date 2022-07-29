package com.oocourse.spec1.exceptions;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    // 0
    private static Count count = new Count();
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        MyPersonIdNotFoundException.count.setCount(0);
        MyPersonIdNotFoundException.count.setSum(0, id);
    }

    public void print() {
        System.out.printf("pinf-%d, %d-%d\n", count.getCount(0), id, count.getSum(0, id));
    }
}
