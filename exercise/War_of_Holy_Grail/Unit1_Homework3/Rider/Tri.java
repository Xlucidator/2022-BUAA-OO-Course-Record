import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tri implements Factor {
    private final int type; //1-sin; 0-cos
    
    public BigInteger getIndex() {
        return index;
    }
    
    public int getType() {
        return type;
    }
    
    public Factor getFactor() {
        return factor;
    }
    
    private final BigInteger index; //指数
    
    private final Factor factor;
    
    private final String factorStr;
    
    public String getStr() {
        return factorStr;
    }
    
    public Tri(int type, BigInteger index, Factor factor, String factorStr) {
        this.type = type;
        this.index = index;
        this.factor = factor;
        this.factorStr = factorStr;
    }
    
    public Factor replace(ArrayList<Factor> formPars, ArrayList<Factor> parameters) {
        Factor ref;
        ref = this.factor.replace(formPars, parameters);
        String factorStr = ref.toTriPoly().toString();
        return new Tri(this.type, this.index, ref, factorStr);
    }
    
    public Tri clone() {
        return new Tri(this.type, this.index, this.factor.clone(), this.factorStr);
    }
    
    public boolean isPre() {
        return factor.isPre();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tri tri = (Tri) o;
        return type == tri.type && Objects.equals(index, tri.index)
                && factorStr.equals(tri.factorStr);//改成字符串相等
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, index, factorStr);
    }
    
    public TriPoly toTriPoly() {
        HashSet<Tri> set = new HashSet<>();
        set.add(this);
        return new TriPoly(new Key(BigInteger.ZERO, set), BigInteger.ONE);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String factStr = factorStr;
        if (type == 1) {
            sb.append("sin");
        } else if (type == 0) {
            sb.append("cos");
        }
        sb.append("(");
        if (isExpr(factStr)) {
            String temp = '(' + factStr + ')';
            temp = temp.equals("(x*x)") ? "x**2" : temp;
            sb.append(temp);
        } else {
            sb.append(factStr);
        }
        sb.append(")");
        if (!index.equals(BigInteger.ONE)) {
            sb.append("**").append(index);
        }
        return sb.toString();
    }
    
    public boolean isExpr(String str) {
        Pattern number = Pattern.compile("-?\\d*");
        Pattern power = Pattern.compile("x(\\*\\*\\d*)?");
        Matcher n = number.matcher(str);
        Matcher p = power.matcher(str);
        if (n.matches()) {
            return false;
        } else if (p.matches()) {
            return false;
        } else {
            Pattern tri = Pattern.compile("(sin|cos)\\(");
            Matcher t = tri.matcher(str);
            boolean flag = true;
            int index = -1;
            while (t.find() && flag) {
                flag = false;
                if (t.start() != 0) {
                    break;
                }
                int left = 0;
                int right = 0;
                for (index = t.end(); index < str.length(); index++) {
                    if (str.charAt(index) == '(') {
                        left++;
                    } else if (str.charAt(index) == ')') {
                        right++;
                    }
                    if (right > left) {
                        break;
                    }
                }
            }
            String sub = str.substring(index + 1);
            Pattern exp = Pattern.compile("\\*\\*\\d*");
            Matcher e = exp.matcher(sub);
            return !sub.isEmpty() && !e.matches();
        }
    }
}
