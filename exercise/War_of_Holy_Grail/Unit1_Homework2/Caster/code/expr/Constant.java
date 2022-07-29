package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

//需要修改的地方：HashMap
public class Constant extends Factor {
    private BigInteger num;
    
    public Constant(String num) {
        this.num = new BigInteger(num);
    }
    
    public BigInteger getNum() {
        return this.num;
    }
    
    @Override
    public void update() {
        HashMap<HashMap<Factor, BigInteger>, BigInteger> ref = new HashMap<>();
        HashMap<Factor, BigInteger> powers = new HashMap<>();
        ref.put(powers, num);
        setPoly(ref);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!this.isPos()) {
            sb.append("-");
        }
        sb.append(num.toString());
        return sb.toString();
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
    public Factor clone() {
        Constant o = new Constant(this.num.toString());
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
        Constant constant = (Constant) o;
        return Objects.equals(num, constant.num);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(num);
    }
}
