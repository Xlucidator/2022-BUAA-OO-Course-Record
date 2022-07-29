import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    private final String data;
    private final String content;
    private final String sender;
    private final String receiver;
    private final int year;
    private final int month;
    private final int day;

    Message(String message) {
        this.data = message;
        this.content = message.substring(message.indexOf("\"") + 1, message.lastIndexOf("\""));

        Pattern messagePattern = Pattern.compile(Task4.MESSAGE_PATTERN);
        Matcher matcherMessage = messagePattern.matcher(message);
        if (matcherMessage.find()) {
            this.year = Integer.parseInt(matcherMessage.group("year"));
            this.month = Integer.parseInt(matcherMessage.group("month"));
            this.day = Integer.parseInt(matcherMessage.group("day"));
            this.sender = matcherMessage.group("sender").substring(1);
            String receiverTmp = matcherMessage.group("receiver");
            if (receiverTmp == null) {
                this.receiver = null;
            } else {
                this.receiver = receiverTmp.substring(1);
            }
        } else {
            throw new RuntimeException("Invalid message!");
        }
    }

    public String getData() {
        return data;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean dateEqual(String[] tmp) {
        boolean yearEqual = tmp[0].isEmpty() || Integer.parseInt(tmp[0]) == year;
        boolean monthEqual = tmp[1].isEmpty() || Integer.parseInt(tmp[1]) == month;
        boolean dayEqual = tmp[2].isEmpty() || Integer.parseInt(tmp[2]) == day;
        return  yearEqual && monthEqual && dayEqual;
    }

}
