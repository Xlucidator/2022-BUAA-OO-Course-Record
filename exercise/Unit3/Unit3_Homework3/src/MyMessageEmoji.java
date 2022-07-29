import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

public class MyMessageEmoji extends MyMessage implements EmojiMessage {
    private final int emojiId;

    public MyMessageEmoji(int messageId, int emojiNumber,
                     Person messagePerson1, Person messagePerson2) {
        super(messageId, emojiNumber, messagePerson1, messagePerson2);
        this.emojiId = emojiNumber;
    }

    public MyMessageEmoji(int messageId, int emojiNumber,
                          Person messagePerson1, Group messageGroup) {
        super(messageId, emojiNumber, messagePerson1, messageGroup);
        this.emojiId = emojiNumber;
    }

    @Override
    public int getEmojiId() {
        return emojiId;
    }
}
