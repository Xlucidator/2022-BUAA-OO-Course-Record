import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Number implements Factor {
    public void setNum(BigInteger num) {
        this.num = num;
    }
    
    private BigInteger num;
    
    public Number(BigInteger num) {
        this.num = num;
    }
    
    public Number(String str) {
        this.num = new BigInteger(str);
    }
    
    public BigInteger add(Number other) {
        return this.num.add(other.num);
    }
    
    public TriPoly toTriPoly() {
        return new TriPoly(new Key(BigInteger.ZERO, new HashSet<>()), num);
    }
    
    public Number clone() {
        return new Number(this.num);
    }
    
    public boolean isPre() {
        return false;
    }
    
    public Factor replace(ArrayList<Factor> formPars, ArrayList<Factor> parameters) {
        return this;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Number number = (Number) o;
        return Objects.equals(num, number.num);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(num);
    }
}