import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Person, Integer> acquaintanceMap;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintanceMap = new HashMap<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return ((Person) obj).getId() == id;
        }
        return false;   // puzzled at JML's meaning
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        }
        for (Person p : acquaintanceMap.keySet()) {
            if (p.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int queryValue(Person person) {
        for (Person p : acquaintanceMap.keySet()) {
            if (p.getId() == person.getId()) {
                return acquaintanceMap.get(p);
            }
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void getToKnow(Person person, int value) {
        acquaintanceMap.put(person, value);
    }
}
