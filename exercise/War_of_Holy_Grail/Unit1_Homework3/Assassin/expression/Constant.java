package expression;

import java.math.BigInteger;
import java.util.HashMap;

public class Constant extends BasicClass implements Factor {
    private final BigInteger num;
    
    //    private final HashMap<Integer, BigInteger> ans = new HashMap<>();
    private final HashMap<Key, BigInteger> ans = new HashMap<>();
    private boolean ansAlready = false;
    
    public Constant(BigInteger num) {
        this.num = num;
    }
    
    @Override
    public HashMap<Key, BigInteger> getAns() {
        BigInteger bigSign = new BigInteger(String.valueOf(this.getSign()));
        if (ansAlready) {
            return ans;
        }
        if (this.getExponent().equals(BigInteger.ZERO)) { // if exponent == 0
            Key key = new Key(BigInteger.ZERO);
            ans.put(key, bigSign);
            ansAlready = true;
            return ans;
        }
        BigInteger coefficient = num;
        if (!this.getExponent().equals(BigInteger.ONE)) {
            for (BigInteger i = BigInteger.ONE; i.compareTo(this.getExponent()) < 0;
                 i = i.add(BigInteger.ONE)) {
                coefficient = coefficient.multiply(num);
            }
        }
        Key key = new Key(BigInteger.ZERO);
        ans.put(key, bigSign.multiply(coefficient));
        ansAlready = true;
        return ans;
    }
    
    @Override
    public Constant clone() {
        Constant constant = new Constant(this.num);
        constant.setExponent(this.getExponent().add(BigInteger.ZERO));
        constant.setSign(this.getSign());
        return constant;
    }
}
