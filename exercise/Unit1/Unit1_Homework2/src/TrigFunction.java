import java.math.BigInteger;

public class TrigFunction implements Comparable<TrigFunction> {
    private final String name;
    private final PolyFactor varFactor;
    private int index;

    public TrigFunction(String name, PolyFactor varFactor, int index) {
        this.name = name;
        this.varFactor = varFactor;
        this.index = index;
    }

    public TrigFunction deepClone() {
        PolyFactor newVarFactor = varFactor.deepClone();
        return new TrigFunction(name, newVarFactor, index);
    }

    public String getName() {
        return this.name;
    }

    public PolyFactor getVarFactor() {
        return this.varFactor;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean modeEquals(TrigFunction other) {
        return this.varFactor.equals(other.getVarFactor()) && this.name.equals(other.getName());
    }

    public void absorbIn(TrigFunction other) {
        this.index += other.getIndex();
    }

    public String toString() {
        return name + "(" + varFactor.toString() + ")" + "**" + index;
    }

    public int compareTo(TrigFunction other) {
        if (!this.getName().equals(other.getName())) {
            return this.getName().equals("sin") ? 1 : -1;
        }
        if (this.getIndex() < other.getIndex()) {
            return -1;
        } else if (this.getIndex() > other.getIndex()) {
            return 1;
        }
        if (this.getVarFactor().compareTo(other.getVarFactor()) < 0) {
            return -1;
        } else if (this.getVarFactor().compareTo(other.getVarFactor()) > 0) {
            return 1;
        }
        return 0;
    }

    public boolean equalOne() {
        return name.equals("cos") && varFactor.getCoefficient().equals(BigInteger.ZERO) ||
                index == 0;
    }

    public boolean equalZero() {
        return name.equals("sin") && varFactor.getCoefficient().equals(BigInteger.ZERO);
    }

    public BigInteger reverseInnerNeg() {
        if (varFactor.getCoefficient().compareTo(BigInteger.ZERO) < 0) {
            if (name.equals("sin")) {
                varFactor.setCoefficient(varFactor.getCoefficient().negate());
                return new BigInteger(varFactor.getIndex() % 2 == 0 ? "1" : "-1");
            } else {
                varFactor.setCoefficient(varFactor.getCoefficient().negate());
                return new BigInteger("1");
            }
        }
        return new BigInteger("1");
    }

    public String inFormat() {
        String indexSuffix = "";
        if (index > 1) {
            indexSuffix = "**" + index;
        }
        return name + "(" + varFactor.inFormat(false)  + ")" + indexSuffix;
    }
}
