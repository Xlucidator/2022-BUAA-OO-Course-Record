import expr.Expr;
import expr.Factor;
import expr.Number;
import expr.Term;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm());

        while (lexer.peek().equals("+")) {    // todo4
            lexer.next();
            expr.addTerm(parseTerm());
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());   // todo5
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();   // todo6 : move through ")"
            return expr;
        } else {
            BigInteger num = new BigInteger(lexer.peek()); // todo7
            lexer.next();
            return new Number(num);
        }
    }
}
