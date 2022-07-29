package factor.function;

import main.Parser;
import main.Lexer;
import factor.Expr;
import factor.Factor;

import java.io.IOException;
import java.util.ArrayList;

public class Custom {
    private final String name;
    private final Expr expr;

    public Custom(String func) throws IOException, ClassNotFoundException {
        String[] temp = func.split("=");
        this.name = temp[0].substring(0, 1);
        ArrayList<String> parameter = new ArrayList<>();
        for (int i = 2; i < temp[0].length(); i += 2) {
            parameter.add(temp[0].substring(i, i + 1));
        }
        String funcBody = temp[1];
        for (int i = 0; i < parameter.size(); i++) {
            funcBody = funcBody.replaceAll(parameter.get(i), Character.toString((char) (106 + i)));
        }

        Lexer lexer = new Lexer(funcBody);
        Parser parser = new Parser(lexer);
        this.expr = parser.parseExpr();
    }

    public Expr setVar(ArrayList<Factor> factors) throws IOException, ClassNotFoundException {
        Expr newExpr = expr.deepCopy();
        return newExpr.setVar(factors);
    }

    public String getName() {
        return name;
    }
}
