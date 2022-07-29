import java.math.BigInteger;
import java.util.ArrayList;
import java.util.TreeMap;

public class Expr implements Factor {
    private ArrayList<Term> terms = new ArrayList<>();
    private BigInteger index = BigInteger.valueOf(1);
    private TreeMap<BigInteger, BigInteger> expr;

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    @Override
    public TreeMap<BigInteger, BigInteger> simplify() {
        TreeMap<BigInteger, BigInteger> res = new TreeMap<>();
        if (index.compareTo(BigInteger.valueOf(0)) == 0) {
            res.put(BigInteger.valueOf(0), BigInteger.valueOf(1));
            return res;
        }
        TreeMap<BigInteger, BigInteger> tmp;
        for (Term term : terms) {
            tmp = term.simplify();
            for (BigInteger key : tmp.keySet()) {
                if (res.containsKey(key)) {
                    BigInteger ori = res.get(key);
                    res.put(key, ori.add(tmp.get(key)));
                } else {
                    res.put(key, tmp.get(key));
                }
            }
        }
        TreeMap<BigInteger, BigInteger> oriRes = res;
        for (BigInteger i = BigInteger.valueOf(1); 
            i.compareTo(index) < 0; i = i.add(BigInteger.valueOf(1))) {
            res = multMap(res, oriRes);
        }
        return res;
    }

    public void setIndex(BigInteger index) {
        this.index = index;
    }

    public void anounce() {
        System.out.println("expr is loaded");
    }

    public TreeMap<BigInteger, BigInteger> getExpr() {
        return expr;
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
