import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class PersonList {
    private ArrayList<PersonRequest> personList;
    private int size;

    public PersonList(int size) {
        personList = new ArrayList<>();
        this.size = size;
    }

    public int size() {
        return personList.size();
    }

    public int getMaxSize() {
        return size;
    }

    public void addPerson(PersonRequest person) {
        personList.add(person);
    }

    public void removePerson(PersonRequest person) {
        personList.remove(person);
    }

    public ArrayList<PersonRequest> getPersonList() {
        return personList;
    }

    public boolean isEmpty() {
        return personList.isEmpty();
    }

    public boolean isFull() {
        return personList.size() == size;
    }

    public boolean contains(PersonRequest person) {
        return personList.contains(person);
    }

    public boolean hasPersonOut(int floor) {
        for (PersonRequest person : personList) {
            if (person.getToFloor() == floor) {
                return true;
            }
        }
        return false;
    }
}
