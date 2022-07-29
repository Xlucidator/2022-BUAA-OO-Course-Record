package expr;

import java.math.BigInteger;
import java.util.HashSet;

public class FactorPower implements Factor {
    private int sign;         //  符号
    private BigInteger coe;   //  系数
    private int index;        //  指数

    public FactorPower(int sign, BigInteger coe, int index) {
        this.sign = sign;
        this.coe = coe;
        this.index = index;
    }

    public int getSign() {
        return sign;
    }

    public BigInteger getCoe() {
        return coe;
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (sign == -1) {
            sb.append("-");
        }
        sb.append(coe.toString());
        sb.append("*");
        sb.append("x**" + index);
        return sb.toString();
    }

    public void mulFactorPower(Factor item) {
        sign = sign * item.getSign();
        coe = coe.multiply(item.getCoe());
        index = index + item.getIndex();
    }

    @Override
    public Expr mulTerm(Term item) {
        return null;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    @Override
    public Expr mulExpr(Expr item) {
        return null;
    }

    @Override
    public HashSet<Term> getTerms() {
        return null;
    }

    @Override
    public Expr upExprPowerToExpr() {
        return null;
    }

    public void setCoe(BigInteger coe) {
        this.coe = coe;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public Expr mulExprFactor(Factor expr) {
        return null;
    }

    public void addFactorPower(Factor f) {
        if (this.index == f.getIndex()) {
            if (f.getSign() == 1) {
                this.coe = this.coe.add(f.getCoe());
            }
            else if (f.getSign() == -1) {
                this.coe = this.coe.subtract(f.getCoe());
            }
        }
    }
}
