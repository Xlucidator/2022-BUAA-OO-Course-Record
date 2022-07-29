package polynomial;

import java.math.BigInteger;

public class Power {
    private final BigInteger index;
    private final BigInteger coefficient;

    public Power(BigInteger index, BigInteger coefficient) {
        this.index = index;
        this.coefficient = coefficient;
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public BigInteger getIndex() {
        return index;
    }

    @Override
    public String toString() {
        if (coefficient.equals(BigInteger.ZERO)) {
            return "";
        }
        if (index.equals(BigInteger.ZERO)) {
            return (coefficient.signum() == 1 ? "+" : "") + coefficient;
        }
        //coefficient
        String s = "";
        if (coefficient.equals(BigInteger.ONE)) {
            s += "+";
        } else if (coefficient.equals(new BigInteger("-1"))) {
            s += "-";
        } else {
            s += (coefficient.signum() == 1 ? "+" : "") + coefficient + "*";
        }
        //index
        s += "x";
        if (index.equals(BigInteger.ONE)) {
            s += "";
        } else if (index.equals(BigInteger.valueOf(2))) {
            s += "*x";
        } else {
            s += "**" + index;
        }
        return s;
    }
}
