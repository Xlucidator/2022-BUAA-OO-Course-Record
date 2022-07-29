package expr;

import java.math.BigInteger;
import java.util.HashSet;

public class ExprPower extends Expr implements Factor {
    private Expr expr;
    private int index;
    private int sign;

    public ExprPower(int index, Expr expr) {
        this.index = index;
        this.expr = expr;
        this.sign = 1;
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
            return expr.toString();
        } else {
            return expr.toString() + "**" + index;
        }
    }

    public Expr upExprPowerToExpr() {
        /*TODO*/
        // 将表达式乘方展开 为 一个表达式
        if (index == 0) {
            Expr ans = new Expr();
            Term t = new Term();
            FactorPower f = new FactorPower(1, BigInteger.ONE, 0);
            t.addFactor(f);
            ans.addTerm(t);
            return ans;
        } else {
            Expr base = this.getExpr();
            Expr ans = this.getExpr();
            int i = 0;
            for (i = 1; i < index; i++) {
                ans = ans.mulExpr(base);
            }
            return ans;
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
    public Expr mulTerm(Term item) {
        return null;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public BigInteger getCoe() {
        return null;
    }

    @Override
    public void mulFactorPower(Factor item) {
        return;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public Expr mulExprFactor(Factor expr) {
        return null;
    }
}
