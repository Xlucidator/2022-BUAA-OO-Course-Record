import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Mul {
    public HashMap<Integer, BigInteger> cal(
            HashMap<Integer, BigInteger> h1,
            HashMap<Integer, BigInteger> h2) {

        HashMap<Integer, BigInteger> h3 = new HashMap<Integer, BigInteger>();
        h3.put(0, BigInteger.valueOf(0));
        h3.put(1, BigInteger.valueOf(0));
        h3.put(2, BigInteger.valueOf(0));
        h3.put(3, BigInteger.valueOf(0));
        h3.put(4, BigInteger.valueOf(0));
        h3.put(5, BigInteger.valueOf(0));
        h3.put(6, BigInteger.valueOf(0));
        h3.put(7, BigInteger.valueOf(0));
        h3.put(8, BigInteger.valueOf(0));


        for (Map.Entry<Integer, BigInteger> entry1 : h1.entrySet()) {
            for (Map.Entry<Integer, BigInteger> entry2 : h2.entrySet()) {
                int key = entry1.getKey() + entry2.getKey();
                BigInteger value = entry1.getValue().multiply(entry2.getValue());
                for (Map.Entry<Integer, BigInteger> entry3 : h3.entrySet()) {
                    if (entry3.getKey() == key) {
                        entry3.setValue(entry3.getValue().add(value));
                    }
                }
            }
        }
        return h3;
    }
}
