import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final HashMap<String, Expr> exprs;

    public Parser(HashMap<String, Expr> exprs) {
        this.exprs = exprs;
    }

    public void parserStr(String str) {
        if (str.contains("add")) {
            add(str);
        } else if (str.contains("sub")) {
            sub(str);
        } else if (str.contains("pos")) {
            pos(str);
        } else if (str.contains("neg")) {
            neg(str);
        } else if (str.contains("mul")) {
            mul(str);
        } else if (str.contains("pow")) {
            pow(str);
        } else if (str.contains("sin")) {
            sin(str);
        } else if (str.contains("cos")) {
            cos(str);
        } else {
            nul(str);
        }
    }

    public ArrayList<Term> parserTerm(String str) {
        String tag = "f[0-9]+";
        Pattern pattern = Pattern.compile(tag);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return exprs.get(str).getTerms();
        } else {
            Term term = new Term(str);
            ArrayList<Term> terms = new ArrayList<>();
            terms.add(term);
            return terms;
        }
    }

    public void add(String str) {
        String[] records = str.split(" ");
        Expr left = new Expr(parserTerm(records[2]));
        Expr right = new Expr(parserTerm(records[3]));
        Operator operator = new Operator(left, right);
        Expr result = new Expr(operator.opAdd());
        exprs.put(records[0], result);
    }

    public void sub(String str) {
        String[] records = str.split(" ");
        Expr left = new Expr(parserTerm(records[2]));
        Expr right = new Expr(parserTerm(records[3]));
        Operator operator = new Operator(left, right);
        Expr result = new Expr(operator.opSub());
        exprs.put(records[0], result);
    }

    public void pos(String str) {
        String[] records = str.split(" ");
        Expr left = new Expr(parserTerm("0"));
        Expr right = new Expr(parserTerm(records[2]));
        Operator operator = new Operator(left, right);
        Expr result = new Expr(operator.opAdd());
        exprs.put(records[0], result);
    }

    public void neg(String str) {
        String[] records = str.split(" ");
        Expr left = new Expr(parserTerm("0"));
        Expr right = new Expr(parserTerm(records[2]));
        Operator operator = new Operator(left, right);
        Expr result = new Expr(operator.opSub());
        exprs.put(records[0], result);
    }

    public void mul(String str) {
        String[] records = str.split(" ");
        Expr left = new Expr(parserTerm(records[2]));
        Expr right = new Expr(parserTerm(records[3]));
        Operator operator = new Operator(left, right);
        Expr result = new Expr(operator.opMul());
        exprs.put(records[0], result);
    }

    public void pow(String str) {
        String[] records = str.split(" ");
        Expr left = new Expr(parserTerm(records[2]));
        Expr right = new Expr(parserTerm(records[3]));
        Operator operator = new Operator(left, right);
        Expr result = new Expr(operator.opPower(Integer.parseInt(records[3])));
        exprs.put(records[0], result);
    }

    public void sin(String str) {
        String[] records = str.split(" ");
        Expr expr = new Expr(parserTerm(records[2]));
        ArrayList<Term> tmp = expr.getNew();
        BigInteger coe;
        BigInteger pow;
        HashMap<Factor, BigInteger> factors = new HashMap<>();
        if (tmp.size() == 0) {  // sin(0) == 0
            coe = new BigInteger("0");
            pow = new BigInteger("0");
        } else if (tmp.size() == 1) {
            BigInteger a = tmp.get(0).getCoefficient();
            if (a.compareTo(BigInteger.ZERO) == -1) {   // sin(-1) == -sin(1)
                BigInteger b = BigInteger.ZERO.subtract(a);
                Term sinTerm = new Term(b, tmp.get(0).getPower(), tmp.get(0).getFactors());
                ArrayList<Term> sinTerms = new ArrayList<>();
                sinTerms.add(sinTerm);
                Expr sinExpr = new Expr(sinTerms);
                Sin sin = new Sin(sinExpr);
                coe = new BigInteger("-1");
                pow = new BigInteger("0");
                factors.put(sin, BigInteger.ONE);
            } else {
                Expr sinExpr = new Expr(tmp);
                Sin sin = new Sin(sinExpr);
                coe = new BigInteger("1");
                pow = new BigInteger("0");
                factors.put(sin, BigInteger.ONE);
            }
        } else {
            Expr sinExpr = new Expr(tmp);
            Sin sin = new Sin(sinExpr);
            coe = new BigInteger("1");
            pow = new BigInteger("0");
            factors.put(sin, BigInteger.ONE);
        }
        Term term = new Term(coe, pow, factors);
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        Expr result = new Expr(terms);
        exprs.put(records[0], result);
    }

    public void cos(String str) {
        String[] records = str.split(" ");
        Expr expr = new Expr(parserTerm(records[2]));
        ArrayList<Term> tmp = expr.getNew();
        BigInteger coe;
        BigInteger pow;
        HashMap<Factor, BigInteger> factors = new HashMap<>();
        if (tmp.size() == 0) {  // cos(0) == 1
            coe = new BigInteger("1");
            pow = new BigInteger("0");
        } else if (tmp.size() == 1) {
            BigInteger a = tmp.get(0).getCoefficient();
            if (a.compareTo(BigInteger.ZERO) == -1) {   // cos(-1) == cos(1)
                BigInteger b = BigInteger.ZERO.subtract(a);
                Term cosTerm = new Term(b, tmp.get(0).getPower(), tmp.get(0).getFactors());
                ArrayList<Term> cosTerms = new ArrayList<>();
                cosTerms.add(cosTerm);
                Expr cosExpr = new Expr(cosTerms);
                Cos cos = new Cos(cosExpr);
                coe = new BigInteger("1");
                pow = new BigInteger("0");
                factors.put(cos, BigInteger.ONE);
            } else {
                Expr cosExpr = new Expr(tmp);
                Cos cos = new Cos(cosExpr);
                coe = new BigInteger("1");
                pow = new BigInteger("0");
                factors.put(cos, BigInteger.ONE);
            }
        } else {
            Expr cosExpr = new Expr(tmp);
            Cos cos = new Cos(cosExpr);
            coe = new BigInteger("1");
            pow = new BigInteger("0");
            factors.put(cos, BigInteger.ONE);
        }
        Term term = new Term(coe, pow, factors);
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        Expr result = new Expr(terms);
        exprs.put(records[0], result);
    }

    public void nul(String str) {
        String[] records = str.split(" ");
        Expr expr = new Expr(parserTerm(records[1]));
        exprs.put(records[0], expr);
    }

}
