import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Expr implements Factor {
    
    private final ArrayList<Term> terms;
    
    private final ArrayList<String> ops;
    
    public Expr() {
        this.terms = new ArrayList<>();
        this.ops = new ArrayList<>();
    }
    
    public Factor replace(ArrayList<Factor> formPars, ArrayList<Factor> parameters) {
        Expr ref = new Expr();
        for (Term term : terms) {
            ref.addTerm((Term) term.replace(formPars, parameters));
        }
        for (String op : ops) {
            ref.addOp(op);
        }
        return ref;
    }
    
    public Expr clone() {
        Expr expr = new Expr();
        for (Term term : this.terms) {
            expr.addTerm(term.clone());
        }
        for (String op : ops) {
            expr.addOp(op);
        }
        return expr;
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
        return Objects.equals(terms, expr.terms) && Objects.equals(ops, expr.ops);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(terms, ops);
    }
    
    public void addTerm(Term term) {
        this.terms.add(term);
    }
    
    public void addOp(String op) {
        this.ops.add(op);
    }
    
    public boolean isPre() {
        for (Term term : this.terms) {
            if (term.isPre()) {
                return true;
            }
        }
        return false;
    }
    
    public TriPoly toTriPoly() {
        int total = terms.size();
        TriPoly p1 = new TriPoly(new Key(BigInteger.ZERO, new HashSet<>()), new BigInteger("0"));
        for (int i = 0; i < total; i++) {
            TriPoly p2 = terms.get(i).toTriPoly();
            String op1 = ops.get(i);
            String op2 = terms.get(i).getOp();
            if (op1.equals(op2)) {
                p1 = p1.add(p2);
            } else {
                p1 = p1.sub(p2);
            }
        }
        return p1;
    }
}