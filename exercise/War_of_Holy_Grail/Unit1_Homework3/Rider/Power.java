import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Power implements Factor, Cloneable {
    public int getType() {
        return type;
    }
    
    private final int type;//1-x, 2-y, 3-z(目前只关心形参中可能出现y/z)
    
    public BigInteger getIndex() {
        return index;
    }
    
    private final BigInteger index;
    
    public Power(int type, BigInteger index) {
        this.type = type;
        this.index = index;
    }
    
    public TriPoly toTriPoly() {
        return new TriPoly(new Key(index, new HashSet<>()), new BigInteger("1"));
    }
    
    public Power clone() {
        return new Power(this.type, this.index);
    }
    
    public boolean isPre() {
        return type != 1;
    }
    
    public Factor replace(ArrayList<Factor> formPars, ArrayList<Factor> parameters) {
        for (int i = 0; i < formPars.size(); i++) {
            if (formPars.get(i) instanceof Power) {
                Power power = (Power) formPars.get(i);
                if (this.getType() == power.getType()) {
                    return parameters.get(i).clone();//需不需要clone,需要
                }
            }
        }
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
        Power power = (Power) o;
        return type == power.type && Objects.equals(index, power.index);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
