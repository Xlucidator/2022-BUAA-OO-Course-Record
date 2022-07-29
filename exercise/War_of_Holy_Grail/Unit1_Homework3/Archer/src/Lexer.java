public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
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

    public void next2() {
        next();
        next();
    }

    public void next4() {
        next();
        next();
        next();
        next();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }

        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if (c == '*') {
            if (input.charAt(pos + 1) == '*') {
                pos += 2;
                curToken = "**";
            } else {
                pos += 1;
                curToken = String.valueOf(c);
            }
        } else if (c == 's') {
            if (input.charAt(pos + 1) == 'i' && input.charAt(pos + 2) == 'n') {
                pos += 3;
                curToken = "sin";
            }
            else if (input.charAt(pos + 1) == 'u' && input.charAt(pos + 2) == 'm') {
                pos += 3;
                curToken = "sum";
            }
        } else if (c == 'c') {
            if (input.charAt(pos + 1) == 'o' && input.charAt(pos + 2) == 's') {
                pos += 3;
                curToken = "cos";
            }
        }   else {
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    public void back() { --pos; }

    public String peek() {
        return this.curToken;
    }
}
