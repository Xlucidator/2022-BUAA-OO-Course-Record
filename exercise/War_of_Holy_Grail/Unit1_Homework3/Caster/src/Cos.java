import java.math.BigInteger;
import java.util.ArrayList;

public class Cos implements Factor {
    private BigInteger coef;
    private BigInteger exp;
    private Factor fac;

    public BigInteger getCoef() {
        return coef;
    }

    public BigInteger getExp() {
        return exp;
    }

    public Factor getFac() {
        return fac;
    }

    public void setFac(Factor fac) {
        this.fac = fac;
    }

    public void setExp(BigInteger exp) {
        this.exp = exp;
    }

    public Cos(BigInteger num, Factor fac) {
        this.exp = num;
        this.fac = fac;
        this.coef = new BigInteger("1");
    }

    public void compute() {
        if (fac instanceof Expr) {
            ((Expr) fac).compute();
        }
        else if (fac instanceof Sum) {
            Expr tmpExpr = ((Sum) fac).compute();
            fac = tmpExpr;
        }
        else if (fac instanceof Sin) {
            ((Sin) fac).compute();
        }
        else if (fac instanceof Cos) {
            ((Cos) fac).compute();
        }
    }

    public void replace(Factor newTerm, ArrayList<Expr> args,
                        ArrayList<String> param) {
        if (this.getFac() instanceof Variable) {
            Variable facTmp = (Variable) this.getFac();
            for (int k = 0; k < param.size(); k++) {
                String paramK = param.get(k);
                if (facTmp.getName().equals(paramK)) {
                    BigInteger tmpExp = args.get(k).getExp().multiply(facTmp.getExp());
                    Expr exprTmp = args.get(k).copy();
                    exprTmp.addExp(tmpExp);
                    ((Cos) newTerm).setFac(exprTmp);
                    break;
                }
            }
        }
        else if (this.getFac() instanceof Expr) {
            ((Expr) this.getFac()).replace(
                    (Expr) ((Cos) newTerm).getFac(),
                    args, param);
        }
        else if (this.getFac() instanceof Sin) {
            ((Sin) this.getFac()).replace(
                    ((Cos) newTerm).fac, args, param);
        }
        else if (this.getFac() instanceof Cos) {
            ((Cos) this.getFac()).replace(
                    ((Cos) newTerm).fac, args, param);
        }
    }

    public void replace(Factor newTerm, Number value) {
        if (this.getFac() instanceof Variable) {
            Variable facTmp = (Variable) this.getFac();
            if (facTmp.getName().equals("i")) {
                BigInteger expTmp = facTmp.getExp();
                BigInteger idx = new BigInteger("0");
                BigInteger res = new BigInteger("1");
                for (; !idx.equals(expTmp); idx = idx.add(BigInteger.valueOf(1))) {
                    res = res.multiply(value.getCoef());
                }
                ((Cos) newTerm).setFac(new Number(res));
            }
        }
        else if (this.getFac() instanceof Expr) {
            ((Expr) this.getFac()).replace(
                    (Expr) ((Cos) newTerm).getFac(), value);
        }
        else if (this.getFac() instanceof Sin) {
            ((Sin) this.getFac()).replace(
                    ((Cos) newTerm).fac, value);
        }
        else if (this.getFac() instanceof Cos) {
            ((Cos) this.getFac()).replace(
                    ((Cos) newTerm).fac, value);
        }
    }

    public Cos copy() {
        BigInteger expTmp = new BigInteger(this.exp.toString());
        Factor facTmp = this.fac.copy();
        return new Cos(expTmp, facTmp);
    }

    public Boolean equals(Factor other) {
        if (!(other instanceof Cos)) {
            return false;
        }
        if (!this.exp.equals(((Cos) other).exp)) {
            return false;
        }
        if (!this.coef.equals(((Cos) other).coef)) {
            return false;
        }
        if (!this.fac.equals(((Cos) other).fac)) {
            return false;
        }
        return true;
    }

    public String toString() {
        if (this.exp.equals(BigInteger.valueOf(0))) {
            return "1";
        }
        else if (this.exp.equals(BigInteger.valueOf(1))) {
            if (fac instanceof Expr) {
                if (((Expr) fac).getResults().size() == 1) {
                    Term tmpTerm = ((Expr) fac).getResults().get(0);
                    if (tmpTerm.getCoef().compareTo(BigInteger.valueOf(1)) == 0) {
                        if (tmpTerm.getSinFactors().size() + tmpTerm.getCosFactors().size() == 1
                                && tmpTerm.getExp().equals(BigInteger.valueOf(0))) {
                            return "cos(" + this.fac.toString() + ")";
                        }
                        else if (tmpTerm.getSinFactors().size() +
                                tmpTerm.getCosFactors().size() == 0) {
                            return "cos(" + this.fac.toString() + ")";
                        }
                    }
                    else if (tmpTerm.getCosFactors().size() == 0 &&
                            tmpTerm.getSinFactors().size() == 0 &&
                            tmpTerm.getExp().equals(BigInteger.valueOf(0))) {
                        return "cos(" + this.fac.toString() + ")";
                    }
                    else if (tmpTerm.getCoef().compareTo(BigInteger.valueOf(0)) == 0) {
                        return "cos(" + this.fac.toString() + ")";
                    }
                }
                else if (((Expr) fac).getResults().size() == 0) {
                    return "cos(" + this.fac.toString() + ")";
                }
                return "cos((" + this.fac.toString() + "))";
            }
            else {
                return "cos(" + this.fac.toString() + ")";
            }
        }
        else {
            return toStringHelper();
        }
    }

    public String toStringHelper() {
        if (fac instanceof Expr) {
            if (((Expr) fac).getResults().size() == 1) {
                Term tmpTerm = ((Expr) fac).getResults().get(0);
                if (tmpTerm.getCoef().compareTo(BigInteger.valueOf(1)) == 0) {
                    if (tmpTerm.getSinFactors().size() + tmpTerm.getCosFactors().size() == 1
                            && tmpTerm.getExp().equals(BigInteger.valueOf(0))) {
                        return "cos(" + this.fac.toString() + ")**" + this.exp;
                    }
                    else if (tmpTerm.getSinFactors().size() +
                            tmpTerm.getCosFactors().size() == 0) {
                        return "cos(" + this.fac.toString() + ")**" + this.exp;
                    }
                }
                else if (tmpTerm.getCosFactors().size() == 0 &&
                        tmpTerm.getSinFactors().size() == 0 &&
                        tmpTerm.getExp().equals(BigInteger.valueOf(0))) {
                    return "cos(" + this.fac.toString() + ")**" + this.exp;
                }
                else if (tmpTerm.getCoef().compareTo(BigInteger.valueOf(0)) == 0) {
                    return "cos(" + this.fac.toString() + ")**" + this.exp;
                }
            }
            else if (((Expr) fac).getResults().size() == 0) {
                return "cos(" + this.fac.toString() + ")**" + this.exp;
            }
            return "cos((" + this.fac.toString() + "))**" + this.exp;
        }
        else {
            return "cos(" + this.fac.toString() + ")**" + this.exp;
        }
    }
}

