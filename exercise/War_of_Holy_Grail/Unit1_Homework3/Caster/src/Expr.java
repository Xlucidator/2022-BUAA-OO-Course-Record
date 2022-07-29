import java.math.BigInteger;
import java.util.ArrayList;

public class Expr implements Factor {

    private ArrayList<Term> terms;
    private ArrayList<String> ops;
    private ArrayList<Term> results;
    private BigInteger exp;
    private BigInteger coef;

    public Expr(ArrayList<Term> terms, ArrayList<Term> res,
                ArrayList<String> ops, BigInteger exp, BigInteger coef) {
        this.terms = terms;
        this.ops = ops;
        this.exp = exp;
        this.coef = coef;
        this.results = res;
    }

    public Expr() {
        this.terms = new ArrayList<>();
        this.ops = new ArrayList<>();
        this.exp = BigInteger.valueOf(1);
        this.coef = new BigInteger("1");
        this.results = new ArrayList<>();
    }

    public ArrayList<Term> getResults() {
        return this.results;
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public BigInteger getExp() {
        return exp;
    }

    public BigInteger getCoef() {
        return coef;
    }

    public void addOp(String op) {
        this.ops.add(op);
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void addExp(BigInteger exp) { this.exp = exp; }

    public Expr copy() {
        ArrayList<Term> termsTmp = new ArrayList<>();
        ArrayList<Term> resTmp = new ArrayList<>();
        ArrayList<String> opsTmp = (ArrayList<String>) this.ops.clone();
        BigInteger expTmp = new BigInteger(this.exp.toString());
        BigInteger coefTmp = new BigInteger(this.coef.toString());
        for (Term term: terms) {
            termsTmp.add(term.copy());
        }
        for (Term term: results) {
            resTmp.add(term.copy());
        }
        return new Expr(termsTmp, resTmp, opsTmp, expTmp, coefTmp);
    }

    public void replaceVariable(ArrayList<Term> terms, Expr newExpr, ArrayList<Expr> args,
                              ArrayList<String> param, int i, int j) {
        for (int k = 0; k < param.size(); k++) {
            String paramK = param.get(k);
            if (((Variable) terms.get(i).getFactors().get(j))
                    .getName().equals(paramK)) {
                BigInteger tmpExp = args.get(k).getExp().multiply(
                        ((Variable) terms.get(i).getFactors().get(j)).getExp());
                Expr exprTmp = args.get(k).copy();
                exprTmp.addExp(tmpExp);
                newExpr.getTerms().get(i).getFactors().set(j, exprTmp);
                break;
            }
        }
    }

    public void replace(Expr newExpr, ArrayList<Expr> args, ArrayList<String> param) {
        ArrayList<Term> terms = this.getTerms();
        for (int i = 0; i < terms.size(); i++) {
            for (int j = 0; j < terms.get(i).getFactors().size(); j++) {
                if (terms.get(i).getFactors().get(j) instanceof Variable) {
                    replaceVariable(terms, newExpr, args, param, i, j);
                }
                else if (terms.get(i).getFactors().get(j) instanceof Sin) {
                    ((Sin) terms.get(i).getFactors().get(j)).replace(
                            newExpr.getTerms().get(i).getFactors().get(j), args, param);
                }
                else if (terms.get(i).getFactors().get(j) instanceof Cos) {
                    ((Cos) terms.get(i).getFactors().get(j)).replace(
                            newExpr.getTerms().get(i).getFactors().get(j), args, param);
                }
                else if (terms.get(i).getFactors().get(j) instanceof Expr) {
                    ((Expr) terms.get(i).getFactors().get(j)).replace(
                            (Expr) (newExpr.getTerms().get(i).getFactors().get(j)), args, param);
                }
            }
        }
    }

    public void replace(Expr newExpr, Number value) {
        for (int i = 0; i < terms.size(); i++) {
            for (int j = 0; j < terms.get(i).getFactors().size(); j++) {
                if (terms.get(i).getFactors().get(j) instanceof Variable) {
                    if (((Variable) terms.get(i).getFactors().get(j)).getName().equals("i")) {
                        BigInteger expTmp = ((Variable) terms.get(i).getFactors().get(j)).getExp();
                        BigInteger idx = new BigInteger("0");
                        BigInteger res = new BigInteger("1");
                        for (; !idx.equals(expTmp); idx = idx.add(BigInteger.valueOf(1))) {
                            res = res.multiply(value.getCoef());
                        }
                        newExpr.terms.get(i).getFactors().set(j, new Number(res));
                    }
                }
                else if (terms.get(i).getFactors().get(j) instanceof Sin) {
                    ((Sin) terms.get(i).getFactors().get(j)).replace(
                            newExpr.getTerms().get(i).getFactors().get(j), value);
                }
                else if (terms.get(i).getFactors().get(j) instanceof Cos) {
                    ((Cos) terms.get(i).getFactors().get(j)).replace(
                            newExpr.getTerms().get(i).getFactors().get(j), value);
                }
                else if (terms.get(i).getFactors().get(j) instanceof Expr) {
                    ((Expr) terms.get(i).getFactors().get(j)).replace(
                            (Expr) (newExpr.getTerms().get(i).getFactors().get(j)), value);
                }
            }
        }
    }

    public void addExpr(Expr tmpExpr) {
        for (Term term: tmpExpr.results) {
            this.results.add(term);
        }
    }

    public void compute() {
        if (this.exp.equals(BigInteger.valueOf(0))) {
            Term term = new Term(BigInteger.valueOf(1), BigInteger.valueOf(0));
            results.add(term);
            return;
        }
        else {
            for (int i = 0; i < terms.size(); i++) {
                Term term = terms.get(i);
                String op = ops.get(i);
                Expr tmpExpr = term.compute();
                tmpExpr.simplify();
                for (int j = 0; j < tmpExpr.getResults().size(); j++) {
                    if (op.equals("+")) {
                        results.add(tmpExpr.getResults().get(j));
                    } else {
                        BigInteger tmpCoef = tmpExpr.getResults().get(j).getCoef().
                                multiply(BigInteger.valueOf(-1));
                        tmpExpr.getResults().get(j).setCoef(tmpCoef);
                        results.add(tmpExpr.getResults().get(j));
                    }
                }
            }
            Expr tmpExpr = new Expr();
            for (Term term : results) {
                tmpExpr.results.add(term);
            }
            BigInteger idx = new BigInteger("1");
            for (; !idx.equals(this.exp); idx = idx.add(BigInteger.valueOf(1))) {
                Term x = new Term();
                tmpExpr = x.computeExpr(results, tmpExpr.getResults());
                tmpExpr.simplify();
            }
            tmpExpr.simplify();
            results = tmpExpr.results;
        }
    }

    public void simplify() {
        for (int i = 0; i < this.results.size(); i++) {
            Term a = this.results.get(i);
            a.simplify();
            for (int j = i + 1; j < this.results.size(); j++) {
                Term b = this.results.get(j);
                b.simplify();
                if (a.isSame(b)) {
                    a.setCoef(a.getCoef().add(b.getCoef()));
                    this.results.remove(j);
                    j--;
                }
            }
        }
    }

    public Boolean equals(Factor other) {
        if (!(other instanceof Expr)) {
            return false;
        }
        Expr tmpExpr = (Expr) other.copy();
        if (tmpExpr.results.size() != this.results.size()) {
            return false;
        }
        else {
            for (int i = 0; i < this.results.size(); i++) {
                int flag = 0;
                Term termA = this.results.get(i);
                for (int j = 0; j < tmpExpr.results.size(); j++) {
                    Term termB = tmpExpr.results.get(j);
                    if (termA.equals(termB)) {
                        flag = 1;
                        tmpExpr.results.remove(j);
                        break;
                    }
                }
                if (flag == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    public String toString() {
        this.simplify();
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        for (Term term : results) {
            int sign = term.getCoef().compareTo(BigInteger.valueOf(0));
            if (sign != 0) {
                if (sign > 0 && idx != 0) {
                    sb.append("+");
                }
                idx = 1;
                int flag = 0;
                if (term.getCoef().equals(BigInteger.valueOf(1))) {
                    flag = 1;
                }
                else if (term.getCoef().equals(BigInteger.valueOf(-1))) {
                    flag = -1;
                    sb.append("-");
                }
                else {
                    sb.append(term.getCoef().toString());
                }
                if (!term.getExp().equals(BigInteger.valueOf(0))) {
                    if (flag == 0) {
                        sb.append("*");
                    }
                    sb.append("x");
                    flag = 0;
                    if (!term.getExp().equals(BigInteger.valueOf(1))) {
                        sb.append("**");
                        sb.append(term.getExp().toString());
                    }
                }
                for (Sin sin : term.getSinFactors()) {
                    if (!sin.getExp().equals(BigInteger.valueOf(0))) {
                        if (flag == 0) {
                            sb.append("*");
                        }
                        flag = 0;
                        sb.append(sin.toString());
                    }
                }
                for (Cos cos : term.getCosFactors()) {
                    if (!cos.getExp().equals(BigInteger.valueOf(0))) {
                        if (flag == 0) {
                            sb.append("*");
                        }
                        flag = 0;
                        sb.append(cos.toString());
                    }
                }
                if (flag != 0) {
                    sb.append("1");
                }
            }
        }
        if (sb.toString().equals("")) { sb.append("0"); }
        return sb.toString();
    }
}
