package expr;

public class Param implements Factor {
    private Factor factor;

    public Param() {}

    public String toString() {
        if (factor == null) {
            return "i";
        } else {
            return factor.toString();
        }
    }

    public Factor getFactor() {
        return factor;
    }

    public void setFactor(Factor factor) {
        this.factor = factor;
    }

    public Poly toPoly() {
        return factor.toPoly();
    }
}
