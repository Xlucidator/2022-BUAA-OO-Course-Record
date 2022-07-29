import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;      // <pid, p>
    private final HashMap<Integer, Group> groups;       // <gid, g>
    private final HashMap<Integer, Message> messages;   // <mid, m>

    private final HashMap<Integer, Integer> ufSet;  // <pid, father_pid>

    public MyNetwork() {
        this.people = new HashMap<>();
        this.groups = new HashMap<>();
        this.messages = new HashMap<>();
        this.ufSet = new HashMap<>();
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
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyExceptionEqualPersonId(person.getId());
        }
        people.put(person.getId(), person);
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

        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        ((MyPerson) person1).getToKnow(person2, value);
        ((MyPerson) person2).getToKnow(person1, value);
        union(id1, id2);
        for (Group group : groups.values()) {
            if (group.hasPerson(person1) && group.hasPerson(person2)) {
                ((MyGroup) group).updateValueSum(value);
            }
        }
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
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyExceptionPersonIdNotFound(id);
        }

        int num = 0;    // vertex num of this block
        for (Integer pid : ufSet.keySet()) {
            if (find(id) == find(pid)) {
                num += 1;
            }
        }

        HashSet<Person> verSet = new HashSet<>();
        HashMap<Person, Integer> edgSet = new HashMap<>(); // <v2, e_value> base to cur verSet
        int total = 0;
        Person person = getPerson(id);
        edgSet.put(person, 0);

        do {
            person = getVertexOfMinEdge(edgSet);
            verSet.add(person);
            total += edgSet.get(person);
            edgSet.remove(person);
            for (Person p : ((MyPerson) person).getAcquaintances()) {
                if (verSet.contains(p) ||
                        (edgSet.containsKey(p) && edgSet.get(p) < person.queryValue(p))) {
                    continue;
                }
                edgSet.put(p, person.queryValue(p));
            }
        } while (verSet.size() < num);
        return total;
    }

    private Person getVertexOfMinEdge(HashMap<Person, Integer> edges) {
        Person v = null;
        int edgeValue = 1024;
        for (Person vertex : edges.keySet()) {
            if (edges.get(vertex) < edgeValue) {
                v = vertex;
                edgeValue = edges.get(vertex);
            }
        }
        return v;
    }

    public boolean containsGroup(int id) {
        return groups.containsKey(id);
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (containsGroup(group.getId())) {
            throw new MyExceptionEqualGroupId(group.getId());
        }
        groups.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
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
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!containsGroup(id)) {
            throw new MyExceptionGroupIdNotFound(id);
        }
        return getGroup(id).getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!containsGroup(id)) {
            throw new MyExceptionGroupIdNotFound(id);
        }
        return getGroup(id).getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!containsGroup(id)) {
            throw new MyExceptionGroupIdNotFound(id);
        }
        return getGroup(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!containsGroup(id2)) {
            throw new MyExceptionGroupIdNotFound(id2);
        } else if (!contains(id1)) {
            throw new MyExceptionPersonIdNotFound(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) { // group-id2 doesn't have person-id1
            throw new MyExceptionEqualPersonId(id1);    // id of person
        }

        getGroup(id2).delPerson(getPerson(id1));
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyExceptionEqualMessageId(message.getId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyExceptionEqualPersonId(message.getPerson1().getId());
        }
        messages.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyExceptionMessageIdNotFound(id);
        }

        Message message = getMessage(id);
        Person ps = message.getPerson1();
        Person pr = message.getPerson2(); // can be null
        Group gr = message.getGroup();    // can be null
        if (message.getType() == 0) {
            if (!ps.isLinked(pr)) {
                throw new MyExceptionRelationNotFound(ps.getId(), pr.getId());
            }
            /* haven't evaded 'message.getPerson1() != message.getPerson2() */
            ps.addSocialValue(message.getSocialValue());
            pr.addSocialValue(message.getSocialValue());
            pr.getMessages().add(0,message);
        } else if (message.getType() == 1) {
            if (!gr.hasPerson(ps)) {
                throw new MyExceptionPersonIdNotFound(ps.getId());
            }
            for (Person p : ((MyGroup) gr).getPeople()) {
                p.addSocialValue(message.getSocialValue());
            }
        }
        messages.remove(message.getId());
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyExceptionPersonIdNotFound(id);
        }
        return getPerson(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyExceptionPersonIdNotFound(id);
        }
        return getPerson(id).getReceivedMessages();
    }
}
