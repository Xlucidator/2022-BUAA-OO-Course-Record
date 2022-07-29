package deconstruct;

public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;
    
    public Lexer(String input) {
        this.input = input;
        this.nextCurToken();
    }

    public String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
    
        return sb.toString();
    }
    
    public void nextCurToken() {
        if (pos == input.length()) {    // if we touch the bottom, we return
            return;
        }
        char c = input.charAt(pos);     // get the char
        if (Character.isDigit(c)) {     // find a number
            curToken = getNumber();
        } else if ("()+-xyzi".indexOf(c) != -1) {  // if we find blankets or '+-' or variables 'xyz'
            pos += 1;
            curToken = String.valueOf(c);
        } else if (c == '*') {        // if we find '*', then we need to judge whether there is '**'
            pos += 1;
            curToken = "*";
            c = input.charAt(pos);
            if (c == '*') {             // we find '**'
                pos += 1;
                curToken = "**";
            }
        } else if ("cs".indexOf(c) != -1) {  // if we find 'c' or 's', we need to get 'cos' or 'sin'
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                sb.append(input.charAt(pos));
                pos++;
            }
            curToken = sb.toString();
        } else if ("fgh".indexOf(c) != -1) {
            pos += 1;
            curToken = String.valueOf(c);
        } else if (c == ',') {
            pos += 1;
            curToken = ",";
        }
    }
    
    public String peekCurToken() {
        return this.curToken;
    }
}
