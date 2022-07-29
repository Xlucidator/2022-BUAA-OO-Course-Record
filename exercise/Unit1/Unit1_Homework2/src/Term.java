import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;

public class Term implements Comparable<Term> {
    private final Expression bracketExpression;
    private final ArrayList<TrigFunction> trigFunctions;
    private final PolyFactor polyFactor;

    public Term(BigInteger coefficient, int index) {
        this.polyFactor = new PolyFactor(coefficient, index);
        this.trigFunctions = new ArrayList<>();
        this.bracketExpression = new Expression();
    }

    public Term(PolyFactor poly, ArrayList<TrigFunction> trigs) {
        this.polyFactor = poly;
        this.trigFunctions = trigs;
        this.bracketExpression = new Expression();
    }

    public Term(PolyFactor poly, ArrayList<TrigFunction> trigs, Expression bracketExpr) {
        this.polyFactor = poly;
        this.trigFunctions = trigs;
        this.bracketExpression = bracketExpr;
    }

    public Term deepClone() {
        PolyFactor newPolyFactor = polyFactor.deepClone();

        ArrayList<TrigFunction> newTrigFunctions = new ArrayList<>();
        for (TrigFunction trig : this.trigFunctions) {
            newTrigFunctions.add(trig.deepClone());
        }

        Expression newBracketExpression = new Expression();
        for (Term term : this.bracketExpression.getTerms()) {
            newBracketExpression.addTerm(term.deepClone());
        }

        return new Term(newPolyFactor, newTrigFunctions, newBracketExpression);
    }

    public PolyFactor getPolyFactor() {
        return this.polyFactor;
    }

    public Expression getBracketExpression() {
        return this.bracketExpression;
    }

    public PolyFactor clonePolyFactor() {
        int index = this.polyFactor.getIndex();
        BigInteger coefficient = this.polyFactor.getCoefficient();
        return new PolyFactor(coefficient, index);
    }

    public ArrayList<TrigFunction> cloneTrigFunctions() {
        ArrayList<TrigFunction> trigs = new ArrayList<>();
        for (TrigFunction trig : this.trigFunctions) {
            String name = trig.getName();
            PolyFactor varFactor = trig.getVarFactor();
            int index = trig.getIndex();
            trigs.add(new TrigFunction(name, varFactor, index));
        }
        return trigs;
    }

    public void addExpression(Expression other) {
        if (bracketExpression.size() == 0) {
            this.bracketExpression.addExpression(other.deepClone());
            return;
        }
        this.bracketExpression.multiplyExpression(other);
    }

    public void addTrig(TrigFunction trigFunction) {
        if (trigFunction.equalOne()) {
            return;
        }
        if (trigFunction.equalZero()) {
            this.setCoefficient(BigInteger.ZERO);
            return;
        }
        if (trigFunction.getVarFactor().getCoefficient().compareTo(BigInteger.ZERO) < 0) {
            this.setCoefficient(getCoefficient().multiply(trigFunction.reverseInnerNeg()));
        }
        for (TrigFunction trig : this.trigFunctions) {
            if (trig.modeEquals(trigFunction)) {
                trig.absorbIn(trigFunction);
                return;
            }

        }
        this.trigFunctions.add(trigFunction);
        trigFunctions.sort(Comparator.reverseOrder());
    }

    public void addTrigs(ArrayList<TrigFunction> other) {
        for (TrigFunction trigFunction : other) {
            this.addTrig(trigFunction);
        }
    }

    public void delTrig(int i) {
        trigFunctions.remove(i);
    }

    public void addPolyFactor(PolyFactor poly) {
        this.polyFactor.multiplyPoly(poly);
    }

    public boolean hasExpressionFactor() {
        return bracketExpression.size() != 0;
    }

