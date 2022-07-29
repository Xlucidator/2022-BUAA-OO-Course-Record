package expr;

import java.math.BigInteger;

public class Sum extends Expr {
    private Param param;
    private Number upper;
    private Number lower;

    public Sum() {
        super();
        param = new Param();
        this.upper = new Number(BigInteger.ZERO);
        this.lower = new Number(BigInteger.ZERO);
    }

    public Sum(BigInteger lower, BigInteger upper) {
        super();
        param = new Param();
        this.upper = new Number(upper);
        this.lower = new Number(lower);
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public void setUpper(Number upper) {
        this.upper = upper;
    }

    public void setLower(Number lower) {
        this.lower = lower;
    }

    public Param getParam() {
        return param;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sum(");
        sb.append(lower.toString());
        sb.append(",");
        sb.append(upper.toString());
        sb.append(",");
        sb.append(super.toString());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public Poly toPoly() {
        BigInteger i = new BigInteger(lower.toString());
        Poly p = new Poly();
        if (lower.getNum().compareTo(upper.getNum()) > 0) {
            return p;
        } else {
            while (i.compareTo(upper.getNum()) <= 0) {
                param.setFactor(new Number(i));
                p = p.add(super.toPoly());
                i = i.add(BigInteger.ONE);
            }
        }
        return p;
    }
}
