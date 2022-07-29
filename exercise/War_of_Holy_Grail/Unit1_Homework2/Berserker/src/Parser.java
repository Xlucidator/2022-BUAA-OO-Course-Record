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
        switch (parserOp(str)) {
            case 1 : add(str);
                     break;
            case 2 : sub(str);
                     break;
            case 3 : pos(str);
                     break;
            case 4 : neg(str);
                     break;
            case 5 : mul(str);
                     break;
            case 6 : pow(str);
                     break;
            case 7 : sin(str);
                     break;
            case 8 : cos(str);
                     break;
            case 9 : cons(str);
                     break;
            default : System.out.println("Wrong");
                     break;
        }
    }

    public Integer parserOp(String str) {
        if (str.contains("add")) {
            return 1;
        } else if (str.contains("sub")) {
            return 2;
        } else if (str.contains("pos")) {
            return 3;
        } else if (str.contains("neg")) {
            return 4;
        } else if (str.contains("mul")) {
            return 5;
        } else if (str.contains("pow")) {
            return 6;
        } else if (str.contains("sin")) {
            return 7;
        } else if (str.contains("cos")) {
            return 8;
        } else {
            return 9;
        }
    }

    public ArrayList<Term> parserTerm(String str) {
        String lable = "f[0-9]+";
        Pattern pattern = Pattern.compile(lable);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return exprs.get(str).getTerms();
        } else {
            Term term = new Term(str);
            ArrayList<Term> list = new ArrayList<>();
            list.add(term);
            return list;
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
        String lable = "f[0-9]+";
        Pattern pattern = Pattern.compile(lable);
        Matcher matcher = pattern.matcher(records[2]);
        if (matcher.find()) {
            Sin sin = new Sin(parserTerm(records[2]));
            BigInteger sinpow = new BigInteger("1");
            HashMap<Factor, BigInteger> factors = new HashMap<>();
            factors.put(sin, sinpow);
            BigInteger coe = new BigInteger("1");
            BigInteger pow = new BigInteger("0");
            Term term = new Term(coe, pow, factors);
            ArrayList<Term> list = new ArrayList<>();
            list.add(term);
            Expr expr = new Expr(list);
            exprs.put(records[0], expr);
        } else if (records[2].equals("x")) {
            Sin sin = new Sin(records[2]);
            BigInteger sinpow = new BigInteger("1");
            HashMap<Factor, BigInteger> factors = new HashMap<>();
            factors.put(sin, sinpow);
            BigInteger coe = new BigInteger("1");
            BigInteger pow = new BigInteger("0");
            Term term = new Term(coe, pow, factors);
            ArrayList<Term> list = new ArrayList<>();
            list.add(term);
            Expr expr = new Expr(list);
            exprs.put(records[0], expr);
        } else {
            BigInteger num = new BigInteger(records[2]);
            Sin sin = new Sin(records[2]);
            if (num.compareTo(BigInteger.ZERO) == 0) {
                Expr expr = new Expr(parserTerm("0"));
                exprs.put(records[0], expr);
            } else if (num.compareTo(BigInteger.ZERO) == -1) {
                BigInteger sinpow = new BigInteger("1");
                HashMap<Factor, BigInteger> factors = new HashMap<>();
                factors.put(sin, sinpow);
                BigInteger coe = new BigInteger("-1");
                BigInteger pow = new BigInteger("0");
                Term term = new Term(coe, pow, factors);
                ArrayList<Term> list = new ArrayList<>();
                list.add(term);
                Expr expr = new Expr(list);
                exprs.put(records[0], expr);
            } else if (num.compareTo(BigInteger.ZERO) == 1) {
                BigInteger sinpow = new BigInteger("1");
                HashMap<Factor, BigInteger> factors = new HashMap<>();
                factors.put(sin, sinpow);
                BigInteger coe = new BigInteger("1");
                BigInteger pow = new BigInteger("0");
                Term term = new Term(coe, pow, factors);
                ArrayList<Term> list = new ArrayList<>();
                list.add(term);
                Expr expr = new Expr(list);
                exprs.put(records[0], expr);
            }
        }
    }

    public void cos(String str) {
        String[] records = str.split(" ");
        String lable = "f[0-9]+";
        Pattern pattern = Pattern.compile(lable);
        Matcher matcher = pattern.matcher(records[2]);
        if (matcher.find()) {
            Cos cos = new Cos(parserTerm(records[2]));
            BigInteger cospow = new BigInteger("1");
            HashMap<Factor, BigInteger> factors = new HashMap<>();
            factors.put(cos, cospow);
            BigInteger coe = new BigInteger("1");
            BigInteger pow = new BigInteger("0");
            Term term = new Term(coe, pow, factors);
            ArrayList<Term> list = new ArrayList<>();
            list.add(term);
            Expr expr = new Expr(list);
            exprs.put(records[0], expr);
        } else if (records[2].equals("x")) {
            Cos cos = new Cos(records[2]);
            BigInteger cospow = new BigInteger("1");
            HashMap<Factor, BigInteger> factors = new HashMap<>();
            factors.put(cos, cospow);
            BigInteger coe = new BigInteger("1");
            BigInteger pow = new BigInteger("0");
            Term term = new Term(coe, pow, factors);
            ArrayList<Term> list = new ArrayList<>();
            list.add(term);
            Expr expr = new Expr(list);
            exprs.put(records[0], expr);
        } else {
            BigInteger num = new BigInteger(records[2]);
            if (num.compareTo(BigInteger.ZERO) == 0) {
                Expr expr = new Expr(parserTerm("1"));
                exprs.put(records[0], expr);
            } else {
                Cos cos = new Cos(records[2]);
                BigInteger cospow = new BigInteger("1");
                HashMap<Factor, BigInteger> factors = new HashMap<>();
                factors.put(cos, cospow);
                BigInteger coe = new BigInteger("1");
                BigInteger pow = new BigInteger("0");
                Term term = new Term(coe, pow, factors);
                ArrayList<Term> list = new ArrayList<>();
                list.add(term);
                Expr expr = new Expr(list);
                exprs.put(records[0], expr);
            }
        }
    }

    public void cons(String str) {
        String[] records = str.split(" ");
        Expr expr = new Expr(parserTerm(records[1]));
        exprs.put(records[0], expr);
    }

}
