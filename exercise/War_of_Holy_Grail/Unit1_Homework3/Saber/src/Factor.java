public interface Factor {
    // include sin(), cos(), x
    public int getPos();

    public Expr getExpr();

    public int compareTo(Factor other);
}
