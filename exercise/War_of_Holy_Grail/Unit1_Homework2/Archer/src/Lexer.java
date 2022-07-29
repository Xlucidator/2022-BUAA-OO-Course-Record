public class Lexer {
    private int pos = 0;
    private String formula;
    private String curToken;

    public Lexer(String input) {
        this.formula = input;
        this.next();
    }

    public void next() {
        if (pos == formula.length()) {
            return;
        }
        char c = formula.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if ("x".indexOf(c) != -1) {
            curToken = "x";
            pos += 1;
        } else if ("u".indexOf(c) != -1) {
            curToken = getTrian();
        } else if ("v".indexOf(c) != -1) {
            curToken = getTrian();
        } else if ("()+*-".indexOf(c) != -1) {
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    public String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < formula.length() && Character.isDigit(formula.charAt(pos))) {
            sb.append(formula.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    public String getTrian() {
        StringBuilder sb = new StringBuilder();
        if (formula.charAt(pos) == 'u') {
            sb.append('u');
            sb.append('(');
        } else {
            sb.append('v');
            sb.append('(');
        }
        int bracket = 1;
        pos = pos + 2;
        while (pos < formula.length() && bracket != 0) {
            sb.append(formula.charAt(pos));
            if (formula.charAt(pos) == '(') {
                bracket++;
            } else if (formula.charAt(pos) == ')') {
                bracket = bracket - 1;
            }
            pos++;
        }
        return sb.toString();
    }

    public String peek() {
        return this.curToken;
    }

    public char peekNext(int i) {
        int t = i - 1;
        if (pos + t < formula.length()) {
            return formula.charAt(pos + t);
        } else {
            return ' ';
        }

    }

}
