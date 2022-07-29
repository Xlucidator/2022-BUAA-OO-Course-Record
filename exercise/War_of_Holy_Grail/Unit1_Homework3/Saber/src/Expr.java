import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Expr {
    private ArrayList<Term> terms;

    public Expr(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public ArrayList<Term> getNew() {
        ArrayList<Term> temp = new ArrayList<>();
        for (Term key : terms) {
            BigInteger a = key.getCoefficient();
            if (a.compareTo(BigInteger.ZERO) != 0) {
                temp.add(key);
            }
        }
        return temp;
    }

    public int compareTo(Expr other) {  // if equals return 0; else return 1;
        ArrayList<Term> temp1 = this.getNew();
        ArrayList<Term> temp2 = other.getNew();
        if (temp1.size() == 0 && temp2.size() == 0) {
            return 0;
        } else if (temp1.size() == temp2.size()) {
            int flag = 0;
            for (Term key : temp1) {
                if (key.find(temp2) == 0) { //can not find
                    flag = 1;
                }
            }
            return flag;
        } else {
            return 1;
        }
    }

    public Integer type() {
        ArrayList<Term> termsTmp = new ArrayList<>();
        for (Term key : terms) {
            if (key.getCoefficient().compareTo(BigInteger.ZERO) != 0) {
                termsTmp.add(key);
            }
        }
        if (termsTmp.size() == 0) {
            return 0;
        } else if (termsTmp.size() == 1) {
            Term term = termsTmp.get(0);
            if (term.getPower().compareTo(BigInteger.ZERO) == 0) {
                if (count(term.getFactors()) == 0) {
                    return 0;
                } else if (count(term.getFactors()) == 1) {
                    if (term.getCoefficient().compareTo(BigInteger.ONE) == 0) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            } else {
                if (term.getCoefficient().compareTo(BigInteger.ONE) == 0) {
                    if (count(term.getFactors()) == 0) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        } else {
            return 1;
        }
    }

    public int count(HashMap<Factor, BigInteger> factors) {
        int a = 0;
        for (Factor key : factors.keySet()) {
            if (factors.get(key).compareTo(BigInteger.ZERO) != 0) {
                a = a + 1;
            }
        }
        return a;   // 三角的指数全为0，返回true，没有三角函数因子也返回true
    }
}