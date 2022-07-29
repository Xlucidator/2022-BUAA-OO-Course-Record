import expr.Term;
import expr.Factor;
import expr.Number;
import expr.Sin;
import expr.Cos;
import expr.VariableX;
import expr.Sum;

import java.math.BigInteger;

public class SumParser {
    private Lexer lexer;
    private Sum sum;

    public SumParser(Lexer lexer, Sum sum) {
        this.lexer = lexer;
        this.sum = sum;
    }

    public Sum getResult() {
        Sum sum1 = parseSum();
        sum.copySignedTerm(sum1);
        return sum;
    }

    public Sum parseSum() {
        Sum sum = new Sum();
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
        sum.addsignedTerm(sign, parseTerm());

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            sign = lexer.peek();
            lexer.next();
            sum.addsignedTerm(sign, parseTerm());
        }

        return sum;
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
            Sum sum = parseSum();
            lexer.next();
            return sum;
        } else if (lexer.peek().equals("x")) {
            VariableX x = new VariableX();
            lexer.next();
            return x;
        } else if (lexer.peek().equals("i")) {
            lexer.next();
            return sum.getParam();
        } else if (lexer.peek().equals("sin")) {
            Sin sin = new Sin();
            lexer.next();
            lexer.next();
            Sum sum = parseSum();
            sin.addExpr(sum);
            lexer.next();
            return sin;
        } else if (lexer.peek().equals("cos")) {
            Cos cos = new Cos();
            lexer.next();
            lexer.next();
            //跳过了sin和'('
            Sum sum = parseSum();
            cos.addExpr(sum);
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
