import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Operator {
    private Expr left;  // left operand
    private Expr right; // right operand

    public Operator(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    public ArrayList<Term> opAdd() {
        ArrayList<Term> addterm = new ArrayList<>();                                // addresult
        for (Term object : left.getTerms()) {                                       // copy
            BigInteger x = object.getCoefficient();
            BigInteger y = object.getPower();
            HashMap<Factor, BigInteger> factors = new HashMap<>();
            for (Factor key : object.getFactors().keySet()) {
                if (key.getPos() == 1) {
                    BigInteger z = object.getFactors().get(key);
                    Sin sin = new Sin(key.getExpr());
                    factors.put(sin, z);
                } else if (key.getPos() == 2) {
                    BigInteger z = object.getFactors().get(key);
                    Cos cos = new Cos(key.getExpr());
                    factors.put(cos, z);
                }
            }
            Term term = new Term(x, y, factors);
            addterm.add(term);
        }
        for (Term object : right.getTerms()) {
            int a = 0;
            for (Term key : addterm) {
                if (object.isSimilar(key) == 0) {
                    key.addCeofficient(object.getCoefficient());
                    a = 1;
                    break;
                }
            }
            if (a == 0) {
                addterm.add(object);
            }
        }
        return addterm;
    }

    public ArrayList<Term> opSub() {
        //  subresult
        ArrayList<Term> subterm = new ArrayList<>();
        //  copy
        for (Term object : left.getTerms()) {
            BigInteger x = object.getCoefficient();
            BigInteger y = object.getPower();
            HashMap<Factor, BigInteger> factors = new HashMap<>();
            for (Factor key : object.getFactors().keySet()) {
                if (key.getPos() == 1) {
                    BigInteger z = object.getFactors().get(key);
                    Factor factor = new Sin(key.getExpr());
                    factors.put(factor, z);
                } else if (key.getPos() == 2) {
                    BigInteger z = object.getFactors().get(key);
                    Factor factor = new Cos(key.getExpr());
                    factors.put(factor, z);
                }
            }
            Term term = new Term(x, y, factors);
            subterm.add(term);
        }
        for (Term object : right.getTerms()) {
            int a = 0;
            for (Term key : subterm) {
                if (object.isSimilar(key) == 0) {
                    key.subCeofficient(object.getCoefficient());
                    a = 1;
                    break;
                }
            }
            if (a == 0) {
                BigInteger x = BigInteger.ZERO.subtract(object.getCoefficient());
                BigInteger y = object.getPower();
                Term term = new Term(x, y, object.getFactors());
                subterm.add(term);
            }
        }
        return subterm;
    }

    public ArrayList<Term> opPower(Integer num) {
        if (num == 0) {
            Term term = new Term("1");
            ArrayList<Term> list = new ArrayList<>();
            list.add(term);
            return list;
        } else if (num == 1) {
            ArrayList<Term> list = new ArrayList<>();
            for (Term term : left.getTerms()) {
                list.add(term);
            }
            return list;
        } else {
            Expr expr = new Expr(left.getTerms());
            Expr expr1 = new Expr(left.getTerms());
            for (int i = 0; i < num - 1; i++) {
                Operator operator = new Operator(expr, expr1);
                expr.setTerms(operator.opMul());
            }
            return expr.getTerms();
        }
    }

    public ArrayList<Term> opMul() {
        ArrayList<Term> multerm = new ArrayList<>();
        for (Term term : right.getTerms()) {
            for (Term object : left.getTerms()) {
                BigInteger x1 = term.getCoefficient();
                BigInteger x2 = object.getCoefficient();
                BigInteger y1 = term.getPower();
                BigInteger y2 = object.getPower();
                ArrayList<HashMap<Factor, BigInteger>> x = new ArrayList<>();
                x.add(term.getFactors());
                x.add(object.getFactors());
                HashMap<Factor, BigInteger> c = mulTri(x);
                Term tmp = new Term(x1.multiply(x2), y1.add(y2), c);
                int a = 0;
                for (Term key : multerm) {
                    if (tmp.isSimilar(key) == 0) {
                        key.addCeofficient(tmp.getCoefficient());
                        a = 1;
                        break;
                    }
                }
                if (a == 0) {
                    multerm.add(tmp);
                }
            }
        }
        return multerm;
    }

    public HashMap<Factor, BigInteger> mulTri(ArrayList<HashMap<Factor, BigInteger>> m) {
        HashMap<Factor, BigInteger> a = m.get(0);
        HashMap<Factor, BigInteger> b = m.get(1);
        HashMap<Factor, BigInteger> c = new HashMap<>();
        if (a.size() == 0 && b.size() != 0) {
            for (Factor key : b.keySet()) {
                BigInteger pow = b.get(key);
                if (key.getPos() == 1) {
                    Sin sin = new Sin(key.getExpr());
                    c.put(sin, pow);
                } else if (key.getPos() == 2) {
                    Cos cos = new Cos(key.getExpr());
                    c.put(cos, pow);
                }
            }
        } else if (a.size() != 0) {
            for (Factor key : a.keySet()) {
                BigInteger pow = a.get(key);
                if (key.getPos() == 1) {
                    Sin sin = new Sin(key.getExpr());
                    c.put(sin, pow);
                } else if (key.getPos() == 2) {
                    Cos cos = new Cos(key.getExpr());
                    c.put(cos, pow);
                }
            }
            if (b.size() != 0) {
                for (Factor key : b.keySet()) {
                    int flag = 0;
                    for (Factor object : c.keySet()) {
                        if (object.compareTo(key) == 0) {
                            BigInteger x = b.get(key);
                            BigInteger y = c.get(object);
                            c.put(object, x.add(y));
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        BigInteger pow = b.get(key);
                        if (key.getPos() == 1) {
                            Sin sin = new Sin(key.getExpr());
                            c.put(sin, pow);
                        } else if (key.getPos() == 2) {
                            Cos cos = new Cos(key.getExpr());
                            c.put(cos, pow);
                        }
                    }
                }
            }
        }
        return c;
    }

}
