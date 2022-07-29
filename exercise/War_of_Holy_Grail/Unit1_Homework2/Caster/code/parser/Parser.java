package parser;

import expr.Function;
import expr.Constant;
import expr.Cos;
import expr.Expr;
import expr.Factor;
import expr.Sin;
import expr.Sum;
import expr.Term;
import expr.Var;

import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private Lexer lexer;
    
    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    
    private boolean handleSig() {
        boolean isPos = true;
        while (lexer.getToken().equals("+") ||
                lexer.getToken().equals("-")) {
            if (lexer.getToken().equals("-")) {
                isPos = !isPos;
            }
            lexer.next();
        }
        return isPos;
    }
    
    //读入一个表达式
    public Expr consumeExpr() {
        Expr expr = new Expr();
        //第一项，如果碰到加减，则开始循环读入加减；否则，读入一个项
        if (lexer.getToken().equals("+") ||
                lexer.getToken().equals("-")) {
            boolean isPos = handleSig();
            Term term = consumeTerm();
            if (!isPos) {
                term.changePos();
            }
            expr.addTerm(term);
        } else {
            expr.addTerm(consumeTerm());
        }
        //循环后面的项，读入加减
        while (lexer.getToken().equals("+") ||
                lexer.getToken().equals("-")) {
            boolean isPos = handleSig();
            Term term = consumeTerm();
            if (!isPos) {
                term.changePos();
            }
            expr.addTerm(term);
        }
        return expr;
    }
    
    //读入一个项
    public Term consumeTerm() {
        Term term = new Term();
        //第一个因子处理，如果是指数，直接加进去
        Factor factor = consumeFactor();
        if (lexer.getToken().equals("**")) {
            expandPower(term, factor);
        } else {
            term.addFactor(factor);
        }
        while (lexer.getToken().equals("*")) {
            lexer.next();
            factor = consumeFactor();
            if (lexer.getToken().equals("**")) {
                expandPower(term, factor);
            } else {
                term.addFactor(factor);
            }
        }
        return term;
    }
    
    //读入一个因子
    public Factor consumeFactor() {
        if (lexer.getToken().equals("(")) {
            lexer.next();
            Factor expr = consumeExpr();
            lexer.next();
            return expr;
        } else if (lexer.getTokenType() == Lexer.TokenType.CONSTANT ||
                lexer.getToken().equals("-") ||
                lexer.getToken().equals("+")) {
            return consumeConstant();
        } else if (lexer.getTokenType() == Lexer.TokenType.VAR) {
            Var var = new Var(lexer.getToken());
            lexer.next();
            return var;
        } else if (lexer.getTokenType() == Lexer.TokenType.SIN) {
            lexer.next(); //sin
            lexer.next(); // (
            Expr expr = consumeExpr();
            lexer.next(); // )
            return new Sin(expr);
        } else if (lexer.getTokenType() == Lexer.TokenType.COS) {
            lexer.next(); // cos
            lexer.next(); // (
            Expr expr = consumeExpr();
            lexer.next();
            return new Cos(expr);
        } else if (lexer.getTokenType() == Lexer.TokenType.FUNCTION) {
            Expr expr = consumeFunction();
            return expr;
        } else if (lexer.getTokenType() == Lexer.TokenType.SUM) {
            lexer.next();
            Expr expr = consumeSum();
            lexer.next();
            return expr;
        } else {
            /*
            exception
            */
            return new Constant("0");
        }
    }
    
    public Function consumeCustomizeFunction() {
        Function function = new Function();
        function.setName(lexer.getToken());
        lexer.next(); // f
        ArrayList<Factor> parameters = new ArrayList<>();
        do {
            lexer.next(); // ( ,
            parameters.add(consumeFactor());
        } while (lexer.getToken().equals(","));
        function.setParameters(parameters);
        lexer.next(); // )
        lexer.next(); // =
        function.setExpr(consumeExpr());
        return function;
    }
    
    private Constant consumeConstant() {
        StringBuilder sb = new StringBuilder();
        //因子中只可能出现一个加减符号
        if (!(lexer.getTokenType() == Lexer.TokenType.CONSTANT)) {
            sb.append(lexer.getToken());
            lexer.next();
        }
        sb.append(lexer.getToken());
        lexer.next();
        return new Constant(sb.toString());
    }
    
    private Expr consumeSum() {
        Sum sum = new Sum();
        //first
        lexer.next();
        sum.setI((Var) consumeFactor());
        lexer.next(); //comma
        //second
        sum.setBottom(consumeConstant());
        lexer.next(); //comma
        //third
        sum.setTop(consumeConstant());
        lexer.next(); //comma
        sum.setTerm(consumeTerm());
        return sum.toExpr();
    }
    
    private Expr consumeFunction() {
        String name = lexer.getToken();
        lexer.next();
        Function function = MainClass.FUNCTIONS.get(name);
        ArrayList<Factor> actualParameters = new ArrayList<>();
        do {
            lexer.next(); // ( ,
            actualParameters.add(consumeTerm());
        } while (lexer.getToken().equals(","));
        lexer.next();
        return function.toExpr(actualParameters);
    }
    
    //乘方展开存入factors
    private void expandPower(Term term, Factor factor) {
        lexer.next();
        StringBuilder sb = new StringBuilder();
        //指数一定是带符号的常数，先处理正负
        if (lexer.getToken().equals("+") ||
                lexer.getToken().equals("-")) {
            sb.append(lexer.getToken());
            lexer.next();
        }
        sb.append(lexer.getToken());
        lexer.next();
        BigInteger ind = new BigInteger(sb.toString());
        for (BigInteger i = BigInteger.ZERO;
             i.compareTo(ind) < 0;
             i = i.add(BigInteger.ONE)) {
            term.addFactor(factor);
        }
        if (ind.equals(BigInteger.ZERO)) {
            term.addFactor(new Constant("1"));
        }
    }
}
