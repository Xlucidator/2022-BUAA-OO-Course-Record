import java.math.BigInteger;
import java.util.HashMap;

public class Term {
    private BigInteger coefficient;
    private BigInteger power;
    private HashMap<Factor, BigInteger> factors;

    public Term(String str) {
        HashMap<Factor, BigInteger> factors = new HashMap<>();
        if (str.equals("x")) {
            this.coefficient = new BigInteger("1");
            this.power = new BigInteger("1");
            this.factors = factors;
        } else {
            this.coefficient = new BigInteger(str);
            this.power = new BigInteger("0");
            this.factors = factors;
        }
    }

    public Term(BigInteger coefficient, BigInteger power, HashMap<Factor, BigInteger> factors) {
        this.coefficient = coefficient;
        this.power = power;
        this.factors = factors;
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public BigInteger getPower() {
        return power;
    }

    public HashMap<Factor, BigInteger> getFactors() {
        return factors;
    }

    public void addCeofficient(BigInteger other) {
        this.coefficient = coefficient.add(other);
    }

    public void subCeofficient(BigInteger other) {
        this.coefficient = coefficient.subtract(other);
    }

    public int compareTo(Term other) {
        int a = 1;
        if (power.compareTo(other.getPower()) == 0) {
            if (factors.size() == other.getFactors().size()) {
                if (factors.size() == 0) {
                    a = 1;
                } else {
                    for (Factor key : factors.keySet()) {
                        if (other.find(key, factors.get(key)) != 0) {
                            a = 0;
                            break;
                        }
                    }
                }
            } else {
                a = 0;
            }
        } else {
            a = 0;
        }
        return a;
    }

    public int find(Factor other, BigInteger x) {
        int a = -1;
        for (Factor key : factors.keySet()) {
            if (key.compareTo(other) == 0 && factors.get(key).compareTo(x) == 0) {
                a = 0;
                break;
            }
        }
        return a;
    }

}
