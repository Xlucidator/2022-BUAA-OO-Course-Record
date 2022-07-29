import java.math.BigInteger;
import java.util.TreeMap;

public class Number implements Factor {
    private BigInteger num;

    public Number(BigInteger n) {
        this.num = n;
    }

    @Override
    public void anounce() {
        System.out.println(num + " is loaded");
    }

    @Override
    public TreeMap<BigInteger, BigInteger> simplify() {
        TreeMap<BigInteger, BigInteger> item = new TreeMap<>();
        item.put(BigInteger.valueOf(0), num);
        return item;
    }
}
