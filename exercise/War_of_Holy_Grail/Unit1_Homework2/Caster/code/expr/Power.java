package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Power extends Factor {
    private Var base;
    private Constant ind;
    
    public Power(Var base, Constant number) {
        this.base = base;
        this.ind = number;
    }
    
    @Override
    public Factor replace(ArrayList<Factor> parameters, ArrayList<Factor> actualParameters) {
        Power ref = (Power) this.clone();
        ref.base = (Var) ref.base.replace(parameters, actualParameters);
        ref.ind = (Constant) ref.ind.replace(parameters, actualParameters);
        return ref;
    }
    
    @Override
    public void update() {
        HashMap<HashMap<Factor, BigInteger>, BigInteger> ref = new HashMap<>();
        HashMap<Factor, BigInteger> power = new HashMap<>();
        power.put(base, BigInteger.ONE);
        ref.put(power, BigInteger.ONE);
        setPoly(ref);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!this.isPos()) {
            sb.append("-");
        }
        sb.append(base);
        if (!(ind.isPos() && ind.getNum().equals(BigInteger.ONE))) {
            sb.append("**");
            sb.append(ind.toString());
        }
        return sb.toString();
    }
    
    @Override
    public Factor clone() {
        Power o = new Power(this.base, this.ind);
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
        Power power = (Power) o;
        return base.equals(power.base) && ind.equals(power.ind);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(base, ind);
    }
}
