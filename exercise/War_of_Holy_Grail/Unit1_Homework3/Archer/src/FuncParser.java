import expr.Func;
import expr.Term;
import expr.Factor;
import expr.Number;
import expr.Sin;
import expr.Cos;

import java.math.BigInteger;

public class FuncParser {
    private final Lexer lexer;
    private Func ff;
    private final String[] funcParam;

    public FuncParser(Lexer lexer, Func f, String[] funcParam) {
        this.lexer = lexer;
        this.ff = f;
        this.funcParam = funcParam;
    }

    public Func getResult() {
        Func f1 = parseFunc();
        ff.copySignedTerm(f1);
        return ff;
    }

    public Func parseFunc() {
        Func func = new Func();
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
        func.addsignedTerm(sign, parseTerm());

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            sign = lexer.peek();
            lexer.next();
            func.addsignedTerm(sign, parseTerm());
        }

        return func;
    }

    public Term parseTerm() {
        Term term = new Term();
        String sign;

        if (lexer.peek().equals("-")) {
            term.changePnSign("-");
            lexer.next();
        } else if (lexer.peek().equals("+")) {
            lexer.next();
        }
        term.addFactor(parseParam());

        while (lexer.peek().equals("*") || lexer.peek().equals("**")) {
            term.addSign(lexer.peek());
            lexer.next();
            term.addFactor(parseParam());
        }

        return term;
    }

    public Factor parseParam() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Func func = parseFunc();
            lexer.next();
            return func;
        } else if (lexer.peek().equals(funcParam[0])) {
            lexer.next();
            return ff.getParam(0);
        } else if (funcParam.length > 1 && lexer.peek().equals(funcParam[1])) {
            lexer.next();
            return ff.getParam(1);
        } else if (funcParam.length > 2 && lexer.peek().equals(funcParam[2])) {
            lexer.next();
            return ff.getParam(2);
        } else if (lexer.peek().equals("sin")) {
            Sin sin = new Sin();
            lexer.next();
            lexer.next();
            Func func = parseFunc();
            sin.addExpr(func);
            lexer.next();
            return sin;
        } else if (lexer.peek().equals("cos")) {
            Cos cos = new Cos();
            lexer.next();
            lexer.next();
            //跳过了sin和'('
            Func func = parseFunc();
            cos.addExpr(func);
            lexer.next();
            //跳过')'
            return cos;
        }
        else {
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


