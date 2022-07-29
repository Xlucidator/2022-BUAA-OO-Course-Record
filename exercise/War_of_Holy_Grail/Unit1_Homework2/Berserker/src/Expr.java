import java.util.ArrayList;

public class Expr {
    private ArrayList<Term> terms;

    public Expr(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<Term> terms) {
        this.terms = terms;
    }
}
