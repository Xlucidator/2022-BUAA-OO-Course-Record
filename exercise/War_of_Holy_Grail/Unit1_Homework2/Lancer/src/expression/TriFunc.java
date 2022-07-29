package expression;

import polynomial.Polynomial;
import polynomial.WholeMonomial;
import polynomial.MonomialCos;
import polynomial.MonomialSin;
import java.math.BigInteger;

public class TriFunc implements Factor {
    private String name;
    private final int pow;
    private int allPow;
    private Factor factor;

    public TriFunc(String name, int pow, Factor factor) {
        this.name = name;
        this.pow = pow;
        this.allPow = 1;
        this.factor = factor;
    }

    @Override
    public Polynomial factorToPoly() {
        Polynomial result = new Polynomial();
        Polynomial polynomial = this.factor.factorToPoly();
        if (this.name.equals("sin")) {
            MonomialSin monomialSin = new MonomialSin(this.pow, BigInteger.ONE, true, polynomial);
            WholeMonomial wholeMonomial = new WholeMonomial(monomialSin);
            result.addMonomial(wholeMonomial);
        } else {
            MonomialCos monomialCos = new MonomialCos(this.pow, BigInteger.ONE, true, polynomial);
            WholeMonomial wholeMonomial = new WholeMonomial(monomialCos);
            result.addMonomial(wholeMonomial);
        }
        return result;
    }

    @Override
    public void setAllPow(int allPow) {
        this.allPow = 1;
    }

    @Override
    public int getAllPow() {
        return this.allPow;
    }

    @Override
    public Factor copy() {
        return new TriFunc(this.name, this.pow, this.factor);
    }
}
