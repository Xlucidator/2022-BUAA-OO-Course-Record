package factor;

import factor.function.Triangle;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

public class Calculator {

    public HashSet<Var> multVar(HashSet<Var> vars, Var var) {
        HashSet<Var> newVars = new HashSet<>();
        boolean flag = false;
        for (Var var1 : vars) {
            if (var.getIndex() == var1.getIndex()) {
                newVars.add(new Var(var.getIndex(), var.getDegree().add(var1.getDegree())));
                flag = true;
            } else {
                newVars.add(var1.deepCopy());
            }
        }
        if (!flag) {
            newVars.add(var.deepCopy());
        }
        return newVars;
    }

    public HashSet<Var> multVars(HashSet<Var> vars1, HashSet<Var> vars2) {
        HashSet<Var> newVar = new HashSet<>(vars1);
        for (Var var : vars2) {
            newVar = multVar(newVar, var);
        }
        return newVar;
    }

    public HashSet<Triangle> multTriangle(HashSet<Triangle> triangles, Triangle triangle)
            throws IOException, ClassNotFoundException {
        HashSet<Triangle> newTriangles = new HashSet<>();
        boolean flag = false;
        for (Triangle triangle1 : triangles) {
            if (triangle1.getName().equals(triangle.getName()) && triangle1.getExpr()
                    .equals(triangle.getExpr())) {
                newTriangles.add(new Triangle(triangle.getName(), triangle.getExpr().deepCopy(),
                        triangle.getDegree().add(triangle1.getDegree())));
                flag = true;
            } else {
                newTriangles.add(triangle1.deepCopy());
            }
        }
        if (!flag) {
            newTriangles.add(triangle.deepCopy());
        }
        return newTriangles;
    }

    public HashSet<Triangle> multTriangles(HashSet<Triangle> triangles1,
                                           HashSet<Triangle> triangles2)
            throws IOException, ClassNotFoundException {
        HashSet<Triangle> newTriangle = new HashSet<>(triangles1);
        for (Triangle triangle : triangles2) {
            newTriangle = multTriangle(newTriangle, triangle);
        }
        return newTriangle;
    }

    public Poly mergePoly(Poly poly1, Poly poly2) throws IOException, ClassNotFoundException {
        HashSet<Var> resVars = multVars(poly1.getVars(), poly2.getVars());
        HashSet<Triangle> resTriangle = multTriangles(poly1.getTriangles(), poly2.getTriangles());
        return new Poly(resTriangle, resVars);
    }

    public HashMap<Poly, BigInteger> multExpr(HashMap<Poly, BigInteger> expr1,
                                              HashMap<Poly, BigInteger> expr2)
            throws IOException, ClassNotFoundException {
        HashMap<Poly, BigInteger> res = new HashMap<>();
        for (Poly poly1 : expr1.keySet()) {
            BigInteger bigInteger1 = expr1.get(poly1);
            for (Poly poly2 : expr2.keySet()) {
                BigInteger bigInteger2 = expr2.get(poly2);
                Poly newPoly = mergePoly(poly1, poly2);
                if (res.containsKey(newPoly)) {
                    res.put(newPoly, bigInteger1.multiply(bigInteger2).add(res.get(newPoly)));
                } else {
                    res.put(newPoly, bigInteger1.multiply(bigInteger2));
                }
            }
        }
        return res;
    }

    public HashMap<Poly, BigInteger> mergeExpr(HashMap<Poly, BigInteger> expr1,
                                               HashMap<Poly, BigInteger> expr2) {
        HashMap<Poly, BigInteger> res = new HashMap<>(expr1);
        for (Poly poly : expr2.keySet()) {
            BigInteger coef2 = expr2.get(poly);
            if (res.containsKey(poly)) {
                res.put(poly, res.get(poly).add(coef2));
            } else if (poly.isSinCos2() && res.containsKey(poly.getCompanyPoly())) {
                Poly polyInRes = poly.getCompanyPoly();
                BigInteger coefInRes = res.get(polyInRes);
                Poly newPoly1 = polyInRes.deleteTriangle();
                BigInteger newCoef2 = coef2.subtract(coefInRes);
                res.remove(polyInRes);
                if (res.containsKey(newPoly1)) {
                    res.put(newPoly1, res.get(newPoly1).add(coefInRes));
                } else {
                    res.put(newPoly1, coefInRes);
                }
                if (!newCoef2.equals(BigInteger.ZERO)) {
                    res.put(poly, newCoef2);
                }
            } else {
                res.put(poly, coef2);
            }
        }
        return res;
    }

    public HashMap<Poly, BigInteger> power(Expr expr, BigInteger times)
            throws IOException, ClassNotFoundException {
        if (times.equals(BigInteger.ZERO)) {
            HashMap<Poly, BigInteger> res = new HashMap<>();
            res.put(new Poly(), BigInteger.ONE);
            return res;
        }
        HashMap<Poly, BigInteger> res = new HashMap<>(expr.deepCopy().getExpr());
        BigInteger newTime = times.subtract(BigInteger.ONE);
        while (newTime.compareTo(BigInteger.ZERO) > 0) {
            res = multExpr(res, expr.deepCopy().getExpr());
            newTime = newTime.subtract(BigInteger.ONE);
        }
        return res;
    }

    public HashMap<Poly, BigInteger> termToExpr(Term term)
            throws IOException, ClassNotFoundException {
        HashSet<Var> vars = new HashSet<>();
        HashSet<Triangle> triangles = new HashSet<>();
        Poly poly = new Poly(triangles, vars);
        BigInteger bigInteger = BigInteger.ONE;
        HashMap<Poly, BigInteger> expr = new HashMap<>();
        expr.put(poly, bigInteger);

        for (Factor factor : term.getFactor()) {
            if (factor instanceof Triangle) {
                Triangle triangle = (Triangle) factor;
                HashSet<Triangle> triangles1 = new HashSet<>();
                HashSet<Var> vars1 = new HashSet<>();
                triangles1.add(triangle);
                Expr newExpr = new Expr(triangles1, vars1, BigInteger.ONE);
                expr = multExpr(expr, newExpr.getExpr());
            } else if (factor instanceof Var) {
                Var var = (Var) factor;
                HashSet<Triangle> triangles1 = new HashSet<>();
                HashSet<Var> vars1 = new HashSet<>();
                vars1.add(var);
                Expr newExpr = new Expr(triangles1, vars1, BigInteger.ONE);
                expr = multExpr(expr, newExpr.getExpr());
            } else if (factor instanceof Number) {
                Number number = (Number) factor;
                BigInteger bigInteger1 = number.getNum();
                for (Poly poly1 : expr.keySet()) {
                    expr.put(poly1, expr.get(poly1).multiply(bigInteger1));
                }
            } else if (factor instanceof Expr) {
                Expr expr1 = (Expr) factor;
                expr = multExpr(expr, expr1.getExpr());
            }
        }
        BigInteger sign = term.getSign();
        if (sign.equals(BigInteger.ONE)) {
            return expr;
        } else {
            HashMap<Poly, BigInteger> newExpr = new HashMap<>();
            for (Poly poly1 : expr.keySet()) {
                newExpr.put(poly1, expr.get(poly1).multiply(sign));
            }
            return newExpr;
        }
    }
}
