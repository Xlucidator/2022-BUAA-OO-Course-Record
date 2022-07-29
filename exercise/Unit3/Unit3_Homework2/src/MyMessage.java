import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private final /* nullable */ Person person2;
    private final /* nullable */ Group group;

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.type = 0;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.group = null;
    }

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Group messageGroup) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.type = 1;
        this.person1 = messagePerson1;
        this.person2 = null;
        this.group = messageGroup;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {    // requires person2 != null
        return person2;
    }

    @Override
    public Group getGroup() {       // requires group != null
        return group;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            return ((Message) obj).getId() == id;
        }
        return false;
    }

}
