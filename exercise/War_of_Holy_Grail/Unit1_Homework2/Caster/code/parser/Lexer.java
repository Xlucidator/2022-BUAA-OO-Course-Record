package parser;

import expr.Constant;

//词法分析器
public class Lexer {
    private String string;
    private int pos = 0;
    private String curToken;
    private TokenType tokenType;
    
    public Lexer(String string) {
        this.string = string;
        this.next();
    }
    
    public enum TokenType {
        ERR, EOL,
        CONSTANT, VAR, SIN, COS, FUNCTION, SUM, OTHER
    }
    
    public String getToken() {
        return this.curToken;
    }
    
    public TokenType getTokenType() {
        return this.tokenType;
    }
    
    //消除空白项
    private void getBlank() {
        while (pos < string.length() && (string.charAt(pos) == ' ' || string.charAt(pos) == '\t')) {
            ++pos;
        }
    }
    
    //得到当前单个字符，并将pos指向下一个
    private void getChar(char c) {
        ++pos;
        curToken = String.valueOf(c);
    }
    
    //得到常数
    private Constant getConstant() {
        StringBuilder sb = new StringBuilder();
        sb.append(string.charAt(pos));
        ++pos;
        while (pos < string.length() && Character.isDigit(string.charAt(pos))) {
            sb.append(string.charAt(pos));
            ++pos;
        }
        return new Constant(sb.toString());
    }
    
    //转向下一个词法
    public void next() {
        if (pos == string.length()) {
            tokenType = TokenType.EOL;
            return;
        }
        char c = string.charAt(pos);
        if (c == ' ' || c == '\t') {
            getBlank();
        }
        if (pos < string.length()) {
            c = string.charAt(pos);
        } else {
            tokenType = TokenType.EOL;
            return;
        }
        if ("()+-,=".indexOf(c) != -1) {
            getChar(c);
            tokenType = TokenType.OTHER;
        } else if (c == '*') {
            if (string.charAt(pos + 1) == '*') {
                pos += 2;
                curToken = "**";
            } else {
                getChar(c);
            }
            tokenType = TokenType.OTHER;
        } else if (Character.isDigit(c)) {
            Constant constant = getConstant();
            curToken = constant.toString();
            tokenType = TokenType.CONSTANT;
        } else if ("xyzi".indexOf(c) != -1) {
            getChar(c);
            tokenType = TokenType.VAR;
        } else if (c == 's' &&
                string.charAt(pos + 1) == 'i' &&
                string.charAt(pos + 2) == 'n') {
            pos += 3;
            curToken = "sin";
            tokenType = TokenType.SIN;
        } else if (c == 'c' &&
                string.charAt(pos + 1) == 'o' &&
                string.charAt(pos + 2) == 's') {
            pos += 3;
            curToken = "cos";
            tokenType = TokenType.COS;
        } else if ("fgh".indexOf(c) != -1) {
            getChar(c);
            tokenType = TokenType.FUNCTION;
        } else if (c == 's' &&
                string.charAt(pos + 1) == 'u' &&
                string.charAt(pos + 2) == 'm') {
            pos += 3;
            curToken = "sum";
            tokenType = TokenType.SUM;
        } else {
            /*
            exception
             */
        }
    }
}