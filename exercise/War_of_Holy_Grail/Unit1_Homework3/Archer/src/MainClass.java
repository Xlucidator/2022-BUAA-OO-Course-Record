import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;
import expr.Expr;

import expr.Poly;

import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        int n = scanner.getCount();
        HashMap<String, String> funcMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String func = scanner.readLine();
            String noBlank = PreProcess.replaceBlank(func);
            String funcName = PreProcess.getFuncName(noBlank);
            funcMap.put(funcName, noBlank);
        }

        String input = scanner.readLine();
        String string = PreProcess.replaceBlank(input);

        Lexer lexer = new Lexer(string);
        Parser parser = new Parser(lexer, funcMap);

        Expr expr = parser.parseExpr();
        Poly p = expr.toPoly();

        Poly pp = p.simplify();

        System.out.println(pp);
    }

    /*
0
sin((x*x)) + sin(((x*x+x*x)))

0
sin(0*x*(x+1))+sin(0)

2
h(x)=x**3+x**2
f(x,y)=x+2*y
h(f(x,x**2+x))
     */
}
