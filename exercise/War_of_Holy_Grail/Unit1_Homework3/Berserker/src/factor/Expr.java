package factor;

import factor.function.Triangle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Expr implements Factor, Serializable {
    private HashMap<Poly, BigInteger> expr;

    public Expr() {
        this.expr = new HashMap<>();
    }

    public Expr(Triangle triangle) {
        this.expr = new HashMap<>();
        this.expr.put(new Poly(triangle), BigInteger.ONE);

    }

    public Expr(HashMap<Poly, BigInteger> expr) {
        this.expr = expr;
    }

    public Expr(HashSet<Triangle> triangles, HashSet<Var> vars, BigInteger bigInteger) {
        Poly poly = new Poly(triangles, vars);
        this.expr = new HashMap<>();
        this.expr.put(poly, bigInteger);
    }

    public HashMap<Poly, BigInteger> getExpr() {
        return expr;
    }

    public Expr setVar(ArrayList<Factor> factors) throws IOException, ClassNotFoundException {
        Calculator calculator = new Calculator();
        HashMap<Poly, BigInteger> res = new HashMap<>();
        for (Poly poly : expr.keySet()) {
            BigInteger coef = expr.get(poly);
            HashMap<Poly, BigInteger> polyBigIntegerHashMap = poly.setVar(factors);
            polyBigIntegerHashMap.replaceAll((p, v) -> polyBigIntegerHashMap.get(p).multiply(coef));
            res = calculator.mergeExpr(res, polyBigIntegerHashMap);
        }
        return new Expr(res);
    }

    public void mergeTerms(Term term) throws IOException, ClassNotFoundException {
        Calculator calculator = new Calculator();
        HashMap<Poly, BigInteger> expr2 = calculator.termToExpr(term);
        expr = calculator.mergeExpr(expr, expr2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expr expr1 = (Expr) o;
        return Objects.equals(expr, expr1.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr);
    }

    public HashMap<String, BigInteger> mergeRes() {
        HashMap<String, BigInteger> mergeMap = new HashMap<>();
        for (Poly poly : expr.keySet()) {
            BigInteger bigInteger = expr.get(poly);
            String polyToString = poly.toString();
            if (mergeMap.containsKey(polyToString)) {
                mergeMap.put(polyToString, mergeMap.get(polyToString).add(bigInteger));
            } else {
                mergeMap.put(polyToString, bigInteger);
            }
        }
        return mergeMap;
    }

    public String toString() {
        HashMap<String, BigInteger> resMap = mergeRes();
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : resMap.keySet()) {
            BigInteger bigInteger = resMap.get(string);
            if (!(bigInteger.equals(BigInteger.ZERO) || string.equals("0"))) {
                StringBuilder attached = new StringBuilder();
                if (!string.equals("1")) {
                    attached.append(bigInteger);
                    attached.append("*");
                    attached.append(string);
                } else {
                    attached.append(bigInteger);
                }
                if (bigInteger.compareTo(BigInteger.ZERO) > 0) {
                    attached.insert(0, "+");
                }
                if (!string.equals("1") && attached.length() != 2 && ((bigInteger.equals(
                        BigInteger.ONE)) || bigInteger.equals(new BigInteger("-1")))) {
                    attached.deleteCharAt(2);
                    attached.deleteCharAt(1);
                }
                if (bigInteger.compareTo(BigInteger.ZERO) > 0) {
                    stringBuilder.insert(0, attached);
                } else {
                    stringBuilder.append(attached);
                }
            }
        }
        if (stringBuilder.length() == 0) {
            return "0";
        } else {
            String res = stringBuilder.toString();
            if (res.charAt(0) == '+') {
                res = res.substring(1);
            }
            return res;
        }
    }

    public Expr deepCopy() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(this);
        oo.close();
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        Expr res = (Expr) oi.readObject();
        oi.close();
        return res;
    }
}