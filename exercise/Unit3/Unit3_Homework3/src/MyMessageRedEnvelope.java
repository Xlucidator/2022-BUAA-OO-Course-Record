import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

public class MyMessageRedEnvelope extends MyMessage implements RedEnvelopeMessage {
    private final int money;

    public MyMessageRedEnvelope(int messageId, int luckyMoney,
                                Person messagePerson1, Person messagePerson2) {
        super(messageId, luckyMoney * 5, messagePerson1, messagePerson2);
        this.money = luckyMoney;
    }

    public MyMessageRedEnvelope(int messageId, int luckyMoney,
                                Person messagePerson1, Group messageGroup) {
        super(messageId, luckyMoney * 5, messagePerson1, messageGroup);
        this.money = luckyMoney;
    }

    @Override
    public int getMoney() {
        return money;
    }
}
