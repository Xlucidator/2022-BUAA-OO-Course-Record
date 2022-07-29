import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;

import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyNetwork implements Network {
    private final ArrayList<Person> people;
    private final ArrayList<Group> groups;

    private final HashMap<Integer, Integer> ufSet;  // <pid, father_pid>

    public MyNetwork() {
        this.people = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.ufSet = new HashMap<>();
    }

    @Override
    public boolean contains(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean containsGroup(int id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private int find(int id) {
        int father = ufSet.get(id);
        if (id != father) { // also (ufSet.get(father) != father)
            ufSet.put(id, find(father));
        }
        return ufSet.get(id);
    }

    private void union(int id1, int id2) {
        int f1 = find(id1);
        int f2 = find(id2);
        ufSet.put(f1, f2);
    }

    @Override
    public Person getPerson(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        for (Person p : people) {
            if (p.equals(person)) {
                throw new MyExceptionEqualPersonId(p.getId());
            }
        }
        people.add(person);
        ufSet.put(person.getId(), person.getId());
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyExceptionPersonIdNotFound(id1);
        } else if (!contains(id2)) {
            throw new MyExceptionPersonIdNotFound(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyExceptionEqualRelation(id1, id2);
        }

        ((MyPerson)getPerson(id1)).getToKnow(getPerson(id2), value);
        ((MyPerson)getPerson(id2)).getToKnow(getPerson(id1), value);
        union(id1, id2);
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyExceptionPersonIdNotFound(id1);
        } else if (!contains(id2)) {
            throw new MyExceptionPersonIdNotFound(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyExceptionRelationNotFound(id1, id2);
        }

        return getPerson(id1).queryValue(getPerson(id2));
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyExceptionPersonIdNotFound(id1);
        } else if (!contains(id2)) {
            throw new MyExceptionPersonIdNotFound(id2);
        }
        //System.out.println("Here:" + (id1 != id2 && find(id1) == find(id2)));
        return find(id1) == find(id2);
    }

    @Override
    public int queryBlockSum() {
        HashSet<Integer> fatherCollect = new HashSet<>();
        for (Integer id : ufSet.keySet()) {
            fatherCollect.add(find(id));
        }
        return fatherCollect.size();
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        for (Group g : groups) {
            if (g.equals(group)) {
                throw new MyExceptionEqualGroupId(group.getId());
            }
        }
        groups.add(group);
    }

    @Override
    public Group getGroup(int id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!containsGroup(id2)) {
            throw new MyExceptionGroupIdNotFound(id2);
        } else if (!contains(id1)) {
            throw new MyExceptionPersonIdNotFound(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyExceptionEqualPersonId(id1);    // id of person
        }

        if (getGroup(id2).getSize() < 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        }
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!containsGroup(id2)) {
            throw new MyExceptionGroupIdNotFound(id2);
        } else if (!contains(id1)) {
            throw new MyExceptionPersonIdNotFound(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyExceptionEqualPersonId(id1);    // id of person
        }

        getGroup(id2).delPerson(getPerson(id1));
    }
}
