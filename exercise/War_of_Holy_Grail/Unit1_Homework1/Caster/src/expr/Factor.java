package expr;

import java.math.BigInteger;
import java.util.HashSet;

public interface Factor {
    public int getSign();

    public BigInteger getCoe();

    public int getIndex();

    public void setSign(int sign);

    public HashSet<Term> getTerms();

    public void mulFactorPower(Factor item);

    public Expr mulExpr(Expr item);

    public Expr mulExprFactor(Factor expr);

    public Expr mulTerm(Term item);

    public Expr upExprPowerToExpr();
}
