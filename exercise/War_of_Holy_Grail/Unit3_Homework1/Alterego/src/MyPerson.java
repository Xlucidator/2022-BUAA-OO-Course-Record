import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final ArrayList<Person> acquaintance = new ArrayList<>();
    private final ArrayList<Integer> value = new ArrayList<>();
    
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof MyPerson)) {
            return false;
        } else {
            return ((Person) obj).getId() == id;
        }
    }
    
    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        }
        for (Person person1 : acquaintance) {
            if (person1.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public int queryValue(Person person) {
        for (int i = 0; i < acquaintance.size(); i++) {
            Person person1 = acquaintance.get(i);
            if (person1.getId() == person.getId()) {
                return value.get(i);
            }
        }
        return 0;
    }
    
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
    
    public void addRelation(Person person, int value) {
        acquaintance.add(person);
        this.value.add(value);
    }
}
