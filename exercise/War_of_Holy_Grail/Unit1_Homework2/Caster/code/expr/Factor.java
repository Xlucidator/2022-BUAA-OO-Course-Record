package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

//需要修改的地方： HashMap
public abstract class Factor {
    //poly 该成分的最简多项式 isPos该成分的符号，默认为正
    private HashMap<HashMap<Factor, BigInteger>, BigInteger> poly;
    private boolean isPos = true;
    
    public boolean isPos() {
        return isPos;
    }
    
    public void changePos() {
        this.isPos = !isPos;
    }
    
    public HashMap<HashMap<Factor, BigInteger>, BigInteger> getPoly() {
        return poly;
    }
    
    public void setPoly(HashMap<HashMap<Factor, BigInteger>, BigInteger> poly) {
        this.poly = poly;
    }
    
    public abstract void update();
    
    //static add方法
    public static HashMap<HashMap<Factor, BigInteger>, BigInteger> add(
            HashMap<HashMap<Factor, BigInteger>, BigInteger> poly1,
            HashMap<HashMap<Factor, BigInteger>, BigInteger> poly2) {
        HashMap<HashMap<Factor, BigInteger>, BigInteger> ans = new HashMap<>(poly1);
        for (HashMap<Factor, BigInteger> key : poly2.keySet()) {
            if (!ans.containsKey(key)) {
                ans.put(key, poly2.get(key));
            } else {
                ans.replace(key, ans.get(key), ans.get(key).add(poly2.get(key)));
            }
        }
        return ans;
    }
    
    //static multi方法
    public static HashMap<HashMap<Factor, BigInteger>, BigInteger> multi(
            HashMap<HashMap<Factor, BigInteger>, BigInteger> poly1,
            HashMap<HashMap<Factor, BigInteger>, BigInteger> poly2) {
        HashMap<HashMap<Factor, BigInteger>, BigInteger> ans = new HashMap<>();
        for (HashMap<Factor, BigInteger> key1 : poly1.keySet()) {
            for (HashMap<Factor, BigInteger> key2 : poly2.keySet()) {
                HashMap<Factor, BigInteger> tmpKey = new HashMap<>(key1);
                for (Factor factor : key2.keySet()) {
                    BigInteger ref = key2.get(factor);
                    if (!tmpKey.containsKey(factor)) {
                        tmpKey.put(factor, ref);
                    } else {
                        tmpKey.replace(factor, tmpKey.get(factor), tmpKey.get(factor).add(ref));
                    }
                }
                BigInteger tmpValue = poly1.get(key1).multiply(poly2.get(key2));
                if (!ans.containsKey(tmpKey)) {
                    ans.put(tmpKey, tmpValue);
                } else {
                    ans.replace(tmpKey, ans.get(tmpKey), ans.get(tmpKey).add(tmpValue));
                }
            }
        }
        return ans;
    }
    
    public abstract Factor replace(ArrayList<Factor> parameters,
                                   ArrayList<Factor> actualParameters);
    
    @Override
    public String toString() {
        return "";
    }
    
    public abstract Factor clone();
}
