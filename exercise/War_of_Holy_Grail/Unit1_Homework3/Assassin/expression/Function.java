package expression;

import deconstruct.Lexer;
import deconstruct.Parser;
import expression.Expr;
import expression.Factor;
import expression.Variable;

import java.util.ArrayList;

public class Function {
    private String name;
    private ArrayList<Factor> parameters = new ArrayList<>();
    private Expr expr;
    
    public Function(String inputFun) {
        String [] fun = inputFun.split("=");
        String mid = fun[0].substring(2, fun[0].length() - 1);
        this.setName(fun[0].substring(0, 1));
        String [] variables = mid.split(",");
        int i = 0;
        for (String name : variables) {
            parameters.add(new Variable(name));
        }
        Lexer lexer = new Lexer(fun[1]);
        Parser parser = new Parser(lexer);
        this.expr = parser.parseExpr();
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public Expr toExpr(ArrayList<Factor> arguments) {
        Expr replacedExpr = expr.replace(parameters, arguments);
        replacedExpr.setExponent(expr.getExponent());
        replacedExpr.setSign(expr.getSign());
        return replacedExpr;
    }
}
