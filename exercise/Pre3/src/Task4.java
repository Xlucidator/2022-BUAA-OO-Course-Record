import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task4 {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static final String DATE_PATTERN = "(?<year>\\d{1,4})" + "/" +
            "(?<month>\\d{1,2})" + "/" + "(?<day>\\d{1,2})";
    public static final String SENDER_PATTERN = "(?<sender>-[a-zA-Z0-9]+)";
    public static final String RECEIVER_PATTERN = "((?<receiver>@[a-zA-Z0-9]+)?)";
    public static final String MESSAGE_PATTERN = DATE_PATTERN + ".*"
            + SENDER_PATTERN + "[^@]*" + RECEIVER_PATTERN;
    private static final ArrayList<Message> MESSAGE = new ArrayList<>();

    public static void main(String[] args) {
        while (SCANNER.hasNext()) {
            String line = SCANNER.nextLine();
            if (line.equals("END_OF_MESSAGE")) {
                break;
            }
            extractMessage(line);
        }

        while (SCANNER.hasNext()) {
            String operation = SCANNER.next();
            switch (operation) {
                case "qdate": {
                    String[] splitTmp = SCANNER.next().split("/");
                    String[] splitDate = new String[]{"", "", ""};
                    System.arraycopy(splitTmp,0,splitDate,0,splitTmp.length);
                    for (Message message : MESSAGE) {
                        if (message.dateEqual(splitDate)) {
                            System.out.println(message.getData());
                        }
                    }
                    break;
                }
                case "qsend": {
                    String selectSender = SCANNER.next();
                    for (Message message : MESSAGE) {
                        if (message.getSender().equals(selectSender)) {
                            System.out.println(message.getData());
                        }
                    }
                    break;
                }
                case "qrecv": {
                    String selectReceiver = SCANNER.next();
                    for (Message message : MESSAGE) {
                        if (message.getReceiver() != null &&
                                message.getReceiver().equals(selectReceiver)) {
                            System.out.println(message.getData());
                        }
                    }
                    break;
                }
                case "qmess": {
                    queryMessage(SCANNER.next(), SCANNER.nextInt());
                    break;
                }
                default: System.out.println("Invalid operation");
            }
        }
    }

    private static void queryMessage(String type, int sel) {
        Pattern pattern = Pattern.compile(".*");
        if (type.equals("A")) {
            switch (sel) {
                case 1:
                    pattern = Pattern.compile("^a{2,3}b{2,4}c{2,4}");
                    break;
                case 2:
                    pattern = Pattern.compile("a{2,3}b{2,4}c{2,4}$");
                    break;
                case 3:
                    pattern = Pattern.compile("a{2,3}b{2,4}c{2,4}");
                    break;
                case 4:
                    pattern = Pattern.compile("(a.*?){2,3}(b.*?){2,4}(c.*?){2,4}");
                    break;
                default: System.out.println("Invalid sel!");
            }
        } else {
            switch (sel) {
                case 1:
                    pattern = Pattern.compile("^a{2,3}b{2,1000000}c{2,4}");
                    break;
                case 2:
                    pattern = Pattern.compile("a{2,3}b{2,1000000}c{2,4}$");
                    break;
                case 3:
                    pattern = Pattern.compile("a{2,3}b{2,1000000}c{2,4}");
                    break;
                case 4:
                    pattern = Pattern.compile("(a.*?){2,3}(b.*?){2,1000000}(c.*?){2,4}");
                    break;
                default: System.out.println("Invalid sel!");
            }
        }
        for (Message message : MESSAGE) {
            Matcher matcher = pattern.matcher(message.getContent());
            if (matcher.find()) {
                System.out.println(message.getData());
            }
        }
    }

    public static void extractMessage(String line) {
        String[] msg = line.split(";");
        for (String message : msg) {
            if (!message.trim().isEmpty()) {
                MESSAGE.add(new Message(message.trim() + ";"));
            }
        }
    }

}
