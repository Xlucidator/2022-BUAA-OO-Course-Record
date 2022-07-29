import expression.BaseExpression;
import expression.Expression;
import expression.Factor;
import expression.Number;
import expression.Term;
import expression.Variable;

import java.math.BigInteger;

public class Parser {
    private final Holder holder;

    public Parser(Holder holder) {
        this.holder = holder;
    }

    public Expression parseExpression() {  // only return expression
        Expression expression = new Expression();
        int firstSign = 1;// the first "-" needs special tackling
        while (holder.getCurToken().equals("-") ||
               holder.getCurToken().equals("+")) {
            if (holder.getCurToken().equals("-")) {
                firstSign = -firstSign;
            }
            holder.next();
        }
        Term firstTerm = parseTerm();
        if (firstSign == -1) {
            firstTerm.negate();
        }
        expression.addTerms(firstTerm);

        while (holder.getCurToken().equals("+") ||
               holder.getCurToken().equals("-")) {
            int sign = 1;
            if (holder.getCurToken().equals("-")) {
                sign = -1;
            }
            holder.next();
            Term term = parseTerm();
            if (sign == -1) {
                term.negate();
            }
            expression.addTerms(term);
        }
        return expression;
    }

    public Term parseTerm() {  // only return Term
        Term term = new Term();
        term.addFactor(parseFactor());
        while (holder.getCurToken().equals("*")) {
            holder.next();
            if (holder.getCurToken().equals("-")) {
                term.negate();
                holder.next();
            }
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {  // only return Factor
        Factor factor = new Factor();
        factor.addBase(parseBase());
        factor.addPower(parsePower());
        return factor;
    }

    public BaseExpression parseBase() {  // return ( Number | Variable ) | Expression
        if (holder.getCurToken().equals("(")) {
            holder.next();
            Expression expression = parseExpression();
            holder.next();
            return expression;
        } else {
            if (holder.getCurToken().equals("x")) {
                holder.next();
                return new Variable();
            }
            String number = holder.getCurToken();
            holder.next();
            return new Number(new BigInteger(number));
        }
    }

    public int parsePower() {  // return only int
        if (holder.getCurToken().equals("**")) {
            holder.next();
            String power = holder.getCurToken();
            holder.next();
            return Integer.parseInt(power);
        }
        return 1;
    }
}
