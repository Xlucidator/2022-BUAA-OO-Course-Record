package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class Expr extends Factor {
    private ArrayList<Term> terms;
    
    public Expr() {
        this.terms = new ArrayList<>();
    }
    
    public void addTerm(Term term) {
        terms.add(term);
    }
    
    @Override
    public Factor replace(ArrayList<Factor> parameters, ArrayList<Factor> actualParameters) {
        Expr ref = new Expr();
        for (Term term : terms) {
            ref.addTerm((Term) term.replace(parameters, actualParameters));
        }
        return ref;
    }
    
    @Override
    public void update() {
        HashMap<HashMap<Factor, BigInteger>, BigInteger> ref = new HashMap<>();
        boolean isFirst = true;
        for (Term term : terms) {
            term.update();
            if (isFirst) {
                ref = term.getPoly();
                isFirst = false;
            } else {
                ref = add(ref, term.getPoly());
            }
        }
        ArrayList<HashMap<Factor, BigInteger>> deleteArray = new ArrayList<>();
        for (HashMap<Factor, BigInteger> power : ref.keySet()) {
            if (ref.get(power).equals(BigInteger.ZERO)) {
                deleteArray.add(power);
            }
        }
        for (HashMap<Factor, BigInteger> power : deleteArray) {
            ref.remove(power);
        }
        setPoly(ref);
    }
    
    @Override
    public String toString() {
        Iterator<Term> iterator = terms.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (!this.isPos()) {
            sb.append("-");
        }
        //        sb.append(iterator.next().toString());
        //        if (iterator.hasNext()) {
        //            Term tmp = iterator.next();
        //            if (tmp.isPos()) {
        //                sb.append("+");
        //            }
        //            sb.append(tmp);
        //            while (iterator.hasNext()) {
        //                Term ref = iterator.next();
        //                if (ref.isPos()) {
        //                    sb.append("+");
        //                }
        //                sb.append(ref);
        //            }
        //        }
        boolean isFirst = true;
        while (iterator.hasNext()) {
            Term tmp = iterator.next();
            if (isFirst) {
                sb.append(tmp.toString());
                isFirst = false;
            } else {
                if (tmp.isPos()) {
                    sb.append("+");
                }
                sb.append(tmp.toString());
            }
        }
        sb.append(")");
        return sb.toString();
    }
    
    //以下为输出函数
    private String displayPower(HashMap<Factor, BigInteger> key) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Factor factor : key.keySet()) {
            if (!isFirst) {
                sb.append("*");
            }
            sb.append(factor.toString());
            if (!key.get(factor).equals(BigInteger.ONE)) {
                if (factor instanceof Var && key.get(factor).equals(new BigInteger("2"))) {
                    sb.append("*");
                    sb.append(factor.toString());
                } else {
                    sb.append("**");
                    sb.append(key.get(factor));
                }
            }
            isFirst = false;
        }
        return sb.toString();
    }
    
    private String displayBasic(HashMap<Factor, BigInteger> key, BigInteger coe) {
        StringBuilder sb = new StringBuilder();
        //系数为1
        if (coe.equals(BigInteger.ONE)) {
            String power = displayPower(key);
            if (power.equals("")) {
                sb.append("1");
            } else {
                sb.append(power);
            }
        } else if (coe.equals(new BigInteger("-1"))) {
            sb.append("-");
            String power = displayPower(key);
            if (power.equals("")) {
                sb.append("1");
            } else {
                sb.append(power);
            }
        } else {
            sb.append(coe);
            String power = displayPower(key);
            if (!power.equals("")) {
                sb.append("*");
            }
            sb.append(power);
        }
        return sb.toString();
    }
    
    public String displayBasicWithSig(HashMap<Factor, BigInteger> key, BigInteger coe) {
        StringBuilder sb = new StringBuilder();
        if (coe.signum() > 0) {
            sb.append("+");
        }
        sb.append(displayBasic(key, coe));
        return sb.toString();
    }
    
    public String display() {
        HashMap<HashMap<Factor, BigInteger>, BigInteger> ref = new HashMap<>(getPoly());
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        //输出第一项
        for (HashMap<Factor, BigInteger> key : ref.keySet()) {
            BigInteger coe = ref.get(key);
            if (coe.signum() > 0) {
                sb.append(displayBasic(key, coe));
                isFirst = false;
                ref.remove(key);
                break;
            }
        }
        for (HashMap<Factor, BigInteger> key : ref.keySet()) {
            BigInteger coe = ref.get(key);
            if (isFirst) {
                sb.append(displayBasic(key, coe));
                isFirst = false;
            } else {
                sb.append(displayBasicWithSig(key, coe));
            }
        }
        if (sb.toString().isEmpty()) {
            sb.append("0");
        }
        return sb.toString();
    }
    
    @Override
    public Factor clone() {
        Expr o = new Expr();
        for (Term term : terms) {
            o.addTerm((Term) term.clone());
        }
        return o;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expr expr = (Expr) o;
        return Objects.equals(terms, expr.terms);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(terms);
    }
}
