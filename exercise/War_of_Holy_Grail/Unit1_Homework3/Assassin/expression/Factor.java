package expression;

import java.math.BigInteger;
import java.util.HashMap;

public interface Factor {
    
    void setSign(int sign);
    
    void setExponent(BigInteger exponent);
    
    int getSign();
    
    BigInteger getExponent();
    
    HashMap<Key, BigInteger> getAns();
    
    Factor clone();
}
