package parse;

import func.Func;
import func.SelfFunc;
import expression.Expression;
import expression.Factor;
import expression.TriFunc;
import expression.Constant;
import expression.Term;
import expression.Power;

import java.math.BigInteger;
import java.util.ArrayList;

public class Parse {
    private final Lexer lexer;
    private final SelfFunc selfFunc;

    public Parse(Lexer lexer, SelfFunc selfFunc) {
        this.lexer = lexer;
        this.selfFunc = selfFunc;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expression = parseExpr();
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                expression.setAllPow(Integer.parseInt(lexer.peek()));
                lexer.next();
            }
            return expression;
        } else if (lexer.peek().equals("x")) {
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                int pow = Integer.parseInt(lexer.peek());
                lexer.next();
                return new Power(pow);
            } else {
                return new Power(1);
            }
        } else if (lexer.peek().equals("-") | lexer.peek().equals("+")) {
            String symbol = lexer.peek();
            lexer.next();
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Constant(num, symbol);
        } else if (lexer.peek().equals("f") | lexer.peek().equals("g") | lexer.peek().equals("h")) {
            String s = this.funcRefer();
            Lexer lexer1 = new Lexer(s);
            Parse parse1 = new Parse(lexer1, this.selfFunc);
            return parse1.parseExpr();
        } else if (lexer.peek().equals("sum")) {
            String s = this.sum();
            Lexer lexer1 = new Lexer(s);
            Parse parse1 = new Parse(lexer1, this.selfFunc);
            return parse1.parseExpr();
        } else if (lexer.peek().equals("cos") | lexer.peek().equals("sin")) {
            final String name = lexer.peek();
            lexer.next();   //"("
            lexer.next();
            Factor factor = parseFactor();
            lexer.next();
            int pow = 1;
            if (lexer.peek().equals("**")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                pow = Integer.parseInt(lexer.peek());
                lexer.next();   //下一个，这个三角函数解析完毕
            }
            return new TriFunc(name, pow, factor);
        } else {
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Constant(num, "+");
        }
    }

    public Term parseTerm(Factor factor, boolean useFactor) {
        Term term = new Term();
        if (useFactor) {
            term.addFactor(factor);
        }
        Factor factor1 = parseFactor();
        for (int i = 0; i < factor1.getAllPow(); i++) {
            term.addFactor(factor1.copy());
        }
        while (lexer.peek().equals("*")) {
            lexer.next();
            Factor factor2 = parseFactor();
            for (int i = 0; i < factor2.getAllPow(); i++) {
                term.addFactor(factor2.copy());
            }
        }
        return term;
    }

    public Expression parseExpr() {
        Expression expression = new Expression();
        Factor constant = new Constant(BigInteger.valueOf(1), "-");
        if (lexer.peek().equals("+")) {
            lexer.next();
            expression.addTerm(parseTerm(constant, false));
        } else if (lexer.peek().equals("-")) {
            lexer.next();
            expression.addTerm(parseTerm(constant, true));
        } else {
            expression.addTerm(parseTerm(constant, false));
        }
        while (lexer.peek().equals("+") | lexer.peek().equals("-")) {
            Factor constant1 = new Constant(BigInteger.valueOf(1), "-");
            if (lexer.peek().equals("+")) {
                lexer.next();
                expression.addTerm(parseTerm(constant1, false));
            } else {
                lexer.next();
                expression.addTerm(parseTerm(constant1, true));
            }
        }
        return expression;
    }

    public String funcRefer() {
        for (Func func : selfFunc.getFuncs()) {
            if (func.getName().equals(lexer.peek())) {
                String result = func.getRelation();
                ArrayList<String> para = func.getPara();
                lexer.next();   //"("
                int j = -1;
                for (String item : para) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("(");
                    lexer.next();   //开始一个参数（跳过括号以及逗号）
                    if (lexer.peek().equals("(")) {
                        j--;
                    } else if (lexer.peek().equals(")")) {
                        j++;
                    }
                    while (!lexer.peek().equals(",") & j != 0) {
                        sb.append(lexer.peek());
                        lexer.next();   //继续该参数
                        if (lexer.peek().equals("(")) {
                            j--;
                        } else if (lexer.peek().equals(")")) {
                            j++;
                        }
                    }
                    sb.append(")");
                    result = result.replaceAll(item, sb.toString());
                }
                lexer.next();   //跳过了函数调用结尾的右括号
                return "(" + result + ")";
            }
        }
        return null;
    }

    public String sum() {
        lexer.next();   //"("
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        lexer.next();   //"i"
        lexer.next();   //","
        lexer.next();
        Factor begin = parseFactor();
        lexer.next();   //跳过”，“
        final Factor end = parseFactor();
        lexer.next();   //","
        StringBuilder sb1 = new StringBuilder();
        int j = 0;
        while (!lexer.peek().equals(")") | j != 0) {
            if (lexer.peek().equals("(")) {
                j--;
            } else if (lexer.peek().equals(")")) {
                j++;
            }
            if (lexer.peek().equals("i")) {
                sb1.append("j");
            } else {
                sb1.append(lexer.peek());
            }
            lexer.next();
        }
        lexer.next();   //右括号的下一个，完成sum的全部解析
        String exp = sb1.toString();
        if (((Constant) begin).compare((Constant) end) == 1) {
            return "0";
        } else {
            sb.append(exp.replaceAll("j", begin.toString()));
            ((Constant) begin).add1();
            while (((Constant) begin).compare((Constant) end) != 1) {
                sb.append("+");
                sb.append(exp.replaceAll("j", begin.toString()));
                ((Constant) begin).add1();
            }
            sb.append(")");
            return sb.toString();
        }
    }
}
