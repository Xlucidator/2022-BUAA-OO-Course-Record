
import expr.ExprPower;
import expr.Expr;
import expr.Term;
import expr.FactorPower;
import expr.Factor;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr(int s) {
        Expr expr = new Expr();
        int signp = 1;
        if (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (lexer.getFlag() == 4 || lexer.getFlag() == 10) {
                signp = -1;
            } else {
                signp = 1;
            }
            lexer.next();
        }
        expr.addTerm(parseTerm(signp * s));

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            int sign = 0;
            if (lexer.getFlag() == 4) {
                sign = -1;
            } else {
                sign = 1;
            }
            lexer.next();
            if (lexer.getFlag() == 10) {
                sign = sign * -1;
                lexer.next();
            } else if (lexer.getFlag() == 11) {
                sign = sign;
                lexer.next();
            }
            expr.addTerm(parseTerm(sign));
        }
        return expr;
    }

    public Term parseTerm(int sign) {
        Term term = new Term();
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        term.setSign(sign);
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            int s = 1;
            if (lexer.getFlag() == 20) {
                s = -1;
                lexer.next();
            } else if (lexer.getFlag() == 21) {
                s = 1;
                lexer.next();
            }
            lexer.next();
            Expr expr = parseExpr(s);
            if (lexer.getFlag() == 7) {
                //make expr**num into expr including num terms.
                int n = lexer.getNumberIn();
                /* int i = 1;
                Term term = new Term();
                for (i = 1; i <= n; i++) {
                    term.addFactor(expr);
                }
                Expr exprM = new Expr();
                exprM.addTerm(term); */
                ExprPower exprPower = new ExprPower(n, expr);
                lexer.next();
                return exprPower;
            }
            lexer.next();
            return expr;
        } else if (lexer.getFlag() == 1) {
            BigInteger num;
            String s;
            if (lexer.peek().charAt(0) == '+' || lexer.peek().charAt(0) == '-') {
                s = lexer.peek().substring(1);
            } else {
                s = lexer.peek();
            }
            if (s.indexOf('*') != -1) {
                int end = s.indexOf('*') - 1;
                BigInteger numF = new BigInteger(s.substring(0, end + 1));
                BigInteger numL = new BigInteger(s.substring(end + 3));
                num = numF.pow(numL.intValue());
            } else {
                num = new BigInteger(s);
            }
            if (lexer.peek().charAt(0) == '-') {
                num = num.negate();
            }
            lexer.next();
            FactorPower factorPower = new FactorPower(1, num, 0);
            return factorPower;
        } else if (lexer.getFlag() == 6 || lexer.getFlag() == 8) {
            FactorPower factorPower = new FactorPower(1, BigInteger.ONE, lexer.getNumberIn());
            lexer.next();
            return factorPower;
        } else {
            return null;
        }
    }
}
