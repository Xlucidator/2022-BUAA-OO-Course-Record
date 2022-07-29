package expression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Expression implements BaseExpression {
    private final ArrayList<Term> terms;

    public Expression() {  // default value is 0
        this.terms = new ArrayList<>();
        Term term = new Term(BigInteger.ZERO);
        this.terms.add(term);
    }

    public Expression(BigInteger value) {  // set initial value of expression
        this.terms = new ArrayList<>();
        Term term = new Term(value);
        this.terms.add(term);
    }

    public Expression(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public void addTerms(Term term) {
        if (term.getComplexTerm() == null) {
            terms.add(term);
        }
        else {
            terms.addAll(term.getComplexTerm().getTerms());
        }
        expressionAggregate();
    }

    public void expressionAggregate() {  // group like terms
        terms.sort(Comparator.naturalOrder());
        for (int i = 1;i < terms.size();i++) {
            while (i < terms.size() && terms.get(i - 1).compareTo(terms.get(i)) == 0) {
                terms.get(i - 1).setCoefficient(terms.get(i - 1)
                                              .getCoefficient()
                                              .add(terms.get(i)
                                              .getCoefficient()));
                terms.remove(i);
            }
        }
        for (int i = 0;i < terms.size();i++) {  // remove zero term
            if (terms.get(i).getCoefficient().equals(BigInteger.ZERO)) {
                terms.remove(i);
                i--;
            }
        }
        if (terms.size() == 0) {  // save at least one term
            terms.add(new Term(BigInteger.ZERO));
        }
    }

    public void expressionMult(Expression another) {  // this.terms is changed
        ArrayList<Term> result = new ArrayList<>();
        for (Term termA : terms) {
            for (Term termB : another.getTerms()) {
                //  System.out.println("\na: " + termA + " b: " + termB);
                result.add(termMult(termA, termB));
            }
        }
        terms.clear();
        terms.addAll(result);
        expressionAggregate();
    }

    private static Term termMult(Term a, Term b) {  // won't change a and b
        Term result = new Term();
        result.setCoefficient(a.getCoefficient()
                              .multiply(b.getCoefficient()));
        //  int varPowerA = (a.getFactors().size() == 0) ? 0 : a.getFactors().get(0).getPower();
        //  int varPowerB = (b.getFactors().size() == 0) ? 0 : b.getFactors().get(0).getPower();
        //  Base base = (varPowerA + varPowerB == 0) ? null : new Variable();
        //  if (base != null) {
        //      result.getFactors().add(new Factor(varPowerA + varPowerB, base));
        //  }
        for (Factor factor : a.getFactors()) {
            result.getFactors().add(new Factor(factor.getPower(), factor.getBase()));
        }
        for (Factor factor : b.getFactors()) {
            result.getFactors().add(new Factor(factor.getPower(), factor.getBase()));
        }
        //  result.getFactors().addAll(a.getFactors());
        //  result.getFactors().addAll(b.getFactors());
        //  System.out.println("a: " + a + " b: " + b + "\nresult before: " +
        //  result + " result size: " + result.getFactors().size());
        result.termAggregate();
        //  System.out.println("a: " + a + " b: " + b + "\nresult after : " +
        //  result + " result size: " + result.getFactors().size());
        return result;
    }

    @Override
    public String toString() {
        for (int i = 1;i < terms.size();i++) {  // make sure the first element is positive
            if (terms.get(i).getCoefficient().compareTo(BigInteger.ZERO) > 0) {
                Collections.swap(terms, 0, i);
                break;
            }
        }
        // start toString
        Iterator<Term> termIterator = terms.iterator();
        StringBuilder sb = new StringBuilder(termIterator.next().toString());
        while (termIterator.hasNext()) {
            sb.append("+");
            sb.append(termIterator.next().toString());
        }
        return sb.toString().replaceAll("\\+-", "-")
               .replaceFirst("^(?:0\\+|0-)", "")  // begin 0
               .replaceAll("-0\\+|\\+0\\+", "+")  // middle 0
               .replaceAll("-0-|\\+0-", "");  // middle 0
    }
}
