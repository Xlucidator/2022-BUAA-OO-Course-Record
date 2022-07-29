package expr;

import java.math.BigInteger;
import java.util.ArrayList;

public class Sum {
    private Var itr;
    private Constant bottom;
    private Constant top;
    private Term term;
    
    public void setI(Var itr) {
        this.itr = itr;
    }
    
    public void setBottom(Constant bottom) {
        this.bottom = bottom;
    }
    
    public void setTop(Constant top) {
        this.top = top;
    }
    
    public void setTerm(Term term) {
        this.term = term;
    }
    
    public Expr toExpr() {
        ArrayList<Factor> parameters = new ArrayList<>();
        parameters.add(itr);
        Expr ref = new Expr();
        for (BigInteger i = bottom.getNum();
             i.compareTo(top.getNum()) <= 0;
             i = i.add(BigInteger.ONE)) {
            ArrayList<Factor> actualParameters = new ArrayList<>();
            actualParameters.add(new Constant(i.toString()));
            ref.addTerm((Term) term.replace(parameters, actualParameters));
        }
        return ref;
    }
}
