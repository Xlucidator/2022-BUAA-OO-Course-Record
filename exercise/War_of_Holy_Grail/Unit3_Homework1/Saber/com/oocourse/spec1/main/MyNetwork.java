package com.oocourse.spec1.main;

import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.exceptions.MyPersonIdNotFoundException;
import com.oocourse.spec1.exceptions.MyEqualPersonIdException;
import com.oocourse.spec1.exceptions.MyEqualRelationException;
import com.oocourse.spec1.exceptions.MyGroupIdNotFoundException;
import com.oocourse.spec1.exceptions.MyRelationNotFoundException;
import com.oocourse.spec1.exceptions.MyEqualGroupIdException;

import java.util.ArrayList;
import java.util.HashMap;

public class MyNetwork implements Network {
    private final ArrayList<Person> people;
    private final ArrayList<Group> groups;
    private final HashMap<Integer, Integer> relations;
    private int peopleSize;
    private int qbs;

    public MyNetwork() {
        this.people = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.relations = new HashMap<>();
        this.peopleSize = 0;
        this.qbs = 0;
    }

    public boolean contains(int id) {
        for (Person p : this.people) {
            if (p.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private boolean containsG(int id) {
        for (Group g : this.groups) {
            if (g.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public Person getPerson(int id) {
        for (Person p : this.people) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (this.people.contains(person)) {
            throw new MyEqualPersonIdException(person.getId());
        }
        this.people.add(person);
        this.relations.put(person.getId(), person.getId());
        this.peopleSize++;
        this.qbs++;
    }

    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (!this.contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!this.contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        MyPerson p1 = (MyPerson) getPerson(id1);
        MyPerson p2 = (MyPerson) getPerson(id2);
        p1.addAcq(p2, value);
        p2.addAcq(p1, value);
        if (!isCircle(id1, id2)) {
            this.qbs--;
        }
        this.relations.put(find(id2), find(id1));
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!this.contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!this.contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }

    public int queryPeopleSum() {
        return this.peopleSize;
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!this.contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!this.contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (id1 == id2) {
            return true;
        }
        return find(id1) == find(id2);
    }

    public int queryBlockSum() {
        return this.qbs;
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        if (this.groups.contains(group)) {
            throw new MyEqualGroupIdException(group.getId());
        }
        this.groups.add(group);
    }

    public Group getGroup(int id) {
        for (Group g : this.groups) {
            if (g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!this.containsG(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!this.contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        if (getGroup(id2).getSize() >= 1111) {
            return;
        }
        this.getGroup(id2).addPerson(this.getPerson(id1));
    }

    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!this.containsG(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!this.contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        this.getGroup(id2).delPerson(this.getPerson(id1));
    }

    private int find(int id) {
        if (this.relations.get(id) != id) {
            this.relations.put(id, find(this.relations.get(id)));
        }
        return this.relations.get(id);
    }

}
