import java.math.BigInteger;
import java.util.HashMap;

public class Pow {
    public HashMap<Integer, BigInteger> cal(
            HashMap<Integer, BigInteger> h1,
            int zhishu) {
        if (zhishu == 0) {
            HashMap<Integer, BigInteger> h3 = new HashMap<Integer, BigInteger>();
            h3.put(0, BigInteger.valueOf(1));
            h3.put(1, BigInteger.valueOf(0));
            h3.put(2, BigInteger.valueOf(0));
            h3.put(3, BigInteger.valueOf(0));
            h3.put(4, BigInteger.valueOf(0));
            h3.put(5, BigInteger.valueOf(0));
            h3.put(6, BigInteger.valueOf(0));
            h3.put(7, BigInteger.valueOf(0));
            h3.put(8, BigInteger.valueOf(0));
            return h3;
        }

        //h3 wei res
        else {
            HashMap<Integer, BigInteger> h3 = (HashMap<Integer, BigInteger>) h1.clone();
            for (int i = 0; i < zhishu - 1; i++) {
                Mul mul = new Mul();
                h3 = (HashMap<Integer, BigInteger>) mul.cal(h3, h1).clone();
            }
            return h3;
        }
    }
}
