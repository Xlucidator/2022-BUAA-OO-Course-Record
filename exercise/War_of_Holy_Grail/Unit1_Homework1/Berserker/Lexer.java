public class Lexer {
    private final String text;
    private int pos = 0;
    private String curToken;

    public Lexer(String text) {
        String s = text.replaceAll("\\s+", "");
        s = s.replaceAll("\\*\\*", "^");
        s = s.replaceAll("--", "+");
        s = s.replaceAll("(\\+-)|(-\\+)", "-");
        s = s.replaceAll("\\+\\+", "+");
        this.text = s;
        this.next();
    }

    public String getToken() {
        return this.curToken;
    }

    public void next() {
        if (pos == text.length()) { return; }
        if (Character.isDigit(text.charAt(pos))) {
            StringBuilder sb = new StringBuilder();
            while (pos < text.length() && Character.isDigit(text.charAt(pos))) {
                sb.append(text.charAt(pos));
                ++pos;
            }
            curToken = sb.toString();
        } else {
            curToken = String.valueOf(text.charAt(pos));
            ++pos;
        }
    }
}
