package factor;

import factor.function.Triangle;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Var implements Factor, Serializable {
    private final int index;
    private BigInteger degree;

    public Var(int index, BigInteger degree) {
        this.index = index;  // index = "jklix".indexof(str)
        this.degree = degree;
    }

    public int getIndex() {
        return index;
    }

    public BigInteger getDegree() {
        return degree;
    }

    public Expr setVar(Factor para) throws IOException, ClassNotFoundException {
        HashSet<Triangle> triangles = new HashSet<>();
        HashSet<Var> vars = new HashSet<>();

        if (para == null) {
            vars.add(this);
            return new Expr(triangles, vars, BigInteger.ONE);
        }
        if (para instanceof Number) {
            Number number = (Number) para;
            BigInteger res = BigInteger.ONE;
            while (degree.compareTo(BigInteger.ZERO) > 0) {
                res = res.multiply(number.getNum());
                degree = degree.subtract(BigInteger.ONE);
            }
            return new Expr(triangles, vars, res);
        } else if (para instanceof Var) {
            Var var = (Var) para;
            vars.add(new Var(var.getIndex(), var.getDegree().multiply(this.degree)));
            return new Expr(triangles, vars, BigInteger.ONE);
        } else if (para instanceof Triangle) {
            Triangle triangle = (Triangle) para;
            triangles.add(new Triangle(triangle.getName(), triangle.getExpr(),
                    triangle.getDegree().multiply(this.degree)));
            return new Expr(triangles, vars, BigInteger.ONE);
        } else {  // expr
            Expr expr = (Expr) para;
            Calculator calculator = new Calculator();
            HashMap<Poly, BigInteger> res = calculator.power(expr, degree);
            return new Expr(res);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Var var = (Var) o;
        return index == var.index && Objects.equals(degree, var.degree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, degree);
    }

    public Var deepCopy() {
        return new Var(index, degree);
    }

    @Override
    public String toString() {
        if (degree.equals(BigInteger.ZERO)) {
            return "1";
        } else if (degree.equals(BigInteger.ONE)) {
            return "x";
        } else if (degree.equals(new BigInteger("2"))) {
            return "x*x";
        } else {
            return "x**" + degree;
        }

    }
}
