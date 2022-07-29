import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public HashMap<Integer, BigInteger> hi(String a, ArrayList<Express> exprs) {
        Pattern pattern = Pattern.compile("f");
        Matcher matcher = pattern.matcher(a);
        boolean m1 = matcher.find();
        if (m1) {
            //是一个标签量
            for (Express item : exprs) {
                if (item.getFn().equals(a)) {
                    return item.getHashmap();
                }
            }
        } else {
            Pattern pattern2 = Pattern.compile("x");
            Matcher matcher2 = pattern2.matcher(a);
            boolean m2 = matcher2.find();
            if (m2) {
                HashMap<Integer, BigInteger> hashMap1 = new HashMap<Integer, BigInteger>();
                Pattern pattern3 = Pattern.compile("-");
                Matcher matcher3 = pattern3.matcher(a);
                boolean m3 = matcher3.find();
                if (m3) {
                    hashMap1.put(1, BigInteger.valueOf(-1));
                } else {
                    hashMap1.put(1, BigInteger.valueOf(1));
                }

                hashMap1.put(0, BigInteger.valueOf(0));
                hashMap1.put(2, BigInteger.valueOf(0));
                hashMap1.put(3, BigInteger.valueOf(0));
                hashMap1.put(4, BigInteger.valueOf(0));
                hashMap1.put(5, BigInteger.valueOf(0));
                hashMap1.put(6, BigInteger.valueOf(0));
                hashMap1.put(7, BigInteger.valueOf(0));
                hashMap1.put(8, BigInteger.valueOf(0));
                return hashMap1;
            } else {
                HashMap<Integer, BigInteger> hashMap2 = new HashMap<Integer, BigInteger>();
                hashMap2.put(0, new BigInteger(a));
                hashMap2.put(1, BigInteger.valueOf(0));
                hashMap2.put(2, BigInteger.valueOf(0));
                hashMap2.put(3, BigInteger.valueOf(0));
                hashMap2.put(4, BigInteger.valueOf(0));
                hashMap2.put(5, BigInteger.valueOf(0));
                hashMap2.put(6, BigInteger.valueOf(0));
                hashMap2.put(7, BigInteger.valueOf(0));
                hashMap2.put(8, BigInteger.valueOf(0));
                return hashMap2;
            }
        }
        return new HashMap<Integer, BigInteger>();
    }
}
