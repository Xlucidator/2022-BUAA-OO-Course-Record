import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TriPoly {
    public HashMap<Key, BigInteger> getTriPoly() {
        return triPoly;
    }
    
    private final HashMap<Key, BigInteger> triPoly;
    
    public TriPoly(Key key, BigInteger cof) {
        this.triPoly = new HashMap<>();
        this.triPoly.put(key, cof);
    }
    
    public TriPoly(HashMap<Key, BigInteger> triPoly) {
        this.triPoly = triPoly;
    }
    
    public TriPoly opposite() {
        HashMap<Key, BigInteger> sbMap = new HashMap<>();
        for (Key key : this.triPoly.keySet()) {
            sbMap.put(key, this.triPoly.get(key).negate());
        }
        return new TriPoly(sbMap);
    }
    
    public TriPoly mul(TriPoly other) {
        HashMap<Key, BigInteger> sbMap = new HashMap<>();
        for (Key key1 : this.getTriPoly().keySet()) {
            for (Key key2 : other.getTriPoly().keySet()) {
                BigInteger cof = this.triPoly.get(key1).multiply(other.triPoly.get(key2));
                if (cof.equals(BigInteger.ZERO)) {
                    continue;
                }
                Key tempKey = key1.add(key2);
                if (sbMap.containsKey(tempKey)) {
                    cof = sbMap.get(tempKey).add(cof);
                    sbMap.put(tempKey, cof);
                } else {
                    sbMap.put(tempKey, cof);
                }
            }
        }
        return new TriPoly(sbMap);
    }
    
    public TriPoly add(TriPoly other) {
        HashMap<Key, BigInteger> sbMap = new HashMap<>();
        Set<Key> set = new HashSet<>();
        set.addAll(this.triPoly.keySet());
        set.addAll(other.triPoly.keySet());
        for (Key i : set) {
            if (this.getTriPoly().containsKey(i) && other.getTriPoly().containsKey(i)) {
                sbMap.put(i, this.getTriPoly().get(i).add(other.getTriPoly().get(i)));
            } else if (this.getTriPoly().containsKey(i)) {
                sbMap.put(i, this.getTriPoly().get(i));
            } else if (other.getTriPoly().containsKey(i)) {
                sbMap.put(i, other.getTriPoly().get(i));
            }
        }
        TriPoly triPoly = new TriPoly(sbMap);
        HashMap<Key, BigInteger> subMap = new HashMap<>(sbMap);
        TriPoly subTriPoly = new TriPoly(subMap);
        triPoly.merge();
        return (triPoly.toString().length() > subTriPoly.toString().length()) ?
                subTriPoly : triPoly;
    }
    
    public TriPoly sub(TriPoly other) {
        HashMap<Key, BigInteger> sbMap = new HashMap<>();
        Set<Key> set = new HashSet<>();
        set.addAll(this.getTriPoly().keySet());
        set.addAll(other.getTriPoly().keySet());
        for (Key i : set) {
            if (this.getTriPoly().containsKey(i) && other.getTriPoly().containsKey(i)) {
                sbMap.put(i, this.getTriPoly().get(i).subtract(other.getTriPoly().get(i)));
            } else if (this.getTriPoly().containsKey(i)) {
                sbMap.put(i, this.getTriPoly().get(i));
            } else if (other.getTriPoly().containsKey(i)) {
                sbMap.put(i, other.getTriPoly().get(i).negate());
            }
        }
        TriPoly triPoly = new TriPoly(sbMap);
        HashMap<Key, BigInteger> subMap = new HashMap<>(sbMap);
        TriPoly subTriPoly = new TriPoly(subMap);
        triPoly.merge();
        return (triPoly.toString().length() > subTriPoly.toString().length()) ?
                subTriPoly : triPoly;
    }
    
    public void merge() {
        boolean flag = true;
        while (flag) {
            ArrayList<Key> mergeMap = new ArrayList<>(this.triPoly.keySet());
            int len = mergeMap.size();
            flag = false;
            for (int i1 = 0; i1 < len; i1++) {
                for (int i2 = i1 + 1; i2 < len; i2++) {
                    Key key1 = mergeMap.get(i1);
                    Key key2 = mergeMap.get(i2);//系数有零没必要合并
                    if (this.triPoly.get(key1).equals(BigInteger.ZERO)
                            || this.triPoly.get(key2).equals(BigInteger.ZERO)) {
                        continue;
                    }
                    if (firstMerge(key1, key2)) {
                        flag = true;
                        break;
                    } else if (secMerge(key1, key2)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
        }
    }
    
    public boolean firstMerge(Key formKey1, Key formKey2) {
        Key key1 = formKey1;
        Key key2 = formKey2;
        if (key1.comparable(key2)) {
            int relation = this.triPoly.get(key1).compareTo(this.triPoly.get(key2));
            if (relation > 0) {
                Key temp = key1;
                key1 = key2;
                key2 = temp;
            }
            BigInteger s1 = key1.getIndex1();
            BigInteger c1 = key1.getIndex0();
            BigInteger s2 = key2.getIndex1();
            BigInteger c2 = key2.getIndex0();
            BigInteger num2 = new BigInteger("2");
            if ((s1.subtract(s2).equals(num2) && c2.subtract(c1).equals(num2)
                    || s2.subtract(s1).equals(num2) && c1.subtract(c2).equals(num2))) {
                HashSet<Tri> triSet = new HashSet<>();
                Factor factor = key1.getFactor().clone();//克隆？
                String factorStr = factor.toTriPoly().toString();
                BigInteger minS = (s1.compareTo(s2) < 0) ? s1 : s2;
                BigInteger minC = (c1.compareTo(c2) < 0) ? c1 : c2;
                if (!minS.equals(BigInteger.ZERO)) {
                    Tri triS = new Tri(1, minS, factor, factorStr);
                    triSet.add(triS);
                }
                if (!minC.equals(BigInteger.ZERO)) {
                    Tri triC = new Tri(0, minC, factor, factorStr);
                    triSet.add(triC);
                }
                Key key = new Key(key1.getIndex(), triSet);
                if (this.getTriPoly().containsKey(key)) {
                    this.triPoly.put(key, this.getTriPoly().get(key)
                            .add(this.getTriPoly().get(key1)));
                } else {
                    this.triPoly.put(key, this.getTriPoly().get(key1));
                }
                this.triPoly.put(key2, this.triPoly.get(key2)
                        .subtract(this.triPoly.get(key1)));
                this.triPoly.remove(key1);
                return true;
            }
        }
        return false;
    }
    
    public boolean secMerge(Key formKey1, Key formKey2) {
        Key key1 = formKey1;
        Key key2 = formKey2;
        if (key1.comparable(key2) && (this.triPoly.get(key1)
                .add(this.triPoly.get(key2))).equals(BigInteger.ZERO)) {
            BigInteger s1 = key1.getIndex1();
            BigInteger c1 = key1.getIndex0();
            BigInteger s2 = key2.getIndex1();
            BigInteger c2 = key2.getIndex0();
            BigInteger num2 = new BigInteger("2");
            BigInteger num0 = BigInteger.ZERO;
            BigInteger sub1 = s1.subtract(s2).abs();
            BigInteger sub2 = c1.subtract(c2).abs();
            int type = (sub1.equals(num2) && sub2.equals(num0)) ? 1
                    : (sub2.equals(num2) && sub1.equals(num0)) ? 0 : -1;
            int chooseKey;
            int chooseTri;
            if (type != -1) {
                chooseTri = (sub1.equals(num2)) ? 0 : 1;
                chooseKey = (chooseTri == 0) ? ((s1.subtract(s2).compareTo(num0) > 0) ? 2 : 1)
                        : ((c1.subtract(c2).compareTo(num0) > 0) ? 2 : 1);
                HashSet<Tri> triSet = new HashSet<>();
                if (chooseKey == 2) {
                    Key temp = key1;
                    key1 = key2;
                    key2 = temp;
                }
                Factor factor = key2.getFactor().clone();//key1可能没有factor
                String factorStr = factor.toTriPoly().toString();
                //key1要改变，且为乘cos2或乘sin2
                Tri tri = new Tri(chooseTri, num2, factor, factorStr);
                triSet.add(tri);
                Key key = new Key(BigInteger.ZERO, triSet);
                Key newKey = key1.add(key);
                if (this.getTriPoly().containsKey(newKey)) {
                    this.triPoly.put(newKey, this.getTriPoly().get(newKey)
                            .add(this.getTriPoly().get(key1)));
                } else {
                    this.triPoly.put(newKey, this.getTriPoly().get(key1));
                }
                this.triPoly.remove(key1);
                this.triPoly.remove(key2);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        HashSet<Key> set = new HashSet<>(this.getTriPoly().keySet());
        for (Key key : set) {
            BigInteger cof = this.triPoly.get(key);
            if (cof.compareTo(BigInteger.ZERO) > 0) {
                sb.append(toTerm(cof, key.toString()));
                set.remove(key);
                break;
            }
        }
        for (Key key : set) {
            BigInteger cof = this.triPoly.get(key);
            String keyStr = key.toString();
            String term = toTerm(cof, keyStr);
            if (!sb.toString().isEmpty() && !term.isEmpty()) {
                sb.append("+");
            }
            sb.append(term);
        }
        String s = sb.toString().replaceAll("[+][-]", "-");
        return s.isEmpty() ? "0" : s;
    }
    
    public String toTerm(BigInteger cof, String keyStr) {
        StringBuilder sb = new StringBuilder();
        if (cof.equals(BigInteger.ZERO)) {
            return "";
        }
        if (keyStr.equals("0")) {
            return "";
        }
        if (cof.equals(BigInteger.ONE)) {
            if (keyStr.isEmpty()) {
                sb.append(cof);
            }
        } else if (cof.equals(new BigInteger("-1"))) {
            if (keyStr.isEmpty()) {
                sb.append(cof);
            } else {
                sb.append("-");
            }
        } else {
            sb.append(cof);
            if (!keyStr.isEmpty()) {
                sb.append("*");
            }
        }
        sb.append(keyStr);
        return sb.toString();
    }
}