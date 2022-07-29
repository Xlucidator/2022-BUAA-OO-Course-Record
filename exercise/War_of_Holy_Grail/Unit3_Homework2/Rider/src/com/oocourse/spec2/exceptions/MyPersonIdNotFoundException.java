package com.oocourse.spec2.exceptions;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {

    private static Counter counter = new Counter();

    private static int id;

    public MyPersonIdNotFoundException(int id) {
        counter.addCount(id);
        MyPersonIdNotFoundException.id = id;
    }

    @Override
    public void print() {
        System.out.println(String.format("pinf-%d, %d-%d",
                counter.getTotalCount(), id, counter.getIdCount(id)));
    }
}
