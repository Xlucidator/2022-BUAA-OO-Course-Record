package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Expr implements Factor {
    private final HashSet<Term> terms;
    private final ArrayList<FactorPower> factorPowers;

    public Expr() {
        this.terms = new HashSet<>();
        this.factorPowers = new ArrayList<>();
    }

    @Override
    public HashSet<Term> getTerms() {
        return terms;
    }

    @Override
    public void setSign(int sign) {

    }

    public ArrayList<FactorPower> getFactorPowers() {
        return factorPowers;
    }

    public void addFactorPowers(FactorPower f) {
        this.factorPowers.add(f);
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void extendExpr(Expr e) {
        for (Term t : e.getTerms()) {
            this.addTerm(t);
        }
    }

    public String toString() {
        Iterator<Term> iter = terms.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append("+");
            sb.append(iter.next().toString());
            while (iter.hasNext()) {
                sb.append("+");
                sb.append(iter.next().toString());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public BigInteger getCoe() {
        return null;
    }

    @Override
    public int getSign() {
        return 0;
    }

    @Override
    public Expr mulExpr(Expr expr) {
        Expr ans = new Expr();
        for (Term item1 : this.getTerms()) {
            for (Term item2 : expr.getTerms()) {
                Expr itemAns = new Expr();
                itemAns = item1.mulTerm(item2);
                ans.extendExpr(itemAns);
            }
        }
        return ans;
    }

    public Expr mulExprFactor(Factor expr) {
        Expr ans = new Expr();
        for (Term item1 : this.getTerms()) {
            for (Term item2 : expr.getTerms()) {
                Expr itemAns = new Expr();
                itemAns = item1.mulTerm(item2);
                ans.extendExpr(itemAns);
            }
        }
        return ans;
    }

    @Override
    public void mulFactorPower(Factor item) {
        for (Term item1 : this.getTerms()) {
            item1.mulFactPower(item);
        }
    }

    @Override
    public Expr mulTerm(Term item) {
        Expr ans = new Expr();
        for (Term t : this.getTerms()) {
            Expr itemAns = t.mulTerm(item);
            ans.extendExpr(itemAns);
        }
        return ans;
    }

    @Override
    public Expr upExprPowerToExpr() {
        return null;
    }

    public void upExpr() {
        FactorPower zero = new FactorPower(1, BigInteger.ZERO, 0);
        factorPowers.add(zero);
        FactorPower one = new FactorPower(1, BigInteger.ZERO, 1);
        factorPowers.add(one);
        FactorPower two = new FactorPower(1, BigInteger.ZERO, 2);
        factorPowers.add(two);
        FactorPower three = new FactorPower(1, BigInteger.ZERO, 3);
        factorPowers.add(three);
        FactorPower four = new FactorPower(1, BigInteger.ZERO, 4);
        factorPowers.add(four);
        FactorPower five = new FactorPower(1, BigInteger.ZERO, 5);
        factorPowers.add(five);
        FactorPower six = new FactorPower(1, BigInteger.ZERO, 6);
        factorPowers.add(six);
        FactorPower seven = new FactorPower(1, BigInteger.ZERO, 7);
        factorPowers.add(seven);
        FactorPower eight = new FactorPower(1, BigInteger.ZERO, 8);
        factorPowers.add(eight);
        FactorPower nine = new FactorPower(1, BigInteger.ZERO, 9);
        factorPowers.add(nine);
        for (Term item : this.getTerms()) {
            int sign = 1;
            if (item.getSign() == -1) {
                sign = -1;
            }
            for (Factor f : item.getFactors()) {
                if (f instanceof FactorPower) {
                    if (sign == -1) {
                        f.setSign(-1);
                    }
                    factorPowers.get(f.getIndex()).addFactorPower(f);
                }
            }
        }
    }

    public void addExpr(Expr expr) {
        int i = 0;
        for (i = 0; i <= 9; i += 1) {
            this.factorPowers.get(i).addFactorPower(expr.getFactorPowers().get(i));
        }
    }

    public String makeAns() {
        int flag = 0;
        StringBuilder sb = new StringBuilder("");
        if (!this.factorPowers.get(0).getCoe().equals(BigInteger.ZERO)) {
            flag = 1;
            if (this.factorPowers.get(0).getSign() == -1) {
                sb.append("-");
            }
            sb.append(this.factorPowers.get(0).getCoe());
        }
        if (!this.factorPowers.get(1).getCoe().equals(BigInteger.ZERO)) {
            flag = 1;
            if (this.factorPowers.get(1).getSign() == 1 &&
                    this.factorPowers.get(1).getCoe().compareTo(BigInteger.ZERO) == 1) {
                sb.append("+");
            }
            else if (this.factorPowers.get(1).getSign() == -1) {
                sb.append("-");
            }
            if (this.factorPowers.get(1).getCoe().equals(BigInteger.ONE)) {
                sb.append("x");
            }
            else if (this.factorPowers.get(1).getCoe().equals((BigInteger.ONE).negate())) {
                sb.append("-x");
            }
            else {
                sb.append(this.factorPowers.get(1).getCoe() + "*x");
            }
        }
        int i = 2;
        while (i <= 9) {
            if (!this.factorPowers.get(i).getCoe().equals(BigInteger.ZERO)) {
                flag = 1;
                if (this.factorPowers.get(i).getSign() == 1 &&
                        this.factorPowers.get(i).getCoe().compareTo(BigInteger.ZERO) == 1) {
                    sb.append("+");
                }
                else if (this.factorPowers.get(i).getSign() == -1) {
                    sb.append("-");
                }
                if (this.factorPowers.get(i).getCoe().equals(BigInteger.ONE)) {
                    sb.append("x**" + i);
                }
                else if (this.factorPowers.get(i).getCoe().equals((BigInteger.ONE).negate())) {
                    sb.append("-x**" + i);
                }
                else {
                    sb.append(this.factorPowers.get(i).getCoe() + "*x**" + i);
                }
            }
            i++;
        }
        if (flag == 0) {
            return "0";
        }
        return sb.toString();
    }
}

