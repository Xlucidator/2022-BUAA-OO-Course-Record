package factor.function;

import factor.Calculator;
import factor.Expr;
import factor.Factor;
import factor.Number;
import factor.Poly;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Sum {
    private final Expr expr;

    public Sum(BigInteger begin, BigInteger end, Expr expr)
            throws IOException, ClassNotFoundException {
        if (begin.compareTo(end) > 0) {
            this.expr = new Expr(new HashSet<>(), new HashSet<>(), new BigInteger("0"));
        } else {
            Poly poly = new Poly();
            BigInteger integer = new BigInteger("0");
            HashMap<Poly, BigInteger> res = new HashMap<>();
            res.put(poly, integer);

            BigInteger i = begin;

            Calculator calculator = new Calculator();
            ArrayList<Factor> factors = new ArrayList<>();
            factors.add(null);
            factors.add(null);
            factors.add(null);
            factors.add(new Number(i));
            factors.add(null);

            while (i.compareTo(end) <= 0) {
                factors.set(3, new Number(i));
                Expr newExpr = expr.deepCopy();
                res = calculator.mergeExpr(res, newExpr.setVar(factors).getExpr());
                i = i.add(new BigInteger("1"));
            }

            this.expr = new Expr(res);
        }
    }

    public Expr getRes() {
        return this.expr;
    }
}
