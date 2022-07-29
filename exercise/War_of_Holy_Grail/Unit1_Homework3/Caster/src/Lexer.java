import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input.replaceAll("[ \t]", "");
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);

        if (Character.isDigit(c)) {
            curToken = getNumber();
        }
        else {
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    public void nextN(int n) {
        for (int i = 0; i < n; i++) {
            this.next();
        }
    }

    public String peek() {
        return this.curToken;
    }

    public String peekN(int n) {
        if (pos + n - 1 > input.length()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(curToken);
        for (int i = pos; i < pos + n - 1; i++) {
            sb.append(input.charAt(i));
        }
        return sb.toString();
    }

    public Matcher findPattern(String patternStr) {
        int start = pos == 0 ? 0 : pos - 1;
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(input.substring(start));
        return matcher;
    }

}
