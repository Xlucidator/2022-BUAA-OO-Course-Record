package expression;

import polynomial.MonomialX;
import polynomial.Polynomial;
import polynomial.WholeMonomial;

import java.math.BigInteger;

public class Power implements Factor {
    private final int pow;
    private int allPow;

    public Power(int pow) {
        this.pow = pow;
        this.allPow = 1;
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
        return new Power(this.pow);
    }

    @Override
    public Polynomial factorToPoly() {
        Polynomial result = new Polynomial();
        MonomialX power = new MonomialX(this.pow, BigInteger.ONE, true);
        WholeMonomial wholeMonomial = new WholeMonomial(power);
        result.addMonomial(wholeMonomial);
        return result;
    }
}
