import java.math.BigInteger;
import java.util.TreeMap;

interface Factor {
    public void anounce();

    public TreeMap<BigInteger, BigInteger> simplify();
}
