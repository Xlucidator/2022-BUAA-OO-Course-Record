import java.math.BigInteger;

public class Sum implements Factor {
    public BigInteger getCoef() {
        return coef;
    }

    @Override
    public Boolean equals(Factor other) {
        return null;
    }

    public BigInteger getExp() {
        return exp;
    }

    private BigInteger coef;
    private BigInteger exp;
    private BigInteger start;
    private BigInteger end;
    private Expr expr;

    public Sum(BigInteger coef, BigInteger exp, BigInteger start, BigInteger end, Expr expr) {
        this.coef = coef;
        this.exp = exp;
        this.start = start;
        this.end = end;
        this.expr = expr;
    }

    public Sum(BigInteger s, BigInteger e, Expr expr) {
        this.exp = BigInteger.valueOf(1);
        this.coef = BigInteger.valueOf(1);
        this.start = s;
        this.end = e.add(BigInteger.valueOf(1));
        this.expr = expr;
    }

    public Expr compute() {
        Expr resExpr = new Expr();
        if (start.compareTo(end) > 0) {
            return resExpr;
        }
        BigInteger i = start;
        for (; !i.equals(end); i = i.add(BigInteger.valueOf(1))) {
            Expr tmpExpr = expr.copy();
            expr.replace(tmpExpr, new Number(i));
            tmpExpr.compute();
            resExpr.addExpr(tmpExpr);
        }
        resExpr.simplify();
        return resExpr;
    }

    public String toString() {
        return "sum(i, " + this.start.toString() +
                ", " + this.end + ", " + this.expr.toString() + ")";
    }

    public Sum copy() {
        BigInteger startTmp = new BigInteger(this.start.toString());
        BigInteger endTmp = new BigInteger(this.end.toString());
        BigInteger expTmp = new BigInteger(this.exp.toString());
        BigInteger coefTmp = new BigInteger(this.coef.toString());
        Expr exprTmp = expr.copy();
        return new Sum(coefTmp, expTmp, startTmp, endTmp, exprTmp);
    }

}
