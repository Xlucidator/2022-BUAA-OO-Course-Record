package expression;

import java.math.BigInteger;
import java.util.ArrayList;

public class Sum {
    private final Factor iter;
    private final BigInteger bottom;
    private final BigInteger top;
    private final Expr expr;
    
    public Sum(Factor iter, BigInteger bottom, BigInteger top, Expr expr) {
        this.iter = iter;
        this.bottom = bottom;
        this.top = top;
        this.expr = expr;
    }

    public Expr toExpr() {
        Expr replacedExpr = new Expr();
        ArrayList<Factor> parameters = new ArrayList<>();
        ArrayList<Factor> arguments = new ArrayList<>();
        if (bottom.compareTo(top) > 0) {
            Term midTerm = new Term();
            Constant zero = new Constant(BigInteger.ZERO);
            midTerm.addFactor(zero);
            replacedExpr.addTerm(midTerm);
            return replacedExpr;
        }
        parameters.add(this.iter);
        for (BigInteger i = bottom; i.compareTo(top) < 0 ||
                i.compareTo(top) == 0; i = i.add(BigInteger.ONE)) {
            Constant argument = new Constant(i);
            arguments.add(argument);
            final Term midTerm = new Term();
            Expr replaceFactor = this.expr.replace(parameters, arguments).clone();
            replaceFactor.getAns();
            replaceFactor.simplify();
            replaceFactor.setSign(this.expr.getSign());
            replaceFactor.setExponent(this.expr.getExponent().multiply(BigInteger.ONE));
            midTerm.addFactor(replaceFactor);
            replacedExpr.addTerm(midTerm);
            arguments.clear();
        }
        return replacedExpr;
    }
}
