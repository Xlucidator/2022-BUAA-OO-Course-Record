import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private int money;
    private int socialValue;
    private final HashMap<Person, Integer> acquaintanceMap;
    private final LinkedList<Message> messages;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.money = 0;
        this.socialValue = 0;
        this.acquaintanceMap = new HashMap<>();
        this.messages = new LinkedList<>();
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
        return acquaintanceMap.containsKey(person);
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintanceMap.containsKey(person)) {
            return acquaintanceMap.get(person);
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public void addSocialValue(int num) {
        this.socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public List<Message> getReceivedMessages() {
        ArrayList<Message> receivedMessages = new ArrayList<>();
        for (int i = 0; i < messages.size() && i <= 3; ++i) {
            receivedMessages.add(messages.get(i));
        }
        return receivedMessages;    // puzzled
    }

    @Override
    public void addMoney(int num) {
        this.money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void getToKnow(Person person, int value) {
        acquaintanceMap.put(person, value);
    }

    public Set<Person> getAcquaintances() {
        return acquaintanceMap.keySet();
    }
}
