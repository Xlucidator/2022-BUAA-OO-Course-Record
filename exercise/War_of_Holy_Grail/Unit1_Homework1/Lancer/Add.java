import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Add {
    public HashMap<Integer, BigInteger> cal(
            HashMap<Integer, BigInteger> h1,
            HashMap<Integer, BigInteger> h2) {

        HashMap<Integer, BigInteger> h3 = new HashMap<Integer, BigInteger>();
        for (Map.Entry<Integer, BigInteger> entry1 : h1.entrySet()) {

            for (Map.Entry<Integer, BigInteger> entry2 : h2.entrySet()) {
                if (entry2.getKey() == entry1.getKey()) {
                    h3.put(entry2.getKey(), entry1.getValue().add(entry2.getValue()));
                }
            }
        }
        return h3;
    }
}
