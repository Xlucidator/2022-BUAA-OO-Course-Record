package expr;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;

public class Term {
    private final HashSet<Factor> factors;
    private int sign;
    private BigInteger coe;
    private int index;

    public Term() {
        this.factors = new HashSet<>();
        sign = 1;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getSign() {
        return sign;
    }

    public HashSet<Factor> getFactors() {
        /*if(sign==-1){
            for(Factor f:factors){
                f.setSign(f.getSign()*-1);
            }
        }*/
        return factors;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public String toString() {
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        if (sign == -1) {
            sb.append(" - ");
        }
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append(" *");
            sb.append(iter.next().toString());
            sb.append(" ");
            while (iter.hasNext()) {
                sb.append("*");
                sb.append(iter.next().toString());
            }
        }
        return sb.toString();
    }

    public Expr mulTerm(Term item) {
        Expr ans = new Expr();
        Term term = new Term();
        FactorPower f = new FactorPower(item.getSign() * this.getSign(), BigInteger.ONE, 0);
        term.addFactor(f);
        ans.addTerm(term);
        for (Factor factorF : item.getFactors()) {
            if (factorF instanceof FactorPower) {
                ans.mulFactorPower(factorF);
            } else if (factorF instanceof ExprPower) {
                factorF = factorF.upExprPowerToExpr();
                ans = ans.mulExprFactor(factorF);
            } else if (factorF instanceof Expr) {
                ans = ans.mulExprFactor(factorF);
            }
        }
        for (Factor factorF : this.getFactors()) {
            if (factorF instanceof FactorPower) {
                ans.mulFactorPower(factorF);
            } else if (factorF instanceof ExprPower) {
                factorF = factorF.upExprPowerToExpr();
                ans = ans.mulExprFactor(factorF);
            } else if (factorF instanceof Expr) {
                ans = ans.mulExprFactor(factorF);
            }
        }
        return ans;
    }

    public void mulFactPower(Factor item) {
        for (Factor factor : this.getFactors()) {
            factor.mulFactorPower(item);
        }
    }

    public Expr upTerm() {
        /*TODO*/
        // 将多个因子乘开，变为新的没有括号的项
        Expr ans = new Expr();
        Term term = new Term();
        BigInteger sign = BigInteger.ONE;
        if (this.getSign() == -1) {
            sign = sign.negate();
        }
        FactorPower f = new FactorPower(1, sign, 0);
        term.addFactor(f);
        ans.addTerm(term);
        for (Factor item : this.getFactors()) {
            if (item instanceof FactorPower) {
                ans.mulFactorPower(item);
            } else if (item instanceof ExprPower) {
                item = item.upExprPowerToExpr();
                ans = ans.mulExprFactor(item);
            } else if (item instanceof Expr) {
                ans = ans.mulExprFactor(item);
            }
        }
        return ans;
    }
}
