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
            if (pos + 2 < input.length()) {
                String s = input.substring(pos, pos + 3);
                if (s.equals("sin") || s.equals("cos") || s.equals("sum")) {
                    curToken = s;
                    pos += 3;
                    return;
                } else if (s.startsWith("**")) {
                    curToken = s.substring(0, 2);
                    pos += 2;
                    return;
                }
            }
            String s = input.substring(pos, pos + 1);
            if (s.substring(0, 1).matches("\\d")) {
                curToken = getNumber();
            } else {
                curToken = s.substring(0, 1);
                pos += 1;
            }
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
        return num.toString();
    }

}
