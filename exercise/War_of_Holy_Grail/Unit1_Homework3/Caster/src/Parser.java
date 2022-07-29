import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class Parser {
    private final Lexer lexer;
    private final ArrayList<Custom> funcs;

    public Parser(Lexer lexer, ArrayList<Custom> funcs) {
        this.lexer = lexer;
        this.funcs = funcs;
    }

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.funcs = null;
    }

    public Custom parseFunc() {
        Matcher matcher = lexer.findPattern("^([fgh])\\(([xyz])(,([xyz]))?(,([xyz]))?\\)=(.*)$");
        matcher.find();
        ArrayList<String> param = new ArrayList<>();
        param.add(matcher.group(2));
        if (matcher.group(4) != null) {
            param.add(matcher.group(4));
        }
        if (matcher.group(6) != null) {
            param.add(matcher.group(6));
        }
        while (!lexer.peek().equals("=")) {
            lexer.next();
        }
        lexer.next();
        Custom func = new Custom(matcher.group(1), param, parseExpr());
        return func;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        if (lexer.findPattern("^[+-]{3}[0-9]|^[+-]{2}[\\(xyziscfgh]").find() == true) {
            if (lexer.peek().equals("-"))
            {
                expr.addOp("-");
                lexer.next();
            }
            else if (lexer.peek().equals("+")) {
                expr.addOp("+");
                lexer.next();
            }
        }
        else {
            expr.addOp("+");
        }

        expr.addTerm(parseTerm());
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            expr.addOp(lexer.peek());
            lexer.next();
            expr.addTerm(parseTerm());
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        if (lexer.findPattern("^[+-]{2}[0-9]|^[+-][\\(xyziscfgh]").find() == true) {
            if (lexer.peek().equals("-"))
            {
                term.setCoef(BigInteger.valueOf(-1));
                lexer.next();
            }
            else if (lexer.peek().equals("+"))
            {
                term.setCoef(BigInteger.valueOf(1));
                lexer.next();
            }
        }
        else {
            term.setCoef(BigInteger.valueOf(1));
        }
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();
            BigInteger exp = parseExp();
            ((Expr) expr).addExp(exp);
            return expr;
        }
        else if ("xyzi".indexOf(lexer.peek().charAt(0)) != -1) {
            String name = lexer.peek();
            lexer.next();
            BigInteger exp = parseExp();
            Factor var = new Variable(exp, name);
            return var;
        }
        else if (lexer.peekN(3).equals("cos")) {
            return parserCos();
        }
        else if (lexer.peekN(3).equals("sin")) {
            return parserSin();
        }
        else if (lexer.peekN(3).equals("sum")) {
            return parserSum();
        }
        else if ("fgh".indexOf(lexer.peek().charAt(0)) != -1) {
            return parserFunc();
        }
        else {
            BigInteger coef = new BigInteger("1");
            if (lexer.peek().equals("-"))
            {
                coef = BigInteger.valueOf(-1);
                lexer.next();
            }
            else if (lexer.peek().equals("+"))
            {
                lexer.next();
            }
            BigInteger num = new BigInteger(lexer.peek()).multiply(coef);
            lexer.next();
            return new Number(num);
        }
    }

    public BigInteger parseExp() {
        if (lexer.peekN(2).equals("**")) {
            lexer.next();
            lexer.next();
            if (lexer.peek().equals("+"))
            {
                lexer.next();
            }
            BigInteger exp = new BigInteger(lexer.peek());
            lexer.next();
            return exp;
        }
        return BigInteger.valueOf(1);
    }

    public Sin parserSin() {
        lexer.nextN(4);
        Factor fac = parseFactor();
        lexer.next();
        BigInteger exp = parseExp();
        return new Sin(exp, fac);
    }

    public Cos parserCos() {
        lexer.nextN(4);
        Factor fac = parseFactor();
        lexer.next();
        BigInteger exp = parseExp();
        return new Cos(exp, fac);
    }

    public Sum parserSum() {
        lexer.nextN(6);
        Number s = (Number) parseFactor();
        lexer.next();
        Number e = (Number) parseFactor();
        lexer.next();
        Expr expr = parseExpr();
        Sum sum = new Sum(s.getCoef(), e.getCoef(), expr);
        lexer.next();
        return sum;
    }

    public Expr parserFunc() {
        for (Custom func: funcs) {
            if (func.getName().equals(lexer.peek())) {
                lexer.nextN(2);
                ArrayList<Expr> args = new ArrayList<>();
                args.add(parseExpr());
                for (int i = 1; i < func.getLen(); i++) {
                    lexer.next();
                    args.add(parseExpr());
                }
                lexer.next();
                Expr newExpr = func.getDef().copy();
                func.getDef().replace(newExpr, args, func.getParam());
                return newExpr;
            }
        }
        return null;
    }
}
