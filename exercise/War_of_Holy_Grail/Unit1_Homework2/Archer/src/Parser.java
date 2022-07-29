import java.math.BigInteger;

public class Parser {
    private Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm());
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            expr.addTerm(parseTerm());
        }
        if (lexer.peek().equals(")")) {
            if (lexer.peekNext(1) == '*' && lexer.peekNext(2) == '*') {
                lexer.next();
                lexer.next();
                lexer.next();
                expr.setPower(Integer.parseInt(lexer.peek()));
            }
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        if (lexer.peek().equals("-")) {
            term.setNe();
            lexer.next();
        } else if (lexer.peek().equals("+")) {
            lexer.next();
        }
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("x")) {
            if (lexer.peekNext(1) == '*') {
                lexer.next();
                if (lexer.peekNext(1) == '*') {
                    lexer.next();
                    lexer.next();
                    String temp = lexer.peek();
                    lexer.next();
                    return new Variable().setVariable(new BigInteger("1"),
                            new BigInteger(temp), new BigInteger("0"), "");
                } else {
                    return new Variable().setVariable(new BigInteger("1"), new BigInteger("1"),
                            new BigInteger("0"), "");
                }
            } else {
                lexer.next();
                return new Variable().setVariable(new BigInteger("1"), new BigInteger("1"),
                        new BigInteger("0"), "");
            }
        } else if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();
            return expr;
        } else if (lexer.peek().charAt(0) == 'u' || lexer.peek().charAt(0) == 'v') {
            String t = lexer.peek();
            if (lexer.peekNext(1) == '*') {
                lexer.next();
                if (lexer.peekNext(1) == '*') {
                    lexer.next();
                    lexer.next();
                    String temp = lexer.peek();
                    lexer.next();
                    return new Variable().setVariable(new BigInteger("1"),
                            new BigInteger("0"), new BigInteger(temp), t);
                } else {
                    return new Variable().setVariable(new BigInteger("1"), new BigInteger("0"),
                            new BigInteger("1"), t);
                }
            } else {
                lexer.next();
                return new Variable().setVariable(new BigInteger("1"), new BigInteger("0"),
                        new BigInteger("1"), t);
            }
        } else {
            String temp;
            if (lexer.peek().equals("-")) {
                lexer.next();
                temp = "-" + lexer.peek();
                lexer.next();
            } else {
                temp = lexer.peek();
                lexer.next();
            }
            return new Variable().setVariable(new BigInteger(temp), new BigInteger("0"),
                    new BigInteger("0"), "");
        }
    }

}
