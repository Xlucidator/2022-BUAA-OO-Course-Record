package com.oocourse.spec1.main;

import java.util.HashMap;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Integer> acquaintance;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Person)) {
            return false;
        }
        return ((Person) obj).getId() == this.id;
    }

    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        return this.acquaintance.containsKey(person.getId());
    }

    public int queryValue(Person person) {
        if (this.acquaintance.containsKey(person.getId())) {
            return this.acquaintance.get(person.getId());
        }
        return 0;
    }

    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }

    protected void addAcq(Person p, int v) {
        this.acquaintance.put(p.getId(), v);
    }
}
