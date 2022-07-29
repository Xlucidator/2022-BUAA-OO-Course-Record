import java.math.BigInteger;

public interface Factor {
    Factor copy();

    BigInteger getExp();

    BigInteger getCoef();

    Boolean equals(Factor other);
}
