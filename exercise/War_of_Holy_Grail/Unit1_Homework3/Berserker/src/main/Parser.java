package main;

import factor.Calculator;
import factor.Expr;
import factor.Factor;
import factor.Term;
import factor.Var;
import factor.function.Sum;
import factor.function.Triangle;
import factor.Number;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;
    private int sign = 1;
    private final FunctionBase functionBase;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.functionBase = new FunctionBase();
    }

    public Parser(Lexer lexer, FunctionBase functionBase) {
        this.lexer = lexer;
        this.functionBase = functionBase;
    }

    public Expr parseExpr()
            throws IOException, ClassNotFoundException {
        // expr表达式，里面是term项，记录方式为HashMap<Poly, BigInteger> expr
        Expr expr = new Expr();
        sign = 1;
        while ("+-".contains(lexer.peek())) {
            if (lexer.peek().equals("-")) {
                sign = -sign;
            }
            lexer.next();
        }
        expr.mergeTerms(parseTerm());

        while ("+-".contains(lexer.peek())) {
            sign = 1;
            while ("+-".contains(lexer.peek())) {
                if (lexer.peek().equals("-")) {
                    sign = -sign;
                }
                lexer.next();
            }
            expr.mergeTerms(parseTerm());
        }
        return expr;
    }

    public Term parseTerm()
            throws IOException, ClassNotFoundException {  // term项，里面是factor因子
        // (包括自定义函数因子转化为表达式因子，表达式因子，数字因子，幂函数因子，三角函数因子)
        Term term = new Term(sign);
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor()
            throws IOException, ClassNotFoundException {
        // factor因子，需要处理自定义函数因子转化为表达式因子，表达式因子，数字因子，幂函数因子，三角函数因子
        if (lexer.peek().equals("(")) {  // 表达式因子
            lexer.next();
            Expr expr = parseExpr();  // 表达式因子解析表达式
            lexer.next();
            if (lexer.peek().equals("**")) {
                Calculator calculator = new Calculator();
                return new Expr(calculator.power(expr, getDegree()));
            }
            return expr;
        } else if ("jklix".contains(lexer.peek())) {
            int index = "jklix".indexOf(lexer.peek());
            lexer.next();
            if (lexer.peek().equals("**")) {
                return new Var(index, getDegree());
            }
            return new Var(index, new BigInteger("1"));
        } else if (lexer.peek().equals("sin") || lexer.peek().equals("cos")) {
            String name = lexer.peek();
            lexer.nextInt(2);
            Expr expr = parseExpr();  // 表达式因子解析表达式
            lexer.next();
            if (lexer.peek().equals("**")) {
                return new Triangle(name, expr, getDegree());
            }
            return new Triangle(name, expr, new BigInteger("1"));
        } else if (lexer.peek().equals("sum")) {
            lexer.nextInt(4);
            BigInteger min = getSignedNum();
            lexer.nextInt(2);
            BigInteger max = getSignedNum();
            lexer.nextInt(2);
            Expr expr = parseExpr();
            lexer.next();
            Sum sum = new Sum(min, max, expr);
            return sum.getRes();
        } else if ("fgh".contains(lexer.peek())) {
            final String name = lexer.peek();
            lexer.next();
            ArrayList<Factor> factors = new ArrayList<>();
            while (!lexer.peek().equals(")")) {
                lexer.next();
                factors.add(parseFactor());
            }
            while (factors.size() < 5) {
                factors.add(null);
            }
            lexer.next();
            return functionBase.getFunction(name).setVar(factors);
        } else {  // 数字因子
            BigInteger num = getSignedNum();
            lexer.next();
            if (lexer.peek().equals("**")) {
                return new Number(num, getDegree());
            }
            return new Number(num);
        }
    }

    public BigInteger getSignedNum() {
        int sign = 1;
        if ("+-".contains(lexer.peek())) {
            if ("-".equals(lexer.peek())) {
                sign = -1;
            }
            lexer.next();
        }
        BigInteger res = new BigInteger(lexer.peek());
        return res.multiply(new BigInteger(String.valueOf(sign)));
    }

    public BigInteger getDegree() {
        lexer.next();
        BigInteger degree = getSignedNum();
        lexer.next();
        return degree;
    }
}
