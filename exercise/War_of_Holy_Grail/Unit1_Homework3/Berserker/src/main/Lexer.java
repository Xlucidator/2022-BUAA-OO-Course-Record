package main;

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

    public void next() {
        if (pos == input.length()) {
            return;
        }

        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if (pos + 1 < input.length() && input.charAt(pos) == '*' && input.charAt(
                pos + 1) == '*') {
            curToken = "**";
            pos += 2;
        } else if ("+-*()xyzjklfgh,i".indexOf(c) != -1) {
            curToken = String.valueOf(c);
            pos += 1;
        } else {
            if (c == 's') {
                if (input.charAt(pos + 1) == 'u') {
                    curToken = "sum";
                } else {
                    curToken = "sin";
                }
            } else {
                curToken = "cos";
            }
            pos += 3;
        }
    }

    public void nextInt(int count) {
        for (int i = 0; i < count; i++) {
            this.next();
        }
    }

    public String peek() {
        return this.curToken;
    }
}