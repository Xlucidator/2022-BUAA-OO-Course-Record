import java.math.BigInteger;
import java.util.TreeMap;

public class X implements Factor {
    private BigInteger index = BigInteger.valueOf(1);

    public void setIndex(BigInteger index) {
        this.index = index;
    }

    public void anounce() {
        System.out.println("x is loaded");
    }

    @Override
    public TreeMap<BigInteger, BigInteger> simplify() {
        TreeMap<BigInteger, BigInteger> item = new TreeMap<>();
        item.put(index, BigInteger.valueOf(1));
        return item;
    }
}
