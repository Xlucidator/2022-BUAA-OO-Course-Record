import java.math.BigInteger;

public interface ValueBody extends Comparable<ValueBody> {
    int getId();

    BigInteger getValue();

    void valueUsedBy(Adventurer user) throws Exception;

    String toString();
}
