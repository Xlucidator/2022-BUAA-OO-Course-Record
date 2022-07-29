package operation;

import expression.Key;

import java.math.BigInteger;
import java.util.HashMap;
//xiimport java.util.Iterator;

public class Multi {
    public static HashMap<Key, BigInteger> multi(HashMap<Key, BigInteger> a,
                                                 HashMap<Key, BigInteger> b) {
        HashMap<Key, BigInteger> finalans = new HashMap<>();
        for (Key i : a.keySet()) {
            for (Key j : b.keySet()) {
                BigInteger exponent1 = a.get(i);
                BigInteger exponent2 = b.get(j);
                Key iaddj = i.addKey(j);
                BigInteger all = finalans.getOrDefault(iaddj, BigInteger.ZERO);
                all = all.add(exponent1.multiply(exponent2));
                finalans.put(iaddj, all);
            }
        }
        //        Iterator<Key> i = a.keySet().iterator();
        //        while (i.hasNext()) {
        //            Iterator<Key> j = b.keySet().iterator();
        //            Key ai = i.next().clone();
        //            while (j.hasNext()) {
        //                Key bj = j.next().clone();
        //                BigInteger exponent1 = a.get(ai);
        //                BigInteger exponent2 = b.get(bj);
        //                Key iaddj = ai.addKey(bj);
        //                BigInteger all = finalans.getOrDefault(iaddj, BigInteger.ZERO);
        //                all = all.add(exponent1.multiply(exponent2));
        //                finalans.put(iaddj, all);
        //            }
        //        }
        return finalans;
    }
}
