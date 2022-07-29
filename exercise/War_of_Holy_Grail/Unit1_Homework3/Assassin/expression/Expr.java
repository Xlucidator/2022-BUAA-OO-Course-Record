package expression;

import operation.Add;
import operation.Multi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Objects;

public class Expr extends BasicClass implements Factor, Cloneable {
    private final ArrayList<Term> terms;
    private HashMap<Key, BigInteger> ans = new HashMap<>();
    private boolean ansAlready = false;
    
    public Expr() {
        terms = new ArrayList<>();
    }
    
    public void setAnsAlready(boolean ansAlready) {
        this.ansAlready = ansAlready;
    }
    
    public void addTerm(Term term) {
        terms.add(term);
    }
    
    public ArrayList<Term> getTerms() {
        return terms;
    }
    
    @Override
    public BigInteger getExponent() {
        return super.getExponent();
    }
    
    public HashMap<Key, BigInteger> getAns() {
        BigInteger bigSign = new BigInteger(String.valueOf(this.getSign()));
        if (ansAlready) {
            return this.ans;
        }
        if (this.getExponent().equals(BigInteger.ZERO)) {
            ans.put(new Key(BigInteger.ZERO), bigSign.multiply(BigInteger.ONE));
            ansAlready = true;
            return ans;
        }
        Iterator<Term> iter = terms.iterator();
        HashMap<Key, BigInteger> midAns = iter.next().getAns();
        while (iter.hasNext()) {
            midAns = Add.add(midAns, iter.next().getAns());
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
        for (Key i : midAns.keySet()) {
            ans.put(i, bigSign.multiply(midAns.get(i)));
        }
        HashSet<Key> toRemove = new HashSet<>();
        for (Key i : ans.keySet()) {
            if (ans.get(i).equals(BigInteger.ZERO)) {
                toRemove.add(i);
            }
        }
        for (Key i : toRemove) {
            ans.remove(i);
        }
        ansAlready = true;
        return ans;
    }
    
    @Override
    public String toString() {
        ans = getAns();
        StringBuilder sb = new StringBuilder();
        Key head = null;
        for (Key i : ans.keySet()) {
            if (ans.get(i).compareTo(BigInteger.ZERO) == 1) {
                head = i;
                sb.append(appendX(i));
                break;
            }
        }
        for (Key i : ans.keySet()) {
            if (i.equals(head)) {
                continue;
            }
            if (!ans.get(i).equals(BigInteger.ZERO)) {
                if (ans.get(i).compareTo(BigInteger.ZERO) == 1) {   // if the coeff > 0
                    sb.append("+");
                }
                sb.append(appendX(i));
            }
        }
        if (sb.toString().length() > 0) {
            return sb.toString();
        } else {
            return "0";
        }
    }
    
    public String appendX(Key i) {
        StringBuilder sb = new StringBuilder();
        // output coef
        if (ans.get(i).equals(new BigInteger("-1")) ||
                ans.get(i).equals(BigInteger.ONE)) { // if coef == 1 or -1
            if (i.getExponent().compareTo(BigInteger.ZERO) > 0 ||
                    (!i.getTris().isEmpty())) {  // if there have x or sin cos
                if (ans.get(i).equals(new BigInteger("-1"))) {  // if coef == -1
                    sb.append("-");
                }
            } else {    // if there is no x or sin cos
                sb.append(ans.get(i));
            }
        } else {    // if coef != 1 and -1
            sb.append(ans.get(i));
            if (i.getExponent().compareTo(BigInteger.ZERO) > 0 || (!i.getTris().isEmpty())) {
                sb.append("*");
            }
        }
        // output sin and cos
        Iterator<Tri> iter = i.getTris().iterator();
        while (iter.hasNext()) {
            Tri presentTri = iter.next();
            sb.append(presentTri.getName());
            sb.append("(");
            if (presentTri.getExpr().needBrackets()) {
                sb.append("(");
                sb.append(presentTri.getExpr().toString());
                sb.append(")");
            } else {
                sb.append(presentTri.getExpr().toString());
            }
            sb.append(")");
            if (presentTri.getExponent().compareTo(BigInteger.ONE) == 1) {
                sb.append("**");
                sb.append(presentTri.getExponent());
            }
            if (iter.hasNext()) {
                sb.append("*");
            }
        }
        if (!i.getTris().isEmpty() && i.getExponent().compareTo(BigInteger.ZERO) == 1) {
            sb.append("*");
        }
        // output x ** ...
        if (i.getExponent().compareTo(BigInteger.ZERO) == 1) {
            sb.append("x");
            if (i.getExponent().equals(new BigInteger("2"))) {
                sb.append("*x");
            } else if (i.getExponent().compareTo(new BigInteger("2")) == 1) {
                sb.append("**");
                sb.append(i.getExponent());
            }
        }
        return sb.toString();
    }
    
    public boolean needBrackets() {
        if (this.ans.keySet().size() > 1) {
            return true;
        }
        if (this.ans.keySet().size() == 0) {
            return false;
        }
        for (Key key : this.ans.keySet()) {
            if (key.getExponent().equals(BigInteger.ZERO)) {
                if (key.getTris().size() > 0) {
                    return !this.ans.get(key).equals(BigInteger.ONE) &&
                            !this.ans.get(key).equals(BigInteger.ZERO);
                } else {
                    return false;
                }
            } else {
                if (key.getTris().size() > 0) {
                    return true;
                } else {
                    return !this.ans.get(key).equals(BigInteger.ONE) &&
                            !this.ans.get(key).equals(BigInteger.ZERO);
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Expr expr = (Expr) o;
        expr.getAns();
        ((Expr) o).getAns();
        return Objects.equals(ans, expr.ans);
    }

    @Override
    public int hashCode() {
        getAns();
        return Objects.hash(super.hashCode(), ans);
    }
    
    public Expr replace(ArrayList<Factor> parameters, ArrayList<Factor> arguments) {
        Expr replacedExpr = new Expr();
        for (Term term : terms) {
            Term replacedTerm = term.replace(parameters, arguments);
            replacedTerm.setSign(term.getSign());
            replacedTerm.setExponent(term.getExponent());
            replacedExpr.addTerm(replacedTerm);
        }
        return replacedExpr;
    }
    
    @Override
    public Expr clone() {
        Expr expr = new Expr();
        for (Term term : terms) {
            expr.addTerm(term.clone());
        }
        expr.setSign(this.getSign());
        expr.setExponent(this.getExponent().add(BigInteger.ZERO));
        return expr;
    }
    
    public boolean simplify() {
        boolean simplified = false;
        if (ans.keySet().size() <= 1) { // 如果两项都没有则不化简
            return false;
        }
        Iterator<Key> i = ans.keySet().iterator();
        HashSet<Key> toMove = new HashSet<>();  // 将来要删除
        HashSet<Key> toAdd = new HashSet<>();   // 将来要增加
        Key mergedKey = null;
        BigInteger coefficient = BigInteger.ZERO;
        BigInteger aco = BigInteger.ZERO;
        BigInteger bco = BigInteger.ZERO;
        Key afinal = null;
        Key bfinal = null;
        while (i.hasNext() && !simplified) {
            Key a = i.next();
            Iterator<Key> j = ans.keySet().iterator();
            while (j.hasNext() && !simplified) {
                Key b = j.next();
                if (!a.equals(b) &&
                        ans.get(a).multiply(ans.get(b)).compareTo(BigInteger.ZERO) > 0) { // 系数符号相同
                    if (a.getExponent().equals(b.getExponent()) &&
                            a.getTris().size() * b.getTris().size() > 0) {  // 指数相同，三角函数大小相同
                        mergedKey = a.canMerge(b);
                        if (mergedKey != null) { // a b 可以合并
                            simplified = true;
                            toMove.add(a);
                            toMove.add(b);
                            afinal = a.clone();
                            bfinal = b.clone();
                            coefficient = getCoefficient(ans.get(a), ans.get(b));
                            aco = ans.get(a).add(coefficient.multiply(new BigInteger("-1")));
                            bco = ans.get(b).add(coefficient.multiply(new BigInteger("-1")));
                            break;
                        }
                    }
                }
            }
        }
        if (mergedKey != null) {
            if (aco.equals(BigInteger.ZERO)) {
                ans.remove(afinal);
            } else {
                ans.put(afinal, aco);
            }
            if (bco.equals(BigInteger.ZERO)) {
                ans.remove(bfinal);
            } else {
                ans.put(bfinal, bco);
            }
            if (ans.keySet().contains(mergedKey)) {
                ans.put(mergedKey, ans.get(mergedKey).add(coefficient));
            } else {
                ans.put(mergedKey, coefficient);
            }
        }
        return simplified;
    }
    
    public BigInteger getCoefficient(BigInteger co1, BigInteger co2) {
        if (co1.compareTo(BigInteger.ZERO) > 0) {
            if (co1.compareTo(co2) > 0) {
                return co2.add(BigInteger.ZERO);
            } else {
                return co1.add(BigInteger.ZERO);
            }
        } else {
            if (co1.compareTo(co2) > 0) {
                return co1.add(BigInteger.ZERO);
            } else {
                return co2.add(BigInteger.ZERO);
            }
        }
    }
    
    public void reverse() {
        ans.replaceAll((k, v) -> ans.get(k).multiply(new BigInteger("-1")));
    }
    
}
