import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;

public class Key {
    public BigInteger getIndex() {
        return index;
    }
    
    private final BigInteger index;//x指数
    
    private final HashSet<Tri> tris;//三角函数
    
    public Key(BigInteger index, HashSet<Tri> tris) {
        this.index = index;
        this.tris = tris;
    }
    
    public boolean comparable(Key other) {
        String str1;
        String str2;
        if (this.same() && other.same() && this.index.equals(other.getIndex())) {
            str1 = this.getStr();
            str2 = other.getStr();
            if (str1.isEmpty() || str2.isEmpty()) {
                return true;//空string可比
            }
            return str1.equals(str2);
        }
        return false;
    }
    
    public Factor getFactor() {
        Factor factor = null;//指针
        for (Tri tri : this.tris) {
            factor = tri.getFactor();//
            break;
        }
        return factor;
    }
    
    public String getStr() {
        String str = "";
        for (Tri tri : this.tris) {
            str = tri.getStr();//
            break;
        }
        return str;
    }
    
    public boolean same() {
        String str = "";
        //判断三角函数内部完全相同,内部为因子,指针？只做比较时，引用即可，此处应测试
        if (tris.isEmpty()) {
            return true;//常数等满足条件
        }
        for (Tri tri : tris) {
            if (!str.isEmpty() && !tri.getStr().equals(str)) {
                return false;
            } else {
                str = tri.getStr();
            }
        }
        return true;
    }
    
    public BigInteger getIndex1() {
        for (Tri tri : tris) {
            if (tri.getType() == 1) {
                return tri.getIndex();//
            }
        }
        return BigInteger.ZERO;//
    }
    
    public BigInteger getIndex0() {
        for (Tri tri : tris) {
            if (tri.getType() == 0) {
                return tri.getIndex();//
            }
        }
        return BigInteger.ZERO;//
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
        return Objects.equals(index, key.index) && Objects.equals(tris, key.tris);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(index, tris);
    }
    
    public Key add(Key other) {
        BigInteger newIndex = this.index.add(other.index);
        HashSet<Tri> subTris = new HashSet<>();
        HashSet<Tri> set = new HashSet<>();
        if (this.tris.isEmpty()) {
            subTris.addAll(other.tris);
        } else if (other.tris.isEmpty()) {
            subTris.addAll(this.tris);
        } else {
            for (Tri tri1 : this.tris) {
                for (Tri tri2 : other.tris) {
                    if (tri1.getStr().equals(tri2.getStr())
                            && tri1.getType() == tri2.getType()) {
                        Tri temp = new Tri(tri1.getType(),
                                tri1.getIndex().add(tri2.getIndex()),
                                tri1.getFactor(), tri1.getStr());
                        subTris.add(temp);
                        set.add(tri1);
                        set.add(tri2);
                    }
                }
            }
            subTris.addAll(this.tris);
            subTris.addAll(other.tris);
            for (Tri tri : set) {
                subTris.remove(tri);
            }
        }
        return new Key(newIndex, subTris);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!(this.tris.isEmpty())) {
            for (Tri tri : this.tris) {
                String triStr = tri.toString();
                if (!sb.toString().isEmpty()) {
                    sb.append("*");
                }
                sb.append(triStr);
            }
        }
        if (index.equals(BigInteger.ZERO)) {
            return sb.toString();//零次方
        }
        if (!sb.toString().isEmpty()) {
            sb.append("*");
        }
        if (index.equals(BigInteger.ONE)) {
            sb.append("x");
        } else if (index.equals(new BigInteger("2"))) {
            sb.append("x*x");
        } else {
            sb.append("x**").append(index);
        }
        return sb.toString();
    }
}
