import java.math.BigInteger;
import java.util.ArrayList;

public class Cos implements Factor {
    private BigInteger coefficient;
    private BigInteger power;
    //  cos(coe * x ** pow)
    private int pos = 2;

    public Cos(String str) {
        if (str.equals("x")) {
            //  cos(x)
            this.coefficient = new BigInteger("1");
            this.power = new BigInteger("1");
        } else {
            //  cos(123)
            BigInteger a = new BigInteger(str);
            if (a.compareTo(BigInteger.ZERO) == -1) {
                this.coefficient = BigInteger.ZERO.subtract(a);
            } else if (a.compareTo(BigInteger.ZERO) == 1) {
                this.coefficient = new BigInteger(str);
            }
            this.power = new BigInteger("0");
        }
        this.pos = 2;
    }

    public Cos(ArrayList<Term> terms) {
        Term term = terms.get(0);
        this.coefficient = term.getCoefficient();
        this.power = term.getPower();
        this.pos = 2;
    }

    public Cos(BigInteger coefficient, BigInteger power) {
        this.coefficient = coefficient;
        this.power = power;
        this.pos = 2;
    }

    public int getPos() {
        return pos;
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    @Override
    public BigInteger getPower() {
        return power;
    }

    public int compareTo(Factor other) {
        if (this.coefficient.compareTo(other.getCoefficient()) == 1) {
            return 1;
        } else if (this.coefficient.compareTo(other.getCoefficient()) == -1) {
            return -1;
        }
        if (this.power.compareTo(other.getPower()) == 1) {
            return 1;
        } else if (this.power.compareTo(other.getPower()) == -1) {
            return -1;
        }
        if (this.pos > other.getPos()) {
            return 1;
        } else if (this.pos < other.getPos()) {
            return -1;
        }
        return 0;
    }
}
