
import java.math.BigInteger;

public class Variable implements Factor {

    private BigInteger exp;
    private BigInteger coef;
    private String name;

    public BigInteger getCoef() {
        return coef;
    }

    public String getName() {
        return name;
    }

    public BigInteger getExp() {
        return exp;
    }

    public Variable(BigInteger num, String name) {
        this.exp = num;
        this.name = name;
        this.coef = new BigInteger("1");
    }

    public String toString() {
        if (this.exp.equals(BigInteger.valueOf(0))) {
            return "1";
        }
        else if (this.exp.equals(BigInteger.valueOf(1))) {
            return "x";
        }
        else {
            return "x**" + this.exp;
        }
    }

    public Boolean equals(Factor other) {
        if (!(other instanceof Variable)) {
            return false;
        }
        if (!this.exp.equals(((Variable) other).exp)) {
            return false;
        }
        if (!this.coef.equals(((Variable) other).coef)) {
            return false;
        }
        if (!this.name.equals(((Variable) other).name)) {
            return false;
        }
        return true;
    }

    public Variable copy() {
        BigInteger expTmp = new BigInteger(this.exp.toString());
        String nameTmp = this.name;
        return new Variable(expTmp, nameTmp);
    }
}
