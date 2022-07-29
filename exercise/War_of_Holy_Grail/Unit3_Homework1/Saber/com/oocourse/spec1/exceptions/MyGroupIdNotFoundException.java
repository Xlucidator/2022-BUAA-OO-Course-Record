package com.oocourse.spec1.exceptions;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    //  4
    private static Count count = new Count();
    private final int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        MyGroupIdNotFoundException.count.setCount(4);
        MyGroupIdNotFoundException.count.setSum(4, id);
    }

    public void print() {
        System.out.printf("ginf-%d, %d-%d\n", count.getCount(4), id, count.getSum(4, id));
    }
}
