import polynomial.Polynomial;
import polynomial.Power;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) { this.lexer = lexer; }

    public Polynomial parsePoly() {
        //poly内相加，res保存和
        Polynomial res = new Polynomial();
        boolean sign = false;
        if (lexer.getToken().matches("[+-]")) {
            sign = lexer.getToken().equals("-");
            lexer.next();
        }
        Polynomial appender = parseTerm();
        res = sign ? res.sub(appender) : res.add(appender);
        while (lexer.getToken().matches("[+-]")) { //加减号
            sign = lexer.getToken().equals("-");
            lexer.next(); //过+-
            appender = parseTerm();
            res = sign ? res.sub(appender) : res.add(appender);
        }
        return res;
    }

    public Polynomial parseTerm() {
        //term内相乘 res保存乘积
        Polynomial res = new Polynomial(new Power(BigInteger.ZERO, BigInteger.ONE));
        res = res.mul(parseFactor());
        while (lexer.getToken().equals("*")) {
            lexer.next();
            res = res.mul(parseFactor());
        }
        return res;
    }

    public Polynomial parseFactor() {
        if (lexer.getToken().equals("(")) {   //表达式因子
            lexer.next(); //过（
            Polynomial buffer = parsePoly();
            lexer.next(); //过 ）
            if (lexer.getToken().equals("^")) {
                lexer.next();
                BigInteger index = parseNum();
                return buffer.power(index);
            } else {
                return buffer;
            }
        } else if (lexer.getToken().equals("x")) {  //幂函数因子
            lexer.next();  //过x
            BigInteger index;
            if (lexer.getToken().equals("^")) {
                lexer.next(); //过^
                index = parseNum();
            } else {
                index = BigInteger.ONE;
            }
            return new Polynomial(new Power(index, BigInteger.ONE));
        } else { //数字因子
            return new Polynomial(new Power(BigInteger.ZERO, parseNum()));
        }
    }

    public BigInteger parseNum() {
        BigInteger ret;
        //符号
        boolean sign = false;
        if (lexer.getToken().equals("-")) {
            sign = true;
            lexer.next();
        } else if (lexer.getToken().equals("+")) {
            lexer.next();
        }
        //数字，支持简写
        if (lexer.getToken().matches("\\d+")) {
            ret = new BigInteger(lexer.getToken());
            lexer.next();
        } else {
            //只有正负号
            ret = BigInteger.ONE;
        }
        return sign ? ret.negate() : ret;
    }
}
