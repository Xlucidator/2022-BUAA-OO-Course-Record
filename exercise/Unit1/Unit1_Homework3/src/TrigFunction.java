import java.math.BigInteger;

public class TrigFunction implements Comparable<TrigFunction> {
    private final String name;
    private final Expression innerFactor;
    private int index;

    public TrigFunction(String name, Expression innerFactor, int index) {
        this.name = name;
        this.innerFactor = innerFactor;
        this.index = index;
    }

    public TrigFunction deepClone() {
        Expression newInnerFactors = innerFactor.deepClone();
        return new TrigFunction(name, newInnerFactors, index);
    }

    public String getName() {
        return this.name;
    }

    public Expression getInnerFactor() {
        return this.innerFactor;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean valueEquals(TrigFunction other) {
        return name.equals(other.getName()) &&
                innerFactor.valueEquals(other.getInnerFactor()) &&
                index == other.getIndex();
    }

    public boolean modeEquals(TrigFunction other) {
        return this.innerFactor.valueEquals(other.getInnerFactor())
                && this.name.equals(other.getName());
    }

    public void absorbIn(TrigFunction other) {
        this.index += other.getIndex();
    }

    public boolean equalOne() {
        if (index == 0) {
            return true;
        }
        if (innerFactor.size() != 1) {
            return false;
        }
        BigInteger termCoefficient = innerFactor.getTerm(0).getCoefficient();
        return name.equals("cos") && termCoefficient.equals(BigInteger.ZERO);
    }

    public boolean equalZero() {
        if (innerFactor.size() != 1) {
            return false;
        }
        BigInteger termCoefficient = innerFactor.getTerm(0).getCoefficient();
        return name.equals("sin") && termCoefficient.equals(BigInteger.ZERO);
    }

    public boolean isInnerNegative() {
        return innerFactor.getTerm(0).getCoefficient().compareTo(BigInteger.ZERO) < 0;
    }

    public boolean isInnerSingle() {
        if (innerFactor.size() != 1) {
            return false;
        }
        Term term = innerFactor.getTerm(0);
        if (!term.getCoefficient().equals(BigInteger.ONE)) {
            return term.getIndex() == 0 && term.triSize() == 0;
        } else {
            if (term.getIndex() == 0) {
                return term.triSize() == 0 | term.triSize() == 1;
            } else {
                return term.triSize() == 0;
            }
        }
    }

    public BigInteger reverseInnerNeg() {
        if (isInnerNegative()) {
            innerFactor.reverseEachTerms();
            return new BigInteger((name.equals("sin") && index % 2 != 0) ? "-1" : "1");
        }
        return new BigInteger("1");
    }

    public String inFormat() {
        String indexSuffix = "";
        if (index > 1) {
            indexSuffix = "**" + index;
        }
        boolean addBracket;
        boolean unfoldTwo;
        addBracket = unfoldTwo = !isInnerSingle();
        return name + "(" + innerFactor.inFormat(addBracket, unfoldTwo) + ")"
                + indexSuffix;
    }

    @Override
    public String toString() {
        return name + "(" + innerFactor.toString() + ")" + "**" + index;
    }

    @Override
    public int compareTo(TrigFunction other) {
        if (!this.getName().equals(other.getName())) {
            return this.getName().equals("sin") ? 1 : -1;
        }
        if (this.getIndex() < other.getIndex()) {
            return -1;
        } else if (this.getIndex() > other.getIndex()) {
            return 1;
        }
        if (this.getInnerFactor().compareTo(other.getInnerFactor()) < 0) {
            return -1;
        } else if (this.getInnerFactor().compareTo(other.getInnerFactor()) > 0) {
            return 1;
        }
        return 0;
    }
}
