import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyGroup implements Group {
    private final int id;
    private final ArrayList<Person> people;

    public MyGroup(int id) {
        this.id = id;
        this.people = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return ((Group) obj).getId() == id;
        }
        return false;   // puzzled at JML's meaning
    }

    @Override
    public void addPerson(Person person) {
        people.add(person);
    }

    @Override
    public boolean hasPerson(Person person) {
        for (Person p : people) {
            if (person.equals(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        for (Person p1 : people) {
            for (Person p2 : people) {
                if (p1.isLinked(p2)) {
                    sum += p1.queryValue(p2);
                }
            }
        }
        return sum;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }

        int total = 0;
        for (Person person : people) {
            total += person.getAge();
        }
        return total / people.size();
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }

        int var = 0;
        int tmp;
        for (Person person : people) {
            tmp = person.getAge() - getAgeMean();
            var += (tmp * tmp);
        }
        var /= people.size();
        return var;
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person);
    }

    @Override
    public int getSize() {
        return people.size();
    }
}
