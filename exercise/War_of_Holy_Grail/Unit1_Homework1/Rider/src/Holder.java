public class Holder {
    private final String expression;
    private String curToken;
    private int ptr;

    public Holder(String expression) {
        this.expression = expression.replaceAll("[ \\t]", "")  // remove blank
                     .replaceAll("-\\+|\\+-", "-")  // between terms
                     .replaceAll("--|\\+\\+", "+")  // between terms
                     .replaceAll("\\*\\+", "*");  // between factors | inside a factor
        this.next();
    }

    public String getCurToken() {
        return curToken;
    }

    public String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (ptr < expression.length() && Character.isDigit(expression.charAt(ptr))) {
            sb.append(expression.charAt(ptr));
            ptr++;
        }
        return sb.toString();
    }

    public void next() {
        if (ptr == expression.length()) {
            return;
        }

        char c = expression.charAt(ptr);
        if (Character.isDigit(c)) {  // read a number
            curToken = getNumber();
        } else if ("()+-*x".indexOf(c) != -1) {  // read ()+-*x
            curToken = String.valueOf(c);
            ptr++;
            if (c == '*' && expression.charAt(ptr) == '*') {  // power
                curToken = "**";
                ptr++;
            }
        }
    }
}
