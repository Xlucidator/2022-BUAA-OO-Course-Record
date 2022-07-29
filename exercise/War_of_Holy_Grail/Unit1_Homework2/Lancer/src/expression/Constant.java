package expression;

import polynomial.MonomialX;
import polynomial.Polynomial;
import polynomial.WholeMonomial;

import java.math.BigInteger;

public class Constant implements Factor {
    private BigInteger num;
    private boolean symbol;
    private int allPow;

    public Constant(BigInteger num, String symbol) {
        this.num = num;
        this.symbol = !symbol.equals("-");
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
        String symbol;
        if (this.symbol) {
            symbol = "+";
        } else {
            symbol = "-";
        }
        return new Constant(this.num, symbol);
    }

    @Override
    public Polynomial factorToPoly() {
        Polynomial result = new Polynomial();
        MonomialX constant = new MonomialX(0, this.num, this.symbol);
        WholeMonomial wholeMonomial = new WholeMonomial(constant);
        result.addMonomial(wholeMonomial);
        return result;
    }

    public int compare(Constant other) {
        if (this.symbol == other.symbol) {
            if (this.symbol) {
                return this.num.compareTo(other.num);
            } else {
                if (this.num.compareTo(other.num) == 1) {
                    return -1;
                } else if (this.num.compareTo(other.num) == -1) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } else if (this.symbol) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return ((this.symbol) ? "" : "-") + this.num.toString();
    }

    public void add1() {
        if (this.symbol) {
            this.num = this.num.add(BigInteger.ONE);
        } else {
            if (this.num.equals(BigInteger.ONE)) {
                this.symbol = true;
                this.num = BigInteger.ZERO;
            } else {
                this.num = this.num.subtract(BigInteger.ONE);
            }
        }
    }
}
