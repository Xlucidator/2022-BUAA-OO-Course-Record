package expression;

import operation.Multi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Term extends BasicClass {
    
    private final ArrayList<Factor> factors;
    private final HashMap<Key, BigInteger> ans = new HashMap<>();
    private boolean ansAlready = false;
    
    public Term() {
        factors = new ArrayList<>();
    }
    
    public void addFactor(Factor factor) {
        factors.add(factor);
    }
    
    public int getFactorsSize() {
        return this.factors.size();
    }

    public Factor getFactorAt(int i) {
        return this.factors.get(i);
    }
 
    public HashMap<Key, BigInteger> getAns() {
        BigInteger bigSign = new BigInteger(String.valueOf(this.getSign()));
        if (ansAlready) {
            return this.ans;
        }
        if (this.getExponent().equals(BigInteger.ZERO)) {   // if exponent = 0
            ans.put(new Key(BigInteger.ZERO), BigInteger.ONE);
            ansAlready = true;
            return ans;
        }
        Iterator<Factor> iter = factors.iterator();
        HashMap<Key, BigInteger> midAns = iter.next().getAns();
        while (iter.hasNext()) {
            midAns = Multi.multi(midAns, iter.next().getAns());
        }
        for (Key i : midAns.keySet()) {
            ans.put(i, bigSign.multiply(midAns.get(i)));
        }
        ansAlready = true;
        return this.ans;
    }
    
    public Term replace(ArrayList<Factor> parameters, ArrayList<Factor> arguments) {
        Term replacedTerm = new Term();
        for (Factor factor : factors) {
            if (factor instanceof Variable) {
                boolean flag = false;
                for (int i = 0; i < parameters.size(); i++) {
                    if (parameters.get(i).equals(factor)) {
                        flag = true;
                        Factor replacedFactor = arguments.get(i).clone();
                        replacedFactor.setExponent(((Variable) factor).getExponent());
                        replacedFactor.setSign(((Variable) factor).getSign());
                        replacedTerm.addFactor(replacedFactor);
                    }
                }
                if (!flag) {
                    replacedTerm.addFactor(factor.clone());
                }
            } else if (factor instanceof Expr) {
                Factor replacedFactor = ((Expr) factor).replace(parameters, arguments);
                replacedFactor.setSign(((Expr) factor).getSign());
                replacedFactor.setExponent(((Expr) factor).getExponent());
                replacedTerm.addFactor(replacedFactor);
            } else if (factor instanceof Tri) {
                Expr replacedInnerExpr = ((Tri) factor).getExpr().replace(parameters, arguments);
                replacedInnerExpr.setSign(((Tri) factor).getExpr().getSign());
                Factor replacedFactor = new Tri(((Tri) factor).getName(), replacedInnerExpr);
                replacedFactor.setExponent(((Tri) factor).getExponent());
                replacedFactor.setSign(((Tri) factor).getSign());
                replacedTerm.addFactor(replacedFactor);
            }
            else {
                replacedTerm.addFactor(factor.clone());
            }
        }
        return replacedTerm;
    }
    
    @Override
    public Term clone() {
        Term term = new Term();
        for (Factor factor : factors) {
            term.addFactor(factor.clone());
        }
        term.setExponent(this.getExponent().add(BigInteger.ZERO));
        term.setSign(this.getSign());
        return term;
    }
}
