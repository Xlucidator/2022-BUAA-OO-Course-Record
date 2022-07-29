import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Term implements Factor {
    
    private final ArrayList<Factor> factors;
    
    public String getOp() {
        return op;
    }
    
    private String op;
    
    public Term() {
        this.factors = new ArrayList<>();
    }
    
    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }
    
    public void setOp(String op) {
        this.op = op;
    }
    
    public boolean isPre() {
        for (Factor factor : this.factors) {
            if (factor.isPre()) {
                return true;
            }
        }
        return false;
    }
    
    public Term clone() {
        Term term = new Term();
        for (Factor factor : this.factors) {
            term.addFactor(factor.clone());
        }
        term.setOp(this.getOp());
        return term;
    }
    
    public Factor replace(ArrayList<Factor> formPars, ArrayList<Factor> parameters) {
        Term ref = new Term();
        for (Factor factor : factors) {
            if (factor instanceof Power) {
                Power power = (Power) factor;
                Factor refactor = factor.replace(formPars, parameters);
                BigInteger exp = power.getIndex();//缓解指数问题
                for (BigInteger i = BigInteger.ZERO; i.compareTo(exp) < 0; ) {
                    ref.addFactor(refactor);
                    i = i.add(BigInteger.ONE);
                }
            }
            else {
                ref.addFactor(factor.replace(formPars, parameters));
            }
        }
        ref.setOp(this.getOp());
        return ref;
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
        return Objects.equals(factors, term.factors) && Objects.equals(op, term.op);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(factors, op);
    }
    
    public TriPoly toTriPoly() {
        TriPoly p1 = new TriPoly(new Key(BigInteger.ZERO, new HashSet<>()), new BigInteger("1"));
        for (Factor factor : factors) {
            TriPoly p2 = factor.toTriPoly();
            p1 = p1.mul(p2);
        }
        return p1;
    }
}
