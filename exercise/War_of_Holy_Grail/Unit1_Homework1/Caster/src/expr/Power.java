package expr;

import java.math.BigInteger;
import java.util.HashSet;

public class Power implements Factor {
    private int index;
    private int sign;

    public Power(int index) {
        this.index = index;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getSign() {
        return sign;
    }

    public String toString() {
        if (index == 0) {
            return "1";
        } else if (index == 1) {
            return "x";
        } else {
            return "x**" + index;
        }
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
    public BigInteger getCoe() {
        return null;
    }

    @Override
    public Expr mulTerm(Term item) {
        return null;
    }

    @Override
    public void mulFactorPower(Factor item) {

    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Expr mulExprFactor(Factor expr) {
        return null;
    }

    @Override
    public Expr upExprPowerToExpr() {
        return null;
    }
}
