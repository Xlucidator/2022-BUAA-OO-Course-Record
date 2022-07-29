public class Sin implements Factor {
    private Expr expr;
    private int pos = 1;

    public Sin(Expr expr) {
        this.expr = expr;
        this.pos = 1;
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
