import java.math.BigInteger;
import java.util.ArrayList;
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

    public int compareTo(Term other) {  // if equals return 0, else return 1;
        if (coefficient.compareTo(other.getCoefficient()) != 0) {
            return 1;
        }
        if (power.compareTo(other.getPower()) != 0) {
            return 1;
        }
        if (factors.size() == other.getFactors().size()) {
            if (factors.size() == 0) {
                return 0;
            } else {
                int sum = 0;
                for (Factor key : factors.keySet()) {
                    for (Factor object : other.getFactors().keySet()) {
                        if (key.compareTo(object) == 0) {
                            if (factors.get(key).compareTo(other.getFactors().get(object)) == 0) {
                                sum = sum + 1;
                                break;
                            }
                        }
                    }
                }
                if (sum == factors.size()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
        return 1;
    }

    public int find(ArrayList<Term> terms) {    // if find equals return 1, else return 0;
        int a = 0;
        for (Term key : terms) {
            if (this.compareTo(key) == 0) {
                a = 1;
                break;
            }
        }
        return a;
    }

    public int isSimilar(Term other) {  // 可合并项返回0
        if (power.compareTo(other.getPower()) != 0) {
            return 1;
        }
        if (factors.size() == other.getFactors().size()) {
            if (factors.size() == 0) {
                return 0;
            } else {
                int sum = 0;
                for (Factor key : factors.keySet()) {
                    for (Factor object : other.getFactors().keySet()) {
                        if (key.compareTo(object) == 0) {
                            if (factors.get(key).compareTo(other.getFactors().get(object)) == 0) {
                                sum = sum + 1;
                                break;
                            }
                        }
                    }
                }
                if (sum == factors.size()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
        return 1;
    }

}
