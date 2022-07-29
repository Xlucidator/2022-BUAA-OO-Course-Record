import java.math.BigInteger;
import java.util.ArrayList;
import java.util.TreeMap;

public class Term {
    private ArrayList<Factor> factors = new ArrayList<>();
    private boolean sign = true;

    public void reverse() {
        if (sign == true) {
            sign = false;
        } else {
            sign = true;
        }
    }

    public void addFactor(Factor factor) {
        factors.add(factor);
    }

    public TreeMap<BigInteger, BigInteger> simplify() {
        TreeMap<BigInteger, BigInteger> res = new TreeMap<>();
        res.put(BigInteger.valueOf(0), BigInteger.valueOf(1));
        for (Factor factor : factors) {
            TreeMap<BigInteger, BigInteger> otherRes = factor.simplify();
            res = multMap(res, otherRes);
        }
        if (sign == false) {
            for (BigInteger k : res.keySet()) {
                res.put(k, res.get(k).negate());
            }
        }
        return res;
    }

    public TreeMap<BigInteger, BigInteger> multMap(TreeMap<BigInteger, BigInteger> map1,
            TreeMap<BigInteger, BigInteger> map2) {
        TreeMap<BigInteger, BigInteger> res = new TreeMap<>();
        for (BigInteger key1 : map1.keySet()) {
            for (BigInteger key2 : map2.keySet()) {
                BigInteger zhi = key1.add(key2);
                BigInteger xi = map1.get(key1).multiply(map2.get(key2));
                if (res.containsKey(zhi)) {
                    res.put(zhi, res.get(zhi).add(xi));
                } else {
                    res.put(zhi, xi);
                }
            }
        }
        return res;
    }
}
