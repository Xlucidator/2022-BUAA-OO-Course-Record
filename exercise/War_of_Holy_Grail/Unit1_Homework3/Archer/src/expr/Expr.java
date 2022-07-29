package expr;

import java.util.ArrayList;

public class Expr implements Factor {
    private ArrayList<SignedTerm> signedTerms;

    class SignedTerm {
        private Term term;
        private String sign;

        public SignedTerm(String sign, Term term) {
            this.sign = sign;
            this.term = term;
        }

        public void setTerm(Term term) { this.term = term; }

        public void setSign(String sign) { this.sign = sign; }

        public Term getTerm() { return this.term; }

        public String getSign() { return this.sign; }
    }

    public ArrayList<SignedTerm> getArray() {
        return signedTerms;
    }

    public void addArray(ArrayList<SignedTerm> signedTerms) {
        this.signedTerms.addAll(signedTerms);
    }

    public Expr() {
        signedTerms = new ArrayList<>();
    }

    public void addsignedTerm(String sign, Term term) {
        this.signedTerms.add(new SignedTerm(sign, term));
    }

    public void copySignedTerm(Expr expr) {
        this.getArray().clear();
        this.addArray(expr.getArray());
    }

    public Poly toPoly() {
        Poly p = signedTerms.get(0).getTerm().toPoly();
        if (signedTerms.get(0).getSign().equals("-")) {
            p = p.toNeg();
        }
        for (int i = 1; i < signedTerms.size(); i++) {
            if (signedTerms.get(i).getSign().equals("+")) {
                p = p.add(signedTerms.get(i).getTerm().toPoly());
            } else {
                p = p.sub(signedTerms.get(i).getTerm().toPoly());
            }
        }
        return p;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < signedTerms.size(); i++) {
            sb.append(signedTerms.get(i).getSign());
            sb.append(signedTerms.get(i).getTerm().toString());
        }
        sb.append(")");
        return sb.toString();
    }

}
