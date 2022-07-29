package expr;

import java.util.ArrayList;

public class Function {
    private String name;
    private ArrayList<Factor> parameters;
    private Expr expr;
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setParameters(ArrayList<Factor> parameters) {
        this.parameters = parameters;
    }
    
    public void setExpr(Expr expr) {
        this.expr = expr;
    }
    
    public Expr toExpr(ArrayList<Factor> actualParameters) {
        Expr ref = (Expr) expr.replace(parameters, actualParameters);
        return ref;
    }
}