    public ArrayList<Term> departBracket() {
        if (!this.hasExpressionFactor()) {
            return null;
        }
        ArrayList<Term> splitTerms = new ArrayList<>();
        for (int i = 0; i < bracketExpression.size(); i++) {
            Term tmp = new Term(this.clonePolyFactor(), this.cloneTrigFunctions());
            tmp.multiplyTerm(bracketExpression.getTerm(i));
            splitTerms.add(tmp);
        }
        return splitTerms;
    }
    /*
    Term tmp = new Term(this.getPolyFactor(), this.getTrigFunctions()); // WARNING
    tmp.multiplyTerm(bracketExpression.getTerm(i)); // changes in 'tmp' will alter 'this' as well
    */

    public void multiplyTerm(Term other) {
        this.polyFactor.multiplyPoly(other.clonePolyFactor());
        this.addTrigs(other.cloneTrigFunctions());
        this.addExpression(other.getBracketExpression());
    }

    public boolean modeEquals(Term other) {
        if (this.polyFactor.getIndex() != other.getPolyFactor().getIndex()) {
            return false;
        } else {
            ArrayList<TrigFunction> trigs1 = this.trigFunctions;
            ArrayList<TrigFunction> trigs2 = other.trigFunctions;
            if (trigs1.size() != trigs2.size()) {
                return false;
            }
            for (int i = 0; i < trigs1.size(); i++) {
                if (trigs1.get(i).compareTo(trigs2.get(i)) != 0) {
                    return false;
                }
            }
            return true;
        }
        // WARNING : don't know what it will compare : address / value ? no idea!
    }

    public int findTrigSquarePair(Term other) {
        if (this.polyFactor.getIndex() != other.getPolyFactor().getIndex()) {
            return -1;
        } else {
            ArrayList<TrigFunction> trigs1 = this.trigFunctions;
            ArrayList<TrigFunction> trigs2 = other.trigFunctions;
            if (trigs1.size() != trigs2.size()) {
                return -1;
            }
            int diffCount = 0;
            int record = -1;
            for (int i = 0; i < trigs1.size(); i++) {
                TrigFunction trig1 = trigs1.get(i);
                TrigFunction trig2 = trigs2.get(i);
                if (trig1.compareTo(trig2) != 0) {
                    if (trig1.getIndex() != trig2.getIndex()) {
                        return -1;
                    } else if (trig1.getIndex() != 2) {
                        return -1;
                    } else if (!trig1.getVarFactor().equals(trig2.getVarFactor())) {
                        return -1;
                    } else if (diffCount >= 1) {
                        return -1;
                    }
                    diffCount++;
                    record = i;
                }
            }
            return record;
        }

    }

    public BigInteger getCoefficient() {
        return this.polyFactor.getCoefficient();
    }

    public void setCoefficient(BigInteger coefficient) {
        this.polyFactor.setCoefficient(coefficient);
    }

    public String inFormat() {
        StringBuilder trigFactor = new StringBuilder();
        for (TrigFunction trig : trigFunctions) {
            trigFactor.append("*").append(trig.inFormat());
        }

        if (polyFactor.inFormat(true).equals("0")) {
            return "0";
        } else if (polyFactor.inFormat(true).equals("1") && trigFunctions.size() != 0) {
            return trigFactor.substring(1);
        } else if (polyFactor.inFormat(true).equals("-1") && trigFunctions.size() != 0) {
            return "-" + trigFactor.substring(1);
        } else {
            return polyFactor.inFormat(true) + trigFactor;
        }
    }

    @Override
    public String toString() {
        String format;
        format = polyFactor.toString();
        for (TrigFunction trig : trigFunctions) {
            format = format.concat(" * " + trig.toString());
        }
        format = format.concat(" * (" + bracketExpression + ") ");
        return format + " + ";
    }

    @Override
    public int compareTo(Term other) {
        if (this.getCoefficient().compareTo(other.getCoefficient()) < 0) {
            return -1;
        } else if (this.getCoefficient().equals(other.getCoefficient())) {
            return 0;
        } else {
            return 1;
        }
    }
}
