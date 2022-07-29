import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashSet;

public class MyGroup implements Group {
    private final int id;
    private final HashSet<Person> people;
    private int ageSum;
    private int valueSum;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashSet<>();
        this.ageSum = 0;
        this.valueSum = 0;
    }

    public HashSet<Person> getPeople() {
        return people;
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
        for (Person p : people) {
            if (person.isLinked(p)) {
                valueSum += 2 * person.queryValue(p);
            }
        }
        people.add(person);
        ageSum += person.getAge();
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.contains(person);
    }

    @Override
    public int getValueSum() {
        return valueSum;
    }

    public void updateValueSum(int value) {
        valueSum += 2 * value;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        return ageSum / people.size();
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
        ageSum -= person.getAge();
        people.remove(person);
        for (Person p : people) {
            if (person.isLinked(p)) {
                valueSum -= 2 * person.queryValue(p);
            }
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }
}
