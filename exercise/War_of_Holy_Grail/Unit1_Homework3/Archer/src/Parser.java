import expr.Func;
import expr.Term;
import expr.Factor;
import expr.Number;
import expr.Sin;
import expr.Cos;
import expr.Expr;
import expr.VariableX;
import expr.Sum;
import expr.Param;

import java.math.BigInteger;
import java.util.HashMap;

public class Parser {
    private Lexer lexer;
    private HashMap<String, String> funcMap = new HashMap<>();

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Parser(Lexer lexer, HashMap<String, String> funcMap) {
        this.lexer = lexer;
        this.funcMap = funcMap;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        String sign;

        if (lexer.peek().equals("-")) {
            sign = lexer.peek();
            lexer.next();
        } else if (lexer.peek().equals("+")) {
            sign = lexer.peek();
            lexer.next();
        } else {
            sign = "+";
        }
        expr.addsignedTerm(sign, parseTerm());

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            sign = lexer.peek();
            lexer.next();
            expr.addsignedTerm(sign, parseTerm());
        }

        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();

        if (lexer.peek().equals("-")) {
            term.changePnSign("-");
            lexer.next();
        } else if (lexer.peek().equals("+")) {
            lexer.next();
        }
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*") || lexer.peek().equals("**")) {
            term.addSign(lexer.peek());
            lexer.next();
            term.addFactor(parseFactor());
        }

        return term;
    }

    public Sum parseSum() {
        lexer.next4();
        //跳过"sum"、‘('、'i'、’，‘
        Sum sum = new Sum();
        sum.setLower((Number) parseFactor());
        lexer.next();
        sum.setUpper((Number) parseFactor());
        lexer.next();
        //Expr expr = parseExpr();
        //sum.copySignedTerm(expr);
        sum = new SumParser(lexer, sum).getResult();

        lexer.next();
        //跳过')'
        return sum;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();
            return expr;
        } else if (lexer.peek().equals("x")) {
            lexer.next();
            return new VariableX();
        } else if (lexer.peek().equals("sum")) {
            return parseSum();
        } else if (lexer.peek().equals("i")) {
            lexer.next();
            return new Param();
        } else if (lexer.peek().equals("sin")) {
            Sin sin = new Sin();
            lexer.next2();
            //跳过了sin和'('
            Expr expr = parseExpr();
            sin.addExpr(expr);
            lexer.next();
            //跳过')'
            return sin;
        } else if (lexer.peek().equals("cos")) {
            Cos cos = new Cos();
            lexer.next2();
            //跳过了sin和'('
            Expr expr = parseExpr();
            cos.addExpr(expr);
            lexer.next();
            //跳过')'
            return cos;
        } else if (lexer.peek().equals("f") || lexer.peek().equals("g")
                || lexer.peek().equals("h")) {
            String funcExpr = funcMap.get(lexer.peek());

            Func f = new Func();

            Lexer lexer1 = new Lexer(PreProcess.getFunction(funcExpr));
            FuncParser funcParser = new FuncParser(lexer1, f, PreProcess.getFuncParam(funcExpr));
            f = funcParser.getResult();

            lexer.next();
            lexer.next();
            //跳过了函数符号和'('

            for (int i = 0; i < 3; i++) {
                Expr expr = parseExpr();
                f.setParam(i, expr);
                if (lexer.peek().equals(")")) {
                    lexer.next();
                    break;
                }
                lexer.next();
            }
            return f;
        } else {
            String flag = "+";
            if (lexer.peek().equals("+") || lexer.peek().equals("-")) {
                if (lexer.peek().equals("-")) {
                    flag = "-";
                }
                lexer.next();
            }
            BigInteger num = new BigInteger(flag + lexer.peek());
            lexer.next();
            return new Number(num);
        }
    }
}
