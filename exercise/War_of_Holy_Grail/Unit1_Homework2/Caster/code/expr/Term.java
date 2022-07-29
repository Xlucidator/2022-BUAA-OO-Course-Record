package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class Term extends Factor {
    private ArrayList<Factor> factors;
    
    public Term() {
        this.factors = new ArrayList<>();
    }
    
    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }
    
    @Override
    public Factor replace(ArrayList<Factor> parameters, ArrayList<Factor> actualParameters) {
        Term ref = new Term();
        for (Factor factor : factors) {
            ref.addFactor(factor.replace(parameters, actualParameters));
        }
        if (!isPos()) {
            ref.changePos();
        }
        return ref;
    }
    
    @Override
    public void update() {
        HashMap<HashMap<Factor, BigInteger>, BigInteger> ref = new HashMap<>();
        boolean isFirst = true;
        for (Factor factor : factors) {
            factor.update();
            if (isFirst) {
                ref = factor.getPoly();
                isFirst = false;
            } else {
                ref = multi(ref, factor.getPoly());
            }
        }
        if (!isPos()) {
            HashMap<HashMap<Factor, BigInteger>, BigInteger> tmp = new HashMap<>();
            tmp.put(new HashMap<>(), new BigInteger("-1"));
            ref = multi(ref, tmp);
        }
        setPoly(ref);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Factor> iterator = factors.iterator();
        if (!this.isPos()) {
            sb.append("-");
        }
        boolean isFirst = true;
        while (iterator.hasNext()) {
            Factor tmp = iterator.next();
            if (isFirst) {
                sb.append(tmp.toString());
                isFirst = false;
            } else {
                sb.append("*");
                sb.append(tmp.toString());
            }
        }
        //        sb.append(iterator.next().toString());
        //        if (iterator.hasNext()) {
        //            sb.append("*");
        //            sb.append(iterator.next().toString());
        //            while (iterator.hasNext()) {
        //                sb.append("*");
        //                sb.append(iterator.next().toString());
        //            }
        //        }
        if (!sb.toString().equals("0")) {
            return sb.toString();
        } else {
            return "";
        }
    }
    
    @Override
    public Factor clone() {
        Term o = new Term();
        for (Factor factor : factors) {
            o.addFactor(factor.clone());
        }
        if (!this.isPos()) {
            o.changePos();
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
        Term term = (Term) o;
        return Objects.equals(factors, term.factors);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(factors);
    }
}
