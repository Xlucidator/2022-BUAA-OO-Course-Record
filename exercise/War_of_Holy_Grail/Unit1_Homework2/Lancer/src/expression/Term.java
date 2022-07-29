package expression;

import polynomial.MonomialX;
import polynomial.Polynomial;
import polynomial.WholeMonomial;

import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private final ArrayList<Factor> factors;

    public Term() {
        this.factors = new ArrayList<>();
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public Term copy() {
        Term term = new Term();
        for (Factor factor : this.factors) {
            Factor f = factor.copy();
            term.addFactor(f);
        }
        return term;
    }

    public Polynomial toPoly() {
        Polynomial result = new Polynomial();
        MonomialX monomialX = new MonomialX(0,BigInteger.ONE,true);
        WholeMonomial wholeMonomial = new WholeMonomial(monomialX);
        result.addMonomial(wholeMonomial);
        ArrayList<Polynomial> t = new ArrayList<>();
        for (Factor factor : factors) {
            t.add(factor.factorToPoly());
        }
        for (Polynomial item : t) {
            result = result.multiply(result, item);
        }
        return result;
    }
}
