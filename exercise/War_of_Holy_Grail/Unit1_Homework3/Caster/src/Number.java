import java.math.BigInteger;

public class Number implements Factor {
    public BigInteger getCoef() {
        return coef;
    }

    private BigInteger coef;

    public BigInteger getExp() {
        return exp;
    }

    private BigInteger exp;

    public Number(BigInteger num) {
        this.coef = num;
        this.exp = BigInteger.valueOf(0);
    }

    public Boolean equals(Factor other) {
        if (!(other instanceof Number)) {
            return false;
        }
        if (!this.exp.equals(((Number) other).exp)) {
            return false;
        }
        if (!this.coef.equals(((Number) other).coef)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return this.coef.toString();
    }

    public Number copy() {
        BigInteger coefTmp = new BigInteger(this.coef.toString());
        return new Number(coefTmp);
    }
}
