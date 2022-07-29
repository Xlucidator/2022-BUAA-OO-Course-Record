package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Cos extends Factor {
    private Expr expr;
    
    public Cos(Expr expr) {
        this.expr = expr;
    }
    
    @Override
    public void update() {
        expr.update();
        //内部符号
        boolean withNeg = false;
        for (HashMap<Factor, BigInteger> key : expr.getPoly().keySet()) {
            BigInteger value = expr.getPoly().get(key);
            if (value.signum() < 0) {
                withNeg = true;
                break;
            }
        }
        if (withNeg) {
            for (HashMap<Factor, BigInteger> key : expr.getPoly().keySet()) {
                expr.getPoly().replace(key, expr.getPoly().get(key).negate());
            }
        }
        HashMap<HashMap<Factor, BigInteger>, BigInteger> ref = new HashMap<>();
        HashMap<Factor, BigInteger> power = new HashMap<>();
        if (expr.display().equals("0")) {
            ref.put(power, BigInteger.ONE);
        } else {
            power.put(this, BigInteger.ONE);
            ref.put(power, BigInteger.ONE);
        }
        setPoly(ref);
    }
    
    @Override
    public Factor replace(ArrayList<Factor> parameters, ArrayList<Factor> actualParameters) {
        Cos ref = (Cos) this.clone();
        ref.expr = (Expr) ref.expr.replace(parameters, actualParameters);
        return ref;
    }
    
    @Override
    public Factor clone() {
        Cos o = new Cos((Expr) this.expr.clone());
        if (!this.isPos()) {
            o.changePos();
        }
        return o;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cos cos = (Cos) o;
        return Objects.equals(expr, cos.expr);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(expr);
    }
    
    @Override
    public String toString() {
        String ref;
        if (expr.display().equals("x*x")) {
            ref = "x**2";
        } else {
            ref = expr.display();
        }
        return "cos(" + ref + ")";
    }
    
    public Sin changeToSin() {
        Sin sin = new Sin(this.expr);
        return sin;
    }
}
