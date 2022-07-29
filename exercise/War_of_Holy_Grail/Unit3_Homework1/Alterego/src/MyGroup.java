import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyGroup implements Group {
    private final int id;
    private final ArrayList<Person> people = new ArrayList<>();
    
    public MyGroup(int id) {
        this.id = id;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Group)) {
            return false;
        } else {
            return ((Group) obj).getId() == id;
        }
    }
    
    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            people.add(person);
        }
    }
    
    public boolean hasPerson(Person person) {
        for (Person person1 : people) {
            if (person1.equals(person)) {
                return true;
            }
        }
        return false;
    }
    
    public int getValueSum() {
        int sum = 0;
        for (Person person : people) {
            for (Person person1 : people) {
                if (person.isLinked(person1)) {
                    sum = sum + person.queryValue(person1);
                }
            }
        }
        return sum;
    }
    
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        } else {
            int sum = 0;
            for (Person person : people) {
                sum = sum + person.getAge();
            }
            return sum / people.size();
        }
    }
    
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        } else {
            int sum = 0;
            for (Person person : people) {
                sum = sum + (person.getAge() - getAgeMean()) * (person.getAge() - getAgeMean());
            }
            return sum / people.size();
        }
    }
    
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove(person);
        }
    }
    
    public int getSize() {
        return people.size();
    }
}
