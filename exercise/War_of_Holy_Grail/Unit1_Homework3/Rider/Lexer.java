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
            sb.append(input.charAt(pos));//
            ++pos;
        }
        
        return sb.toString();
    }
    
    public void next() {
        if (pos == input.length()) {
            curToken = "";
            return;
        }
        
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();//
        } else if (c == '*') {
            char c1Next = input.charAt(pos + 1);
            if (c1Next == '*') {
                char c2Next = input.charAt(pos + 2);
                if (c2Next == '+') {
                    pos += 3;
                } else {
                    pos += 2;
                }
                curToken = getNumber();
            } else {
                pos += 1;
                curToken = String.valueOf(c);
            }
        } else if ("+-fgh()xyz,sc".indexOf(c) != -1) {
            pos += 1;
            curToken = String.valueOf(c);
        }
    }
    
    public String peek() {
        return this.curToken;
    }
}
