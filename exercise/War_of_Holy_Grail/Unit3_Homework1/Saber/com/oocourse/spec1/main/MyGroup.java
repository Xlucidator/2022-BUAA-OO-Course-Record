package com.oocourse.spec1.main;

import java.util.ArrayList;

public class MyGroup implements Group {
    private final int id;
    private final ArrayList<Person> people;
    private int sumAge;
    private int sumAge2;

    public MyGroup(int id) {
        this.id = id;
        this.sumAge = 0;
        this.sumAge2 = 0;
        this.people = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Group)) {
            return false;
        }
        return ((Group) obj).getId() == this.id;
    }

    public void addPerson(Person person) {
        this.people.add(person);
        this.sumAge += person.getAge();
        this.sumAge2 += person.getAge() * person.getAge();
    }

    public boolean hasPerson(Person person) {
        return this.people.contains(person);
    }

    public int getValueSum() {
        int sum = 0;
        for (Person p1 : this.people) {
            for (Person p2 : this.people) {
                if (p1.isLinked(p2)) {
                    sum += p1.queryValue(p2);
                }
            }
        }
        return sum;
    }

    public int getAgeMean() {
        if (this.people.size() == 0) {
            return 0;
        }
        return this.sumAge / this.people.size();
    }

    public int getAgeVar() {
        if (this.people.size() == 0) {
            return 0;
        }
        int mean = this.getAgeMean();
        return (this.sumAge2 - 2 * mean * this.sumAge) / this.people.size() + mean * mean;
    }

    public void delPerson(Person person) {
        this.people.remove(person);
        this.sumAge -= person.getAge();
        this.sumAge2 -= person.getAge() * person.getAge();
    }

    public int getSize() {
        return this.people.size();
    }

}
