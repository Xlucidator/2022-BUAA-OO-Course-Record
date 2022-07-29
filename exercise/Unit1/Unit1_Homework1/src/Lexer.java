public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    public String peek() {
        return this.curToken;
    }

    public void next() {
        if (pos < input.length()) {
            String s = input.substring(pos, pos + 1);
            curToken = s.matches("\\d") ? getNumber() : s;
            pos++;
        } else {
            curToken = "EOL";
        }
    }

    public String getNumber() {
        StringBuilder num = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            num.append(input.charAt(pos));
            pos++;
        }
        pos--;
        return num.toString();
    }
}
