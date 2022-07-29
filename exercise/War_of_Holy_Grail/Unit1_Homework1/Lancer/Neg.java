import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Neg {
    public HashMap<Integer, BigInteger> cal(HashMap<Integer, BigInteger> hashMap) {
        for (Map.Entry<Integer, BigInteger> entry : hashMap.entrySet()) {
            entry.setValue(entry.getValue().multiply(BigInteger.valueOf(-1)));
        }
        return hashMap;
    }
}
