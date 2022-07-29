package expression;

import java.math.BigInteger;
import java.util.Objects;

public class BasicClass {
    private int sign = 1;
    private BigInteger exponent = BigInteger.ONE;
    
    public void setSign(int sign) {
        this.sign = sign;
    }
    
    public void setExponent(BigInteger exponent) {
        this.exponent = exponent;
    }
    
    public int getSign() {
        return sign;
    }
    
    public BigInteger getExponent() {
        return exponent;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BasicClass that = (BasicClass) o;
        return sign == that.sign && Objects.equals(exponent, that.exponent);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(sign, exponent);
    }
    
}
