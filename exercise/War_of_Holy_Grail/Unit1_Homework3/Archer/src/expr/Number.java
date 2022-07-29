package expr;

import java.math.BigInteger;

public class Number implements Factor {
    private BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public void setNum(BigInteger num) { this.num = num; }

    public BigInteger getNum() { return num; }

    public String toString() {
        return this.num.toString();
    }

    public Poly toPoly() {
        Poly p = new Poly();
        if (num.compareTo(BigInteger.ZERO) == 0) {
            return p;
        }
        p.addItem(new CoPo(num, BigInteger.ZERO));
        return p;
    }
}
