package factor.function;

import factor.Factor;
import factor.Expr;
import factor.Poly;
import factor.Var;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Triangle implements Factor, Serializable {
    private final String name;
    private Expr expr;
    private final BigInteger degree;

    public Triangle(String name, Expr expr, BigInteger degree) {
        this.name = name;
        this.expr = expr;
        this.degree = degree;
    }

    public Expr getExpr() {
        return expr;
    }

    public String getName() {
        return name;
    }

    public BigInteger getDegree() {
        return degree;
    }

    public Expr setVar(ArrayList<Factor> factors) throws IOException, ClassNotFoundException {
        this.expr = expr.setVar(factors).deepCopy();
        return new Expr(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Triangle triangle = (Triangle) o;
        return Objects.equals(name, triangle.name) && Objects.equals(expr,
                triangle.expr) && Objects.equals(degree, triangle.degree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, expr, degree);
    }

    public Triangle deepCopy() throws IOException, ClassNotFoundException {
        return new Triangle(name, expr.deepCopy(), degree);
    }

    public String triangleExpr() {
        HashMap<Poly, BigInteger> expr = this.expr.getExpr();
        if (expr.size() == 1) {
            for (Poly poly : expr.keySet()) {
                BigInteger coef = expr.get(poly);
                HashSet<Triangle> triangles = poly.getTriangles();
                HashSet<Var> vars = poly.getVars();
                if (triangles.size() == 1 && vars.size() == 0 && coef.equals(BigInteger.ONE)) {
                    for (Triangle triangle : triangles) {
                        return triangle.toString();
                    }
                } else if (triangles.size() == 0 && vars.size() == 1 && coef.equals(
                        BigInteger.ONE)) {
                    for (Var var : vars) {
                        return var.toString().replaceAll("x\\*x", "x**2");
                    }
                }
            }
        }
        String string = this.expr.toString();
        if (string.matches("\\d+") || (string.length() >= 2 && string.charAt(
                0) == '-' && string.substring(1).matches("\\d+"))) {
            return string;
        }
        return "(" + string + ")";
    }

    @Override
    public String toString() {
        if (degree.equals(BigInteger.ZERO)) {
            return "1";
        } else if (expr.toString().equals("0")) {
            if (name.equals("sin")) {
                return "0";
            } else {
                return "1";
            }
        } else if (degree.equals(BigInteger.ONE)) {
            return name + "(" + triangleExpr() + ")";
        } else {
            return name + "(" + triangleExpr() + ")" + "**" + degree;
        }
    }
}
