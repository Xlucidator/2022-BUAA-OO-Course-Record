import java.util.ArrayList;

public class Function {
    private final ArrayList<Factor> formPars;
    private final Expr expr;
    
    public Factor toExpr(ArrayList<Factor> parameters) {
        Factor tempFactor = expr.replace(formPars, parameters);
        String tempStr = tempFactor.toTriPoly().toString();//格式化重构
        tempStr = tempStr.replace("sin","s");
        tempStr = tempStr.replace("cos","c");
        Lexer lexer = new Lexer(tempStr);
        Parser parser = new Parser(lexer);
        return parser.parseExpr(null, null, null);
    }
    
    public Function(Expr expr, ArrayList<Factor> formPars) {
        this.expr = expr;
        this.formPars = formPars;
    }
}
