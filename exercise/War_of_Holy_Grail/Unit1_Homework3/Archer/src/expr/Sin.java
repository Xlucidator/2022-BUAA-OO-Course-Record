package expr;

import java.math.BigInteger;

public class Sin implements Factor {
    private Expr expr = new Expr();

    public void addExpr(Expr expr) {
        this.expr = expr;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sin(");
        sb.append(expr.toString());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public Poly toPoly() {
        Poly p = new Poly();
        CoPo c = new CoPo(BigInteger.ONE, BigInteger.ZERO);
        TriItem t = new TriItem(BigInteger.ONE, "sin", expr.toPoly());
        c.setTriItem(t);
        p.addItem(c);
        return p;
    }

}
