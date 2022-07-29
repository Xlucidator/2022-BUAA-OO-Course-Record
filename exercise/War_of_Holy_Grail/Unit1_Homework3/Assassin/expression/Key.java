package expression;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

public class Key {
    private BigInteger exponent;    // exponent of x
    private HashSet<Tri> tris = new HashSet<>();      // multi of tris
    
    public Key(BigInteger exponent) {
        this.exponent = exponent;
    }

    public Key(BigInteger exponent, Tri tri) {
        this.exponent = exponent;
        tris.add(tri);
    }

    public Key(BigInteger exponent, HashSet<Tri> tris) {
        this.exponent = exponent;
        this.setTris(tris);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Key key = (Key) o;
        return Objects.equals(exponent, key.exponent) && Objects.equals(tris, key.tris);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(exponent, tris);
    }

    public void addTri(Tri tri) {
        this.tris.add(tri.clone());
    }
    
    public Key addKey(Key other) {
        Key anskey = new Key(this.exponent.add(other.exponent));
        HashSet<Tri> toRemove = new HashSet<>();
        HashSet<Tri> toAdd = new HashSet<>();
        for (Tri i : this.tris) {
            anskey.tris.add(i.clone());
        }
        for (Tri i : other.tris) {
            boolean flag = true;
            for (Tri j : anskey.tris) {
                if (i.getName().equals(j.getName()) && i.getExpr().equals(j.getExpr()) && flag) {
                    flag = false;
                    Tri both = new Tri(i.getName(), i.getExpr());
                    both.setSign(i.getSign() * j.getSign());
                    both.setExponent(i.getExponent().add(j.getExponent()));
                    toRemove.add(j);
                    toAdd.add(both);
                }
            }
            for (Tri j : toRemove) {
                anskey.tris.remove(j);
            }
            toRemove.clear();
            for (Tri j : toAdd) {
                anskey.tris.add(j);
            }
            toAdd.clear();
            if (flag) {
                anskey.tris.add(i);
            }
        }
        return anskey;
    }
    
    public BigInteger getExponent() {
        return exponent;
    }
    
    public void setTris(HashSet<Tri> tris) {
        this.tris = tris;
    }
    
    public HashSet<Tri> getTris() {
        return tris;
    }

    public Key clone() {
        Key newKey = new Key(new BigInteger(this.exponent.toString()));
        for (Tri tri : this.tris) {
            newKey.addTri(tri);
        }
        return newKey;
    }
    
    public Key canMerge(Key other) {
        Iterator<Tri> i = this.tris.iterator();
        while (i.hasNext()) {
            Tri a = i.next();
            if (a.getExponent().compareTo(new BigInteger("2")) >= 0) {
                Iterator<Tri> j = other.tris.iterator();
                while (j.hasNext()) {
                    Tri b = j.next();
                    if (b.getExponent().compareTo(new BigInteger("2")) >= 0 && /* ab 的指数大于2 */
                            !a.getName().equals(b.getName()) && /* 两个名字不一样 */
                            a.getExpr().equals(b.getExpr())) {  /* 三角函数内容相同 */
                        HashSet<Tri> trisA = new HashSet<>();
                        HashSet<Tri> trisB = new HashSet<>();
                        Tri simplyA = new Tri(a.getName(), a.getExpr().clone());
                        simplyA.setExponent(a.getExponent().add(new BigInteger("-2")));
                        Tri simplyB = new Tri(b.getName(), b.getExpr().clone());
                        simplyB.setExponent(b.getExponent().add(new BigInteger("-2")));
                        for (Tri tri : this.tris) {
                            if (!tri.equals(a)) {
                                trisA.add(tri.clone());
                            } else {
                                if (!simplyA.getExponent().equals(BigInteger.ZERO)) {
                                    trisA.add(simplyA);
                                }
                            }
                        }
                        for (Tri tri : other.tris) {
                            if (!tri.equals(b)) {
                                trisB.add(tri);
                            } else {
                                if (!simplyB.getExponent().equals(BigInteger.ZERO)) {
                                    trisB.add(simplyB);
                                }
                            }
                        }
                        if (hsEquals(trisA, trisB)) {
                            Key newKey = new Key(this.exponent.add(BigInteger.ZERO), trisA);
                            return newKey;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public boolean hsEquals(HashSet<Tri> a, HashSet<Tri> b) {
        boolean ans = false;
        if (a.size() == b.size()) {
            ans = true;
            for (Tri i : a) {
                if (!b.contains(i)) {
                    ans = false;
                }
            }
            for (Tri j : b) {
                if (!a.contains(j)) {
                    ans = false;
                }
            }
        }
        return ans;
    }
}
