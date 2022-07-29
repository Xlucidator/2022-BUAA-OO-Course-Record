import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

public class MyMessageNotice extends MyMessage implements NoticeMessage {
    private final String string;

    public MyMessageNotice(int messageId, String noticeString,
                           Person messagePerson1, Person messagePerson2) {
        super(messageId, noticeString.length(), messagePerson1, messagePerson2);
        this.string = noticeString;
    }

    public MyMessageNotice(int messageId, String noticeString,
                           Person messagePerson1, Group messageGroup) {
        super(messageId, noticeString.length(), messagePerson1, messageGroup);
        this.string = noticeString;
    }

    @Override
    public String getString() {
        return string;
    }
}
