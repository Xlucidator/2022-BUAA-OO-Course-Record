package expr;

import java.math.BigInteger;
import java.util.HashSet;

public class Number implements Factor {
    private final BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public String toString() {
        return this.num.toString();
    }

    @Override
    public void mulFactorPower(Factor item) {

    }

    @Override
    public Expr mulTerm(Term item) {
        return null;
    }

    @Override
    public void setSign(int sign) {

    }

    @Override
    public BigInteger getCoe() {
        return null;
    }

    @Override
    public HashSet<Term> getTerms() {
        return null;
    }

    @Override
    public Expr mulExpr(Expr item) {
        return null;
    }

    @Override
    public int getSign() {
        return 0;
    }

    @Override
    public int getIndex() {
        return 0;
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