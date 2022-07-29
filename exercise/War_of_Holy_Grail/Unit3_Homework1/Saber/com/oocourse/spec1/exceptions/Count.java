package com.oocourse.spec1.exceptions;

import java.util.ArrayList;
import java.util.HashMap;

public class Count {
    private int[] count = new int[]{0, 0, 0, 0, 0, 0};

    private HashMap<Integer, Integer> num1 = new HashMap<>();
    private HashMap<Integer, Integer> num2 = new HashMap<>();
    private HashMap<Integer, Integer> num3 = new HashMap<>();
    private HashMap<Integer, Integer> num4 = new HashMap<>();
    private HashMap<Integer, Integer> num5 = new HashMap<>();
    private HashMap<Integer, Integer> num6 = new HashMap<>();
    private ArrayList<HashMap<Integer, Integer>> list;

    Count() {
        this.list = new ArrayList<>();
        this.list.add(num1);
        this.list.add(num2);
        this.list.add(num3);
        this.list.add(num4);
        this.list.add(num5);
        this.list.add(num6);
    }

    public void setCount(int index) {
        this.count[index]++;
    }

    public void setSum(int index, int id) {
        if (!this.list.get(index).containsKey(id)) {
            this.list.get(index).put(id, 1);
        } else {
            int x = this.list.get(index).get(id);
            this.list.get(index).put(id, x + 1);
        }
    }

    public int getCount(int index) {
        return this.count[index];
    }

    public int getSum(int index, int id) {
        return this.list.get(index).get(id);
    }

}
