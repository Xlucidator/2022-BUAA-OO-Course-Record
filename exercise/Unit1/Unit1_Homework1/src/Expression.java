import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;

public class Expression {
    private final ArrayList<Term> terms;

    public Expression() {
        this.terms = new ArrayList<>();
    }

    public void addTerm(Term term) {
        terms.add(term);
    }

    public void delTerm(int index) {
        terms.remove(index);
    }

    public void uniteLikeTerm() {
        for (int i = 0; i < terms.size(); i++) {
            Term term1 = terms.get(i);
            if (term1.getBracketExpressions().size() != 0) {
                continue;
            }
            for (int j = i + 1; j < terms.size(); j++) {
                Term term2 = terms.get(j);
                if (term2.getBracketExpressions().size() != 0) {
                    continue;
                }
                if (term1.getDegree() == term2.getDegree()) {
                    term1.setCoefficient(term1.getCoefficient().add(term2.getCoefficient()));
                    terms.set(i, term1);
                    terms.remove(j--);
                }
            }
        }
        clearZeroTerm();
    }

    public void clearZeroTerm() {
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).getCoefficient().equals(BigInteger.ZERO)) {
                terms.remove(i--);
            }
        }
    }

    public void sortInCoefficientDescendingOrder() {
        terms.sort(Comparator.reverseOrder());
    }

    public int size() {
        return terms.size();
    }

    public Term get(int i) {
        return terms.get(i);
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
