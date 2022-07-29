package factor;

import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private final ArrayList<Factor> factors;
    private final BigInteger sign;

    public Term(int sign) {
        this.factors = new ArrayList<>();
        this.sign = new BigInteger(String.valueOf(sign));
    }

    public void addFactor(Factor factor) {
        factors.add(factor);
    }

    public ArrayList<Factor> getFactor() {
        return factors;
    }

    public BigInteger getSign() {
        return sign;
    }

}
