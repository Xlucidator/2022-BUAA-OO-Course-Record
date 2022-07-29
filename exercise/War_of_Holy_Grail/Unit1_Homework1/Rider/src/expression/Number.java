package expression;

import java.math.BigInteger;

public class Number implements Base {
    private final BigInteger value;

    public Number(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
