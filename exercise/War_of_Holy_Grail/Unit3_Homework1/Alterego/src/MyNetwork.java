import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyNetwork implements Network {
    private final ArrayList<Person> people = new ArrayList<>();
    private final ArrayList<Group> groups = new ArrayList<>();
    private final ArrayList<Leaf> leaves = new ArrayList<>();
    
    public boolean contains(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return true;
            }
        }
        return false;
    }
    
    public Person getPerson(int id) {
        Person person = null;
        for (Person person1 : people) {
            if (person1.getId() == id) {
                person = person1;
                break;
            }
        }
        return person;
    }
    
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.add(person);
            Leaf leaf = new Leaf();
            leaf.setOwn(person.getId());
            leaf.setFather(person.getId());
            leaves.add(leaf);
        }
    }
    
    public Leaf find(int x) {
        for (Leaf leaf : leaves) {
            if (x == leaf.getOwn()) {
                if (leaf.getOwn() == leaf.getFather()) {
                    return leaf;
                } else {
                    return find(leaf.getFather());
                }
            }
        }
        return null;
    }
    
    public void merge(int i, int j) {
        Leaf leaf1 = find(i);
        Leaf leaf2 = find(j);
        if (leaf1.getRank() <= leaf2.getRank()) {
            leaf1.setFather(leaf2.getOwn());
        } else {
            leaf2.setFather(leaf1.getOwn());
        }
        if (leaf1.getRank() == leaf2.getRank() && leaf1.getOwn() != leaf2.getOwn()) {
            leaf2.addRank(1);
        }
    }
    
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            if (id1 == id2) {
                MyPerson myPerson = (MyPerson) getPerson(id1);
                myPerson.addRelation(getPerson(id2), value);
            } else {
                MyPerson myPerson1 = (MyPerson) getPerson(id1);
                MyPerson myPerson2 = (MyPerson) getPerson(id2);
                myPerson1.addRelation(myPerson2, value);
                myPerson2.addRelation(myPerson1, value);
                merge(id1, id2);
            }
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyEqualRelationException(id1, id2);
            }
        }
    }
    
    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyRelationNotFoundException(id1, id2);
            }
        }
    }
    
    public int queryPeopleSum() {
        return people.size();
    }
    
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (contains(id1) && contains(id2)) {
            Leaf leaf1 = find(id1);
            Leaf leaf2 = find(id2);
            return leaf1.getOwn() == leaf2.getOwn();
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else {
                throw new MyPersonIdNotFoundException(id2);
            }
        }
    }
    
    public int queryBlockSum() {
        int sum = 0;
        for (int i = 0; i < people.size(); i++) {
            int flag = 0;
            for (int j = 0; j < i; j++) {
                try {
                    if (isCircle(people.get(i).getId(), people.get(j).getId())) {
                        flag = 1;
                        break;
                    }
                } catch (PersonIdNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (flag == 0) {
                sum++;
            }
        }
        return sum;
    }
    
    public void addGroup(Group group) throws EqualGroupIdException {
        int flag = 0;
        for (Group group1 : groups) {
            if (group1.equals(group)) {
                flag = 1;
                break;
            }
        }
        if (flag == 1) {
            throw new MyEqualGroupIdException(group.getId());
        } else {
            groups.add(group);
        }
    }
    
    public Group getGroup(int id) {
        Group group = null;
        for (Group group1 : groups) {
            if (group1.getId() == id) {
                group = group1;
                break;
            }
        }
        return group;
    }
    
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        int flag = 0;
        if (contains(id1) && getGroup(id2) != null) {
            if (!getGroup(id2).hasPerson(getPerson(id1))) {
                if (getGroup(id2).getSize() < 1111) {
                    getGroup(id2).addPerson(getPerson(id1));
                }
            } else {
                flag = 1;
            }
        } else {
            if (getGroup(id2) == null) {
                throw new MyGroupIdNotFoundException(id2);
            } else if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            }
        }
        if (flag == 1) {
            throw new MyEqualPersonIdException(id1);
        }
    }
    
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        int flag = 0;
        if (contains(id1) && getGroup(id2) != null) {
            if (getGroup(id2).hasPerson(getPerson(id1))) {
                getGroup(id2).delPerson(getPerson(id1));
                
            } else {
                flag = 1;
            }
        } else {
            if (getGroup(id2) == null) {
                throw new MyGroupIdNotFoundException(id2);
            } else if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            }
        }
        if (flag == 1) {
            throw new MyEqualPersonIdException(id1);
        }
    }
}
