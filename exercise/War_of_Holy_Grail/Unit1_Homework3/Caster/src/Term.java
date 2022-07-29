import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Term implements Factor {
    public ArrayList<Factor> getFactors() {
        return factors;
    }

    private ArrayList<Factor> factors;
    private BigInteger coef;
    private BigInteger exp;
    private ArrayList<Sin> sinFactors;
    private ArrayList<Cos> cosFactors;

    public void addExp(BigInteger exp) {
        this.exp = this.exp.add(exp);
    }

    public void setCoef(BigInteger coef) {
        this.coef = coef;
    }

    public BigInteger getExp() {
        return exp;
    }

    public ArrayList<Sin> getSinFactors() {
        return sinFactors;
    }

    public ArrayList<Cos> getCosFactors() {
        return cosFactors;
    }

    public void addSinFactors(Sin sin) {
        this.sinFactors.add(sin);
    }

    public void addCosFactors(Cos cos) {
        this.cosFactors.add(cos);
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public BigInteger getCoef() {
        return coef;
    }

    public Term() {
        this.exp = BigInteger.valueOf(0);
        this.coef = BigInteger.valueOf(1);
        this.factors = new ArrayList<>();
        this.sinFactors = new ArrayList<>();
        this.cosFactors = new ArrayList<>();
    }

    public Term(BigInteger coef, BigInteger exp, ArrayList<Factor> factors,
                ArrayList<Sin> sinFactors, ArrayList<Cos> cosFactors) {
        this.factors = factors;
        this.coef = coef;
        this.exp = exp;
        this.sinFactors = sinFactors;
        this.cosFactors = cosFactors;
    }

    public Term(BigInteger coef, BigInteger exp) {
        this.coef = coef;
        this.exp = exp;
        this.factors = new ArrayList<>();
        this.sinFactors = new ArrayList<>();
        this.cosFactors = new ArrayList<>();
    }

    public void simplify() {
        for (int i = 0; i < sinFactors.size(); i++) {
            Sin a = sinFactors.get(i);
            for (int j = i + 1; j < sinFactors.size(); j++) {
                Sin b = sinFactors.get(j);
                if (a.getFac().equals(b.getFac())) {
                    a.setExp(a.getExp().add(b.getExp()));
                    sinFactors.remove(j);
                    j--;
                }
            }
        }
        for (int i = 0; i < cosFactors.size(); i++) {
            Cos a = cosFactors.get(i);
            for (int j = i + 1; j < cosFactors.size(); j++) {
                Cos b = cosFactors.get(j);
                if (a.getFac().equals(b.getFac())) {
                    a.setExp(a.getExp().add(b.getExp()));
                    cosFactors.remove(j);
                    j--;
                }
            }
        }
    }

    public Boolean isSame(Term term) {
        boolean a1 = this.exp.equals(term.exp);
        boolean a2 = true;
        if (this.sinFactors.size() != term.sinFactors.size()) {
            a2 = false;
        }
        else {
            for (int i = 0; i < this.sinFactors.size(); i++) {
                Sin sin1 = this.sinFactors.get(i);
                Sin sin2 = term.sinFactors.get(i);
                if (!sin1.equals(sin2)) {
                    a2 = false;
                    break;
                }
            }
        }
        boolean a3 = true;
        if (this.cosFactors.size() != term.cosFactors.size()) {
            a3 = false;
        }
        else {
            for (int i = 0; i < this.cosFactors.size(); i++) {
                Cos cos1 = this.cosFactors.get(i);
                Cos cos2 = term.cosFactors.get(i);
                if (!cos1.equals(cos2)) {
                    a3 = false;
                    break;
                }
            }
        }
        return a1 && a2 && a3;
    }

    public Boolean equals(Factor term) {
        if (!(term instanceof Term)) {
            return false;
        }
        boolean a1 = this.exp.equals(((Term) term).exp);
        boolean a4 = this.coef.equals(((Term) term).coef);
        boolean a2 = true;
        if (this.sinFactors.size() != ((Term) term).sinFactors.size()) {
            a2 = false;
        }
        else {
            for (int i = 0; i < this.sinFactors.size(); i++) {
                Sin sin1 = this.sinFactors.get(i);
                Sin sin2 = ((Term) term).sinFactors.get(i);
                if (!sin1.equals(sin2)) {
                    a2 = false;
                    break;
                }
            }
        }
        boolean a3 = true;
        if (this.cosFactors.size() != ((Term) term).cosFactors.size()) {
            a3 = false;
        }
        else {
            for (int i = 0; i < this.cosFactors.size(); i++) {
                Cos cos1 = this.cosFactors.get(i);
                Cos cos2 = ((Term) term).cosFactors.get(i);
                if (!cos1.equals(cos2)) {
                    a3 = false;
                    break;
                }
            }
        }
        return a1 && a2 && a3 && a4;
    }

    public Term copy() {
        ArrayList<Factor> factorsTmp = new ArrayList<>();
        for (Factor factor: factors) {
            factorsTmp.add(factor.copy());
        }
        ArrayList<Sin> sinFactorsTmp = new ArrayList<>();
        for (Sin sin: sinFactors) {
            sinFactorsTmp.add(sin.copy());
        }
        ArrayList<Cos> cosFactorsTmp = new ArrayList<>();
        for (Cos cos: cosFactors) {
            cosFactorsTmp.add(cos.copy());
        }
        BigInteger expTmp = new BigInteger(this.exp.toString());
        BigInteger coefTmp = new BigInteger(this.coef.toString());
        return new Term(coefTmp, expTmp, factorsTmp, sinFactorsTmp, cosFactorsTmp);
    }

    public Expr computeExpr(ArrayList<Term> resExpr, ArrayList<Term> iterExpr) {
        Expr tmpExpr = new Expr();
        for (int i = 0; i < resExpr.size(); i++) {
            for (int j = 0; j < iterExpr.size(); j++) {
                Term tmpTerm = new Term();
                BigInteger tmpExp = resExpr.get(i).exp.add(iterExpr.get(j).exp);
                BigInteger tmpCoef = resExpr.get(i).coef.multiply(iterExpr.get(j).coef);
                tmpTerm.addExp(tmpExp);
                tmpTerm.setCoef(tmpCoef);
                for (Sin x : resExpr.get(i).sinFactors) {
                    tmpTerm.addSinFactors(x.copy());
                }
                for (Sin y : iterExpr.get(j).sinFactors) {
                    tmpTerm.addSinFactors(y.copy());
                }
                for (Cos x : resExpr.get(i).cosFactors) {
                    tmpTerm.addCosFactors(x.copy());
                }
                for (Cos y : iterExpr.get(j).cosFactors) {
                    tmpTerm.addCosFactors(y.copy());
                }
                tmpExpr.getResults().add(tmpTerm);
            }
        }
        return tmpExpr;
    }

    public Expr compute() {
        Expr resExpr = new Expr();
        Term initTerm = new Term(coef, BigInteger.valueOf(0));
        resExpr.getResults().add(initTerm);
        for (Factor iter: factors) {
            if (iter instanceof Expr) {
                Expr iterExpr = ((Expr) iter);
                iterExpr.compute();
                resExpr = computeExpr(resExpr.getResults(), iterExpr.getResults());
            }
            else if (iter instanceof Number) {
                for (int i = 0; i < resExpr.getResults().size(); i++) {
                    BigInteger tmpRes = resExpr.getResults().get(i).coef.
                            multiply(((Number) iter).getCoef());
                    resExpr.getResults().get(i).coef = tmpRes;
                }
            }
            else if (iter instanceof Cos) {
                ((Cos) iter).compute();
                for (int i = 0; i < resExpr.getResults().size(); i++) {
                    resExpr.getResults().get(i).cosFactors.add((Cos) iter.copy());
                }
            }
            else if (iter instanceof Sin) {
                ((Sin) iter).compute();
                for (int i = 0; i < resExpr.getResults().size(); i++) {
                    resExpr.getResults().get(i).sinFactors.add((Sin) iter.copy());
                }
            }
            else if (iter instanceof Sum) {
                resExpr = computeExpr(resExpr.getResults(), ((Sum) iter).compute().getResults());
            }
            else {
                Variable term =  ((Variable) iter);
                for (int i = 0; i < resExpr.getResults().size(); i++) {
                    resExpr.getResults().get(i).addExp(new BigInteger(term.getExp().toString()));
                }
            }
        }
        return resExpr;
    }

    public String toString() {
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append(" ");
            sb.append(iter.next().toString());
            sb.append(" *");
            while (iter.hasNext()) {
                sb.append(" ");
                sb.append(iter.next().toString());
                sb.append(" *");
            }
        }
        return sb.toString();
    }
}
