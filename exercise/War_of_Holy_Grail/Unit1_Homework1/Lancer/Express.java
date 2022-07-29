import java.math.BigInteger;
import java.util.HashMap;

public class Express {
    private String fn;
    private String order;
    private HashMap<Integer, BigInteger> hashmap;

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public HashMap<Integer, BigInteger> getHashmap() {
        return hashmap;
    }

    public void setHashmap(HashMap<Integer, BigInteger> hashmap) {
        this.hashmap = hashmap;
    }

    public Express(String fn, String order, HashMap<Integer, BigInteger> hashmap) {
        this.setFn(fn);
        this.setOrder(order);
        this.setHashmap(hashmap);
    }
}
