import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;

public class Expression {
    private final ArrayList<Term> terms;

    public Expression() {
        this.terms = new ArrayList<>();
    }

    public Expression(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public Expression deepClone() {
        ArrayList<Term> newTerms = new ArrayList<>();
        for (Term term : terms) {
            newTerms.add(term.deepClone());
        }
        return new Expression(newTerms);
    }

    public ArrayList<Term> getTerms() {
        return this.terms;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void delTerm(int index) {
        terms.remove(index);
    }

    public void clearExpressionInTerm() {
        for (int i = 0; i < terms.size(); i++) {
            Term t = this.getTerm(i);
            if (!t.hasExpressionFactor()) {
                continue;
            }
            ArrayList<Term> splitTerms = t.departBracket();
            this.delTerm(i--);
            for (Term term : splitTerms) {
                this.addTerm(term);
            }
        }
    }

    public void uniteLikeTerm() {
        sortInCoefficientDescendingOrder();
        for (int i = 0; i < terms.size(); i++) {
            Term term1 = terms.get(i);
            if (term1.hasExpressionFactor()) {
                continue;
            }
            for (int j = i + 1; j < terms.size(); j++) {
                Term term2 = terms.get(j);
                if (term2.hasExpressionFactor()) {
                    continue;
                }
                if (term1.modeEquals(term2)) {
                    term1.setCoefficient(term1.getCoefficient().add(term2.getCoefficient()));
                    terms.remove(j--);
                } else {
                    int position = term1.findTrigSquarePair(term2);
                    if (position != -1) {
                        if (term1.getCoefficient().equals(term2.getCoefficient())) {
                            term1.delTrig(position);
                            terms.remove(j--);
                        } else {
                            term1.setCoefficient(
                                    term1.getCoefficient().subtract(term2.getCoefficient())
                            );
                            //BigInteger num = term2.getCoefficient();
                            term2.delTrig(position);
                            //terms.add(new Term(num, 0));
                        }
                    }
                }
            }
        }
        clearZeroTerm();
    }

    public void clearZeroTerm() {
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).getCoefficient().equals(BigInteger.ZERO)) {
                if (terms.size() > 1) {
                    terms.remove(i--);
                }
            }
        }
    }

    public void sortInCoefficientDescendingOrder() {
        terms.sort(Comparator.reverseOrder());
    }

    public int size() {
        return terms.size();
    }

    public Term getTerm(int i) {
        return terms.get(i);
    }

    public void multiplyTerm(Term other) {
        for (Term term : this.terms) {
            term.multiplyTerm(other);
        }
    }

    public void multiplyExpression(Expression other) {
        Expression origin = this.deepClone();
        for (int i = 0; i < other.terms.size(); i++) {
            Term term = other.terms.get(i);
            if (i == 0) {
                this.multiplyTerm(term);
            } else {
                Expression newAns = origin.deepClone();
                newAns.multiplyTerm(term);
                this.addExpression(newAns);
            }
        }
    }

    public void addExpression(Expression other) {
        this.terms.addAll(other.terms);
    }

    public String inFormat() {
        boolean isFirst = true;
        StringBuilder stringFormat = new StringBuilder();
        for (Term term : terms) {
            if (!isFirst) {
                stringFormat.append("+");
            }
            stringFormat.append(term.inFormat());
            isFirst = false;
        }
        return stringFormat.toString();
    }

    @Override
    public String toString() {
        StringBuilder format = new StringBuilder();
        for (Term term : terms) {
            format.append(term);
        }
        return format.toString();
    }
}
