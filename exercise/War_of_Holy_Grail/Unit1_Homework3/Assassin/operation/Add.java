package operation;

import expression.Key;

import java.math.BigInteger;
import java.util.HashMap;

public class Add {
    public static HashMap<Key, BigInteger> add(HashMap<Key, BigInteger> a,
                                               HashMap<Key, BigInteger> b) {
        HashMap<Key, BigInteger> finalans = new HashMap<>();
        for (Key i : a.keySet()) {
            BigInteger all = finalans.getOrDefault(i, BigInteger.ZERO);
            all = all.add(a.get(i));
            finalans.put(i, all);
        }
        for (Key i : b.keySet()) {
            BigInteger all = finalans.getOrDefault(i, BigInteger.ZERO);
            all = all.add(b.get(i));
            finalans.put(i, all);
        }
        return finalans;
    }
    
}
