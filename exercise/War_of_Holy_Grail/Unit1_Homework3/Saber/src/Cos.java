public class Cos implements Factor {
    private Expr expr;
    private int pos = 2;

    public Cos(Expr expr) {
        this.expr = expr;
        this.pos = 2;
    }

    public Expr getExpr() {
        return expr;
    }

    public int getPos() {
        return pos;
    }

    public int compareTo(Factor other) {
        if (this.pos != other.getPos()) {
            return 1;
        }
        if (this.expr.compareTo(other.getExpr()) == 1) {
            return 1;
        }
        return 0;
    }
}
