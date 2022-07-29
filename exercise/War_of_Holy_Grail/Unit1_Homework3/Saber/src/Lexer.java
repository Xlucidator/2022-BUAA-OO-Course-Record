import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    private ArrayList<Term> termsPos;
    private ArrayList<Term> termsNeg;

    public Lexer(Expr expr) {
        ArrayList<Term> termsPos = new ArrayList<>();
        ArrayList<Term> termsNeg = new ArrayList<>();
        for (Term key : expr.getTerms()) {
            if (key.getCoefficient().compareTo(BigInteger.ZERO) == 1) {
                termsPos.add(key);  // pos 系数为正
            } else if (key.getCoefficient().compareTo(BigInteger.ZERO) == -1) {
                termsNeg.add(key);  // neg 系数为负
            }
        }
        this.termsPos = termsPos;
        this.termsNeg = termsNeg;
    }

    public boolean judge(HashMap<Factor, BigInteger> factors) {
        boolean a = true;
        for (Factor key : factors.keySet()) {
            if (factors.get(key).compareTo(BigInteger.ZERO) != 0) {
                a = false;
            }
        }
        return a;   // 三角的指数全为0，返回true
    }

    public String out() {
        if (termsPos.size() == 0 && termsNeg.size() == 0) {
            String t = "0";
            return t;
        } else {
            StringBuilder p = new StringBuilder(10005);
            outPos(termsPos, p);
            outNeg(termsNeg, p);
            char c = p.charAt(0);
            if (c == '*') {
                p.deleteCharAt(0);
            }
            String t = p.toString();
            t = t.replaceAll("\\+\\*","+");
            t = t.replaceAll("-\\*","-");
            return t;
        }
    }

    public void outPos(ArrayList<Term> termsPos, StringBuilder print) {
        int i = 0;
        for (Term pos : termsPos) {
            if (i != 0) {
                print.append("+");
            }
            if (pos.getCoefficient().compareTo(BigInteger.ONE) == 0) {      // 1*x**
                if (pos.getPower().compareTo(BigInteger.ZERO) == 0) {       // 1*x**0*
                    if (judge(pos.getFactors())) {
                        String str = String.valueOf(pos.getCoefficient());
                        print.append(str);
                    }
                } else if (pos.getPower().compareTo(BigInteger.ONE) == 0) {     // 1*x**1*
                    print.append("x");
                } else {
                    print.append("x**");
                    String str = String.valueOf(pos.getPower());
                    print.append(str);
                }
            } else {
                String str = String.valueOf(pos.getCoefficient());
                if (pos.getPower().compareTo(BigInteger.ZERO) == 0) {       // e.g. 2*x**0*
                    print.append(str);
                } else if (pos.getPower().compareTo(BigInteger.ONE) == 0) {
                    print.append(str);
                    print.append("*x");
                } else {
                    print.append(str);
                    print.append("*x**");
                    String pow = String.valueOf(pos.getPower());
                    print.append(pow);
                }
            }
            outTri(pos.getFactors(), print);        // print Tri
            i++;
        }
    }

    public void outNeg(ArrayList<Term> termsNeg, StringBuilder print) {
        for (Term neg : termsNeg) {
            if (neg.getCoefficient().compareTo(BigInteger.valueOf(-1)) == 0) {
                if (neg.getPower().compareTo(BigInteger.ZERO) == 0) {
                    if (judge(neg.getFactors())) {
                        String coe = String.valueOf(neg.getCoefficient());
                        print.append(coe);
                    } else {
                        print.append("-");
                    }
                } else if (neg.getPower().compareTo(BigInteger.ONE) == 0) {
                    print.append("-x");
                } else {
                    String pow = String.valueOf(neg.getPower());
                    print.append("-x**");
                    print.append(pow);
                }
            } else {
                String coe = String.valueOf(neg.getCoefficient());
                if (neg.getPower().compareTo(BigInteger.ZERO) == 0) {
                    print.append(coe);
                } else if (neg.getPower().compareTo(BigInteger.ONE) == 0) {
                    print.append(coe);
                    print.append("*x");
                } else {
                    print.append(coe);
                    print.append("*x**");
                    String pow = String.valueOf(neg.getPower());
                    print.append(pow);
                }
            }
            outTri(neg.getFactors(), print);
        }
    }

    public  void outTri(HashMap<Factor, BigInteger> factors, StringBuilder print) {
        if (factors.size() != 0) {      // map is empty , null
            for (Factor key : factors.keySet()) {
                if (factors.get(key).compareTo(BigInteger.ZERO) != 0) {
                    if (key.getPos() == 1) {
                        print.append("*sin(");
                        if (key.getExpr().type() == 1) {
                            print.append("(");
                        }
                        Lexer lexer = new Lexer(key.getExpr());
                        print.append(lexer.out());
                        if (key.getExpr().type() == 1) {
                            print.append(")");
                        }
                        print.append(")");
                    } else if (key.getPos() == 2) {
                        print.append("*cos(");
                        if (key.getExpr().type() == 1) {
                            print.append("(");
                        }
                        Lexer lexer = new Lexer(key.getExpr());
                        print.append(lexer.out());
                        if (key.getExpr().type() == 1) {
                            print.append(")");
                        }
                        print.append(")");
                    }
                    if (factors.get(key).compareTo(BigInteger.ONE) != 0) {
                        print.append("**");
                        String str = String.valueOf(factors.get(key));
                        print.append(str);
                    }
                }
            }
        }
    }

}
