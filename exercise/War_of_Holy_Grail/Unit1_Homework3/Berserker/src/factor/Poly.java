package factor;

import factor.function.Triangle;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Poly implements Serializable {
    private final HashSet<Triangle> triangles;
    private final HashSet<Var> vars;
    // poly = Triangle ** BigInteger * Var ** BigInteger

    public Poly() {
        this.triangles = new HashSet<>();
        this.vars = new HashSet<>();
    }

    public Poly(Triangle triangle) {
        this.triangles = new HashSet<>();
        this.vars = new HashSet<>();
        triangles.add(triangle);
    }

    public Poly(HashSet<Triangle> triangles, HashSet<Var> vars) {
        this.triangles = triangles;
        this.vars = vars;
    }

    public HashMap<Poly, BigInteger> setVar(ArrayList<Factor> factors)
            throws IOException, ClassNotFoundException {
        Calculator calculator = new Calculator();
        HashMap<Poly, BigInteger> res = new HashMap<>();

        res.put(new Poly(), BigInteger.ONE);

        for (Triangle triangle : triangles) {
            res = calculator.multExpr(res, triangle.setVar(factors).getExpr());
        }
        for (Var var : vars) {
            res = calculator.multExpr(res, var.setVar(factors.get(var.getIndex())).getExpr());
        }
        return res;
    }

    public HashSet<Triangle> getTriangles() {
        return triangles;
    }

    public HashSet<Var> getVars() {
        return vars;
    }

    public boolean isSinCos2() {
        if (triangles.size() == 1) {
            for (Triangle triangle : triangles) {
                return triangle.getDegree().equals(new BigInteger("2"));
            }
        }
        return false;
    }

    public Poly getCompanyPoly() {
        HashSet<Triangle> companyTriangles = new HashSet<>();
        HashSet<Var> companyVars = new HashSet<>(vars);
        for (Triangle triangle : triangles) {
            Triangle companyTriangle = null;
            if (triangle.getName().equals("sin")) {
                companyTriangle = new Triangle("cos", triangle.getExpr(), triangle.getDegree());
            } else {
                companyTriangle = new Triangle("sin", triangle.getExpr(), triangle.getDegree());
            }
            companyTriangles.add(companyTriangle);
        }
        return new Poly(companyTriangles, companyVars);
    }

    public Poly deleteTriangle() {
        HashSet<Var> newVars = new HashSet<>();
        for (Var var : vars) {
            newVars.add(var.deepCopy());
        }
        return new Poly(new HashSet<>(), newVars);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Poly poly = (Poly) o;
        return Objects.equals(triangles, poly.triangles) && Objects.equals(vars, poly.vars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triangles, vars);
    }

    public String toString() {
        ArrayList<String> strings = new ArrayList<>();
        for (Var var : vars) {
            String string = var.toString();
            if (!string.equals("1")) {
                strings.add(var.toString());
            }
        }
        for (Triangle triangle : triangles) {
            String string = triangle.toString();
            if (string.equals("0")) {
                return "0";
            } else if (!string.equals("1")) {
                strings.add(triangle.toString());
            }
        }
        String res = String.join("*", strings);
        if (res.equals("")) {
            return "1";
        }
        return res;  // 返回值只能是0 or 1 or res
    }
}
