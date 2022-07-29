import func.Func;
import func.SelfFunc;
import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;
import expression.Expression;
import parse.Lexer;
import parse.Parse;
import polynomial.Polynomial;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        int n = scanner.getCount();
        SelfFunc selfFunc = new SelfFunc();
        for (int i = 0; i < n; i++) {
            Lexer lexer = new Lexer(scanner.readLine());
            Func func = new Func(lexer);
            selfFunc.addFunc(func);
        }
        String expr = scanner.readLine();
        Lexer lexer = new Lexer(expr);
        Parse parse = new Parse(lexer,selfFunc);
        Expression expression = parse.parseExpr();
        Polynomial polynomial = expression.factorToPoly();
        //polynomial = polynomial.add();
        System.out.println(polynomial.toString());
    }
}
