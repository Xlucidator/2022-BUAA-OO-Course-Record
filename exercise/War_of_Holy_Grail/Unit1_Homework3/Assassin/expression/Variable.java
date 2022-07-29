package expression;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;

public class Variable extends BasicClass implements Factor {
    
    private final String name;
    private final HashMap<Key, BigInteger> ans = new HashMap<>();
    private boolean ansAlready = false;
    
    public Variable(String  name) {
        this.name = name;
    }
    
    public HashMap<Key, BigInteger> getAns() {
        BigInteger bigSign = new BigInteger(String.valueOf(this.getSign()));
        if (ansAlready) {
            return ans;
        }
        Key key = new Key(this.getExponent());
        ans.put(key, bigSign);
        ansAlready = true;
        return ans;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public Variable clone() {
        Variable variable = new Variable(this.getName().substring(0));
        variable.setExponent(this.getExponent().add(BigInteger.ZERO));
        variable.setSign(this.getSign());
        return variable;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Variable variable = (Variable) o;
        return Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
