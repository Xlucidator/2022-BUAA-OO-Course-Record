package expression;

import polynomial.Polynomial;

import java.util.ArrayList;

public class Expression implements Factor {
    private final ArrayList<Term> terms;
    private int allPow;

    public Expression() {
        this.terms = new ArrayList<>();
        this.allPow = 1;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    @Override
    public void setAllPow(int allPow) {
        this.allPow = allPow;
    }

    @Override
    public int getAllPow() {
        return this.allPow;
    }

    public Factor copy() {
        Expression expression = new Expression();
        for (Term term : this.terms) {
            Term t = term.copy();
            expression.addTerm(t);
        }
        return expression;
    }

    @Override
    public Polynomial factorToPoly() {
        Polynomial result = new Polynomial();
        ArrayList<Polynomial> t = new ArrayList<>();
        for (Term term : terms) {
            t.add(term.toPoly());
        }
        for (Polynomial item : t) {
            result = result.addPolynomial(result, item);
        }
        return result;
    }
}
