package deconstruct;

import expression.Function;
import expression.Expr;
import expression.Term;
import expression.Factor;
import expression.Variable;
import expression.Tri;
import expression.Sum;
import expression.Constant;

import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;
    private ArrayList<Function> functions;
    
    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    
    public void setFunctions(ArrayList<Function> functions) {
        this.functions = functions;
    }
    
    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm());
        while (lexer.peekCurToken().equals("+") || lexer.peekCurToken().equals("-")) {
            if (lexer.peekCurToken().equals("+")) {
                lexer.nextCurToken();
                expr.addTerm(parseTerm());
            } else {
                lexer.nextCurToken();
                Term term = parseTerm();
                term.setSign(term.getSign() * -1);
                expr.addTerm(term);
            }
        }
        return expr;
    }
    
    public Term parseTerm() {
        Term term = new Term();
        //term.addFactor(parseFactor());
        Factor newFactor = parseFactor();
        if (newFactor.getSign() == -1) {
            newFactor.setSign(1);
            term.setSign(term.getSign() * -1);
        }
        term.addFactor(newFactor);
        while (lexer.peekCurToken().equals("*")) {
            lexer.nextCurToken();
            //term.addFactor(parseFactor());
            newFactor = parseFactor();
            if (newFactor.getSign() == -1) {
                newFactor.setSign(1);
                term.setSign(term.getSign() * -1);
            }
            term.addFactor(newFactor);
        }
        return term;
    }
    
    public Factor parseFactor() {
        int sign = 1;
        BigInteger exponent = new BigInteger("1");
        Factor factor;
        while (lexer.peekCurToken().equals("-") ||
                lexer.peekCurToken().equals("+")) {  // solve the '+-' at the beginning
            if (lexer.peekCurToken().equals("-")) {
                sign *= -1;
            }
            lexer.nextCurToken();
        }
        
        if (lexer.peekCurToken().equals("(")) { // solve the brackets
            lexer.nextCurToken();
            factor = parseExpr();
            lexer.nextCurToken();
        } else if ("xyzi".indexOf(lexer.peekCurToken().charAt(0)) != -1) {  // solve the variables
            factor = new Variable(lexer.peekCurToken());
            lexer.nextCurToken();
        } else if (lexer.peekCurToken().equals("cos") ||
                lexer.peekCurToken().equals("sin")) {  // solve the cos and sin
            String triname = lexer.peekCurToken();
            lexer.nextCurToken();
            lexer.nextCurToken();
            Expr insideExpr = parseExpr();
            String triname2 = triname.substring(0);
            insideExpr.setAnsAlready(false);
            boolean flag = true;
            for (Term term : insideExpr.getTerms()) {
                if (term.getSign() == 1) {
                    flag = false;
                }
            }
            String triname3 = triname2.substring(0);
            if (flag) {
                for (Term term : insideExpr.getTerms()) {
                    term.setSign(1);
                }
                insideExpr.setSign(insideExpr.getSign() * -1);
            }
            String triname4 = triname3.substring(0);
            //            if (insideExpr.getSign() == -1) {
            //                insideExpr.setSign(1);
            //                if (triname.equals("sin")) {
            //                    sign *= -1;
            //                }
            //            }
            lexer.nextCurToken();
            factor = new Tri(triname4, insideExpr);
        } else if (lexer.peekCurToken().equals("sum")) {    // solve the sum function
            factor = deconstSum();
        } else if ("fgh".indexOf(lexer.peekCurToken().charAt(0)) != -1) { // solve the fgh function
            factor = deconstfgh();
        } else {    // finally, solve the numbers
            BigInteger num = new BigInteger(lexer.peekCurToken());
            lexer.nextCurToken();
            factor = new Constant(num);
        }
        
        if (lexer.peekCurToken().equals("**")) {
            exponent = deconstPower();
        }
        factor.setSign(sign);
        factor.setExponent(exponent);
        return factor;
    }

    public Factor deconstfgh() {
        String name = lexer.peekCurToken();
        ArrayList<Factor> arguments = new ArrayList<>();
        lexer.nextCurToken(); // (
        lexer.nextCurToken();
        Factor arguFactor = parseExpr();
        arguments.add(arguFactor);
        int i = 1;
        while (lexer.peekCurToken() == ",") {
            lexer.nextCurToken();
            arguFactor = parseExpr();
            arguments.add(arguFactor);
            i++;
        }
        lexer.nextCurToken();
        Factor factor;
        factor = null;
        for (i = 0; i < functions.size(); i++) {
            if (functions.get(i).getName().equals(name)) {
                factor = functions.get(i).toExpr(arguments);
            }
        }
        return factor;
    }
    
    public Factor deconstSum() {
        lexer.nextCurToken();
        lexer.nextCurToken();
        Factor iter = parseFactor();
        lexer.nextCurToken();
        Expr const1 = parseExpr();
        BigInteger bottom = new BigInteger(const1.toString());
        lexer.nextCurToken();
        Expr const2 = parseExpr();
        BigInteger top = new BigInteger(const2.toString());
        lexer.nextCurToken();
        Expr exprSum = parseExpr();
        lexer.nextCurToken();
        Sum sum = new Sum(iter, bottom, top, exprSum);
        Factor factor;
        factor = sum.toExpr();
        return factor;
    }
    
    public BigInteger deconstPower() {
        BigInteger exponent;
        lexer.nextCurToken();
        if (lexer.peekCurToken().equals("+")) {
            lexer.nextCurToken();
        }
        exponent = new BigInteger(lexer.peekCurToken());
        lexer.nextCurToken();
        return exponent;
    }
}
