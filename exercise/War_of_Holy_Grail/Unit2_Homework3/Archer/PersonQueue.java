import java.util.ArrayList;

public class PersonQueue {
    private ArrayList<Person> waitPeople;
    private boolean isEnd;

    public PersonQueue() {
        waitPeople = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized void addRequest(Person person) {
        waitPeople.add(person);
        notifyAll();
    }

    public synchronized ArrayList<Person> getPeople() {
        return waitPeople;
    }

    public synchronized void setPeople(ArrayList<Person> requests) {
        this.waitPeople = requests;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        return waitPeople.isEmpty();
    }
}
