import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;
import java.util.ArrayList;

public class Mainclass {
    public static void main(String[] args) {
        ArrayList<String> functions = new ArrayList<>();
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        int cnt = scanner.getCount();
        String f = "";
        String g = "";
        String h = "";
        for (int i = 0;i < cnt;i++) {
            String func = scanner.readLine();
            func = func.replaceAll("\\s*","");
            if (func.charAt(0) == 'f') {
                f = func;
            } else if (func.charAt(0) == 'g') {
                g = func;
            } else if (func.charAt(0) == 'h') {
                h = func;
            }
        }
        String expr = scanner.readLine();
        expr = new PreDeal().dealFunction(expr,f,"f");
        expr = new PreDeal().dealFunction(expr,g,"g");
        expr = new PreDeal().dealFunction(expr,h,"h");
        expr = expr.replaceAll("\\s*","");
        expr = new PreDeal().dealSum(expr);
        expr = new PreDeal().singleAdd(expr);
        expr = expr.replace("**+","**");
        expr = expr.replace("*+","*");
        expr = expr.replace("sin","u");
        expr = expr.replace("cos","v");
        //System.out.println(expr);
        Lexer lexer = new Lexer(expr);
        Parser parser = new Parser(lexer);
        Expr father = parser.parseExpr();
        father.print(father.cal());


    }
}
