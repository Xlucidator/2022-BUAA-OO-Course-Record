package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Var extends Factor {
    private String name;
    
    public Var(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public Factor replace(ArrayList<Factor> parameters, ArrayList<Factor> actualParameters) {
        Factor ref = this.clone();
        if (parameters.contains(this)) {
            ref = actualParameters.get(parameters.indexOf(this)).clone();
        }
        return ref;
    }
    
    @Override
    public void update() {
        HashMap<HashMap<Factor, BigInteger>, BigInteger> ref = new HashMap<>();
        HashMap<Factor, BigInteger> power = new HashMap<>();
        power.put(this, BigInteger.ONE);
        ref.put(power, BigInteger.ONE);
        setPoly(ref);
    }
    
    @Override
    public Factor clone() {
        Var o = new Var(this.name);
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
        Var var = (Var) o;
        return Objects.equals(name, var.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
