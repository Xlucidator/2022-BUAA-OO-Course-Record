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
            sb.append(input.charAt(pos));  // todo1
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
            curToken = getNumber();  // todo2
        } else if (String.valueOf(c).matches("[+*()]")) { // todo3 : tend to be puzzled
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    public String peek() {
        return this.curToken;
    }
}
