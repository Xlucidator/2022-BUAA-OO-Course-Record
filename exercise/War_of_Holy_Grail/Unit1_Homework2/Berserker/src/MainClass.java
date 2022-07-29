import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.ParsedMode);
        int n = scanner.getCount();
        ArrayList<String> exprs = new ArrayList<>();
        HashMap<String, Expr> lexprs = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String curExpr = scanner.readLine();
            Parser parser = new Parser(lexprs);
            exprs.add(curExpr);
            parser.parserStr(curExpr);
        }
        String str = exprs.get(n - 1);
        String[] temp = str.split(" ");
        Expr expr = lexprs.get(temp[0]);
        ArrayList<Term> termspos = new ArrayList<>();
        ArrayList<Term> termsneg = new ArrayList<>();
        for (Term key : expr.getTerms()) {
            if (key.getCoefficient().compareTo(BigInteger.ZERO) == 1) {
                termspos.add(key);  // pos
            } else if (key.getCoefficient().compareTo(BigInteger.ZERO) == -1) {
                termsneg.add(key);  // neg
            }
        }
        if (termspos.size() == 0 && termsneg.size() == 0) {
            System.out.println(0);
        } else {
            StringBuilder p = new StringBuilder(10005);
            outpos(termspos, p);
            outneg(termsneg, p);
            char c = p.charAt(0);
            if (c == '*') {
                p.deleteCharAt(0);
            }
            String t = p.toString();
            t = t.replaceAll("\\+\\*","+");
            t = t.replaceAll("-\\*","-");
            System.out.print(t);
        }
    }

    public static void print(HashMap<Factor, BigInteger> factors, StringBuilder p) {
        if (factors.size() != 0) {      // map is empty , null
            for (Factor key : factors.keySet()) {
                if (factors.get(key).compareTo(BigInteger.ZERO) != 0) {
                    if (key.getPos() == 1) {
                        if (key.getPower().compareTo(BigInteger.ZERO) == 0) {
                            p.append("*sin(");
                            String str = String.valueOf(key.getCoefficient());
                            p.append(str);
                            p.append(")");
                        } else if (key.getPower().compareTo(BigInteger.ONE) == 0) {
                            p.append("*sin(x)");
                        } else {
                            p.append("*sin(x**");
                            String str = String.valueOf(key.getPower());
                            p.append(str);
                            p.append(")");
                        }
                    } else if (key.getPos() == 2) {
                        if (key.getPower().compareTo(BigInteger.ZERO) == 0) {
                            p.append("*cos(");
                            String str = String.valueOf(key.getCoefficient());
                            p.append(str);
                            p.append(")");
                        } else if (key.getPower().compareTo(BigInteger.ONE) == 0) {
                            p.append("*cos(x)");
                        } else {
                            p.append("*cos(x**");
                            String str = String.valueOf(key.getPower());
                            p.append(str);
                            p.append(")");
                        }
                    }
                    if (factors.get(key).compareTo(BigInteger.ONE) != 0) {
                        p.append("**");
                        String str = String.valueOf(factors.get(key));
                        p.append(str);
                    }
                }
            }
        }
    }

    public static boolean judge(HashMap<Factor, BigInteger> factors) {
        boolean a = true;
        for (Factor key : factors.keySet()) {
            if (factors.get(key).compareTo(BigInteger.ZERO) != 0) {
                a = false;
            }
        }
        return a;
    }

    public static void outpos(ArrayList<Term> termspos, StringBuilder p) {
        BigInteger two = new BigInteger("2");
        int i = 0;
        for (Term pos : termspos) {
            if (i != 0) {
                p.append("+");
            }
            if (pos.getCoefficient().compareTo(BigInteger.ONE) == 0) {      // 1*x**
                if (pos.getPower().compareTo(BigInteger.ZERO) == 0) {       // 1*x**0*
                    if (judge(pos.getFactors())) {
                        String str = String.valueOf(pos.getCoefficient());
                        p.append(str);
                    }
                } else if (pos.getPower().compareTo(BigInteger.ONE) == 0) {     // 1*x**1*
                    p.append("x");
                } else if (pos.getPower().compareTo(two) == 0) {    // 1*x**2*
                    p.append("x*x");
                } else {
                    p.append("x**");
                    String str = String.valueOf(pos.getPower());
                    p.append(str);
                }
            } else {
                String str = String.valueOf(pos.getCoefficient());
                if (pos.getPower().compareTo(BigInteger.ZERO) == 0) {       // e.g. 2*x**0*
                    p.append(str);
                } else if (pos.getPower().compareTo(BigInteger.ONE) == 0) {
                    p.append(str);
                    p.append("*x");
                } else if (pos.getPower().compareTo(two) == 0) {
                    p.append(str);
                    p.append("*x*x");
                } else {
                    p.append(str);
                    p.append("*x**");
                    String pow = String.valueOf(pos.getPower());
                    p.append(pow);
                }
            }
            print(pos.getFactors(), p);        // print Tri
            i++;
        }
    }

    public static void outneg(ArrayList<Term> termsneg, StringBuilder p) {
        BigInteger two = new BigInteger("2");
        for (Term neg : termsneg) {
            if (neg.getCoefficient().compareTo(BigInteger.valueOf(-1)) == 0) {
                if (neg.getPower().compareTo(BigInteger.ZERO) == 0) {
                    if (judge(neg.getFactors())) {
                        String coe = String.valueOf(neg.getCoefficient());
                        p.append(coe);
                    } else {
                        p.append("-");
                    }
                } else if (neg.getPower().compareTo(BigInteger.ONE) == 0) {
                    p.append("-x");
                } else if (neg.getPower().compareTo(two) == 0) {
                    p.append("-x*x");
                } else {
                    String pow = String.valueOf(neg.getPower());
                    p.append("-x**");
                    p.append(pow);
                }
            } else {
                String coe = String.valueOf(neg.getCoefficient());
                if (neg.getPower().compareTo(BigInteger.ZERO) == 0) {
                    p.append(coe);
                } else if (neg.getPower().compareTo(BigInteger.ONE) == 0) {
                    p.append(coe);
                    p.append("*x");
                } else if (neg.getPower().compareTo(two) == 0) {
                    p.append(coe);
                    p.append("*x*x");
                } else {
                    p.append(coe);
                    p.append("*x**");
                    String pow = String.valueOf(neg.getPower());
                    p.append(pow);
                }
            }
            print(neg.getFactors(), p);
        }
    }
}

