import java.math.BigInteger;

public class PolyFactor implements Comparable<PolyFactor> {
    private BigInteger coefficient;
    private int index;

    public PolyFactor(BigInteger coefficient, int index) {
        this.coefficient = coefficient;
        this.index = index;
    }

    public PolyFactor deepClone() {
        BigInteger newCoefficient = new BigInteger(coefficient.toString());
        return new PolyFactor(newCoefficient, index);
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public int getIndex() {
        return index;
    }

    public void multiplyPoly(PolyFactor other) {
        this.coefficient = getCoefficient().multiply(other.getCoefficient());
        this.index = getIndex() + other.getIndex();
    }

    public boolean valueEquals(PolyFactor other) {
        return this.coefficient.equals(other.getCoefficient()) && this.index == other.getIndex();
    }

    public String inFormat(boolean unfoldIndexTwo) {
        StringBuilder stringFormat = new StringBuilder();
        if (index == 0) {
            return coefficient.toString();
        } else {
            if (coefficient.equals(BigInteger.ZERO)) {
                return "0";
            } else if (coefficient.equals(BigInteger.valueOf(-1))) {
                stringFormat.append("-");
            } else if (!coefficient.equals(BigInteger.ONE)) {
                stringFormat.append(coefficient).append("*");
            }

            stringFormat.append("x");

            if (unfoldIndexTwo && index == 2) {
                stringFormat.append("*x");
            } else if (index != 1) {
                stringFormat.append("**").append(index);
            }
        }
        return stringFormat.toString();
    }

    @Override
    public String toString() {
        return coefficient.toString() + "x" + "**" + index;
    }

    @Override
    public int compareTo(PolyFactor other) {
        if (this.getCoefficient().compareTo(other.getCoefficient()) < 0) {
            return -1;
        } else if (this.getCoefficient().compareTo(other.getCoefficient()) > 0) {
            return 1;
        }
        if (this.getIndex() < other.getIndex()) {
            return -1;
        } else if (this.getIndex() > other.getIndex()) {
            return 1;
        }
        return 0;
    }
}
