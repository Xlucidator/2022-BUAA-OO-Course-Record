import java.math.BigInteger;
import java.util.ArrayList;

public class Term implements Comparable<Term> {
    private final ArrayList<Expression> bracketExpressions;
    private BigInteger coefficient;
    private int degree;

    public Term() {
        this.coefficient = BigInteger.ZERO;
        this.degree = 0;
        this.bracketExpressions = new ArrayList<>();
    }

    public Term(BigInteger coefficient, int degree) {
        this.coefficient = coefficient;
        this.degree = degree;
        this.bracketExpressions = new ArrayList<>();
    }

    public ArrayList<Expression> getBracketExpressions() {
        return bracketExpressions;
    }

    public void addExpression(Expression expression) {
        bracketExpressions.add(expression);
    }

    public ArrayList<Term> departBracket() {
        if (bracketExpressions.size() == 0) {
            return null;
        }
        mergeAllExpressions();
        ArrayList<Term> splitTerms = new ArrayList<>();
        Expression lastExp = bracketExpressions.get(0);
        for (int i = 0; i < lastExp.size(); i++) {
            splitTerms.add(multiplyTwoTerms(this, lastExp.get(i)));
        }
        return splitTerms;
    }

    public void mergeAllExpressions() {
        if (bracketExpressions.size() <= 1) {
            return;
        }
        for (int i = 1; i < bracketExpressions.size(); i++) {
            Expression exp1 = bracketExpressions.get(0);
            Expression exp2 = bracketExpressions.get(i);
            bracketExpressions.set(0, mergeTwoExpressions(exp1, exp2));
            bracketExpressions.remove(i--);
        }
    }

    public Expression mergeTwoExpressions(Expression exp1, Expression exp2) {
        Expression expression = new Expression();
        for (int i = 0; i < exp1.size(); i++) {
            Term t1 = exp1.get(i);
            for (int j = 0; j < exp2.size(); j++) {
                Term t2 = exp2.get(j);
                expression.addTerm(multiplyTwoTerms(t1, t2));
            }
        }
        expression.uniteLikeTerm();
        return expression;
    }

    public Term multiplyTwoTerms(Term t1, Term t2) {
        BigInteger c = t1.getCoefficient().multiply(t2.getCoefficient());
        int d = t1.getDegree() + t2.getDegree();
        return new Term(c, d);
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String inFormat() {
        StringBuilder stringFormat = new StringBuilder();
        if (degree == 0) {
            return coefficient.toString();
        } else {
            if (coefficient.equals(BigInteger.valueOf(-1))) {
                stringFormat.append("-");
            } else if (!coefficient.equals(BigInteger.ONE)) {
                stringFormat.append(coefficient).append("*");
            }
            stringFormat.append("x");
            if (degree == 2) {
                stringFormat.append("*x");
            } else if (degree != 1) {
                stringFormat.append("**").append(degree);
            }
        }
        return stringFormat.toString();
    }

    @Override
    public String toString() {
        String format;
        format = coefficient.toString() + "x^" + degree;
        for (Expression expression : bracketExpressions) {
            format = format.concat(" * (" + expression + ") ");
        }
        return format + " + ";
    }

    @Override
    public int compareTo(Term other) {
        if (this.getCoefficient().compareTo(other.getCoefficient()) < 0) {
            return -1;
        } else if (this.getCoefficient().equals(other.getCoefficient())) {
            return 0;
        } else {
            return 1;
        }
    }
}
