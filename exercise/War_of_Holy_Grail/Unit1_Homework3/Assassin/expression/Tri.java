package expression;

import deconstruct.Lexer;
import deconstruct.Parser;
import operation.Multi;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;

public class Tri extends BasicClass implements Factor {
    private int sign = 1;
    private BigInteger exponent = BigInteger.ONE;
    private String name;
    private Expr expr;
    private HashMap<Key, BigInteger> ans = new HashMap<>();
    private boolean ansAlready = false;
    
    public Tri(String name, Expr expr) {
        this.expr = expr;
        this.name = name;
    }

    @Override
    public void setSign(int sign) {
        this.sign = sign;
    }
    
    @Override
    public void setExponent(BigInteger exponent) {
        this.exponent = exponent;
    }
    
    public void setExpr(Expr expr) {
        this.expr = expr;
    }
    
    @Override
    public HashMap<Key, BigInteger> getAns() {
        BigInteger bigSign = new BigInteger(String.valueOf(this.sign));
        bigSign = bigSign.multiply(BigInteger.ONE);
        if (ansAlready) {
            return ans;
        }
        if (this.getExponent().equals(BigInteger.ZERO)) {
            ansAlready = true;
            Key key = new Key(BigInteger.ZERO);
            ans.put(key, BigInteger.ONE);
            return ans;
        }
        if (this.expr.toString().equals("0")) {
            ansAlready = true;
            if (this.name.equals("sin")) {
                Key key = new Key(BigInteger.ZERO);
                ans.put(key, BigInteger.ZERO);
            } else if (this.name.equals("cos")) {
                Key key = new Key(BigInteger.ZERO);
                ans.put(key, BigInteger.ONE);
            }
            return ans;
        }
        //
        //Key key = new Key(BigInteger.ZERO, )
        checkInsideExpr();
        
        Tri tri = new Tri(this.name, this.expr.clone());
        tri.expr.setAnsAlready(false);
        Key key = new Key(BigInteger.ZERO);
        HashMap<Key, BigInteger> midAns = new HashMap<>();
        if (tri.expr.getSign() == -1) {
            if (tri.name.equals("sin")) {
                tri.expr.setSign(1);
                tri.expr.setAnsAlready(false);
                key.addTri(tri);
                midAns.put(key, new BigInteger("-1"));
            } else {
                tri.expr.setSign(1);
                tri.expr.setAnsAlready(false);
                key.addTri(tri);
                midAns.put(key, BigInteger.ONE);
            }
        } else {
            key.addTri(tri);
            midAns.put(key, BigInteger.ONE);
        }
        
        if (!this.getExponent().equals(BigInteger.ONE)) {
            HashMap<Key, BigInteger> basic = new HashMap<>();
            for (Key i : midAns.keySet()) {
                basic.put(i.clone(), new BigInteger(midAns.get(i).toString()));
            }
            for (BigInteger i = BigInteger.ONE; i.compareTo(this.getExponent()) < 0;
                 i = i.add(BigInteger.ONE)) {
                midAns = Multi.multi(midAns, basic);
            }
        }
        //
        for (Key key1 : midAns.keySet()) {
            ans.put(key1, midAns.get(key1));
        }
        //Key key = new Key(BigInteger.ZERO, this);
        //ans.put(key, bigSign);
        ansAlready = true;
        return ans;
    }
    
    public void checkInsideExpr() {
        if (this.expr.toString().charAt(0) == '-') {
            this.expr.reverse();
            Lexer lexer = new Lexer(this.expr.toString());
            Parser parser = new Parser(lexer);
            Expr nexpr = parser.parseExpr();
            nexpr.setSign(-1);
            this.setExpr(nexpr.clone());
        }
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public Expr getExpr() {
        return expr;
    }
    
    @Override
    public BigInteger getExponent() {
        return exponent;
    }
    
    @Override
    public int getSign() {
        return sign;
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
        return Objects.equals(exponent, tri.exponent) &&
                Objects.equals(name, tri.name) &&
                Objects.equals(expr, tri.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exponent, name, expr);
    }
    
    @Override
    public Tri clone() {
        Tri tri = new Tri(this.name.substring(0), this.expr.clone());
        tri.setSign(this.getSign());
        tri.setExponent(this.getExponent().add(BigInteger.ZERO));
        return tri;
    }
}
