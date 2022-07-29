package main;

import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;
import factor.Expr;
import factor.function.Custom;

import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);

        int cnt = scanner.getCount();
        FunctionBase functionBase = new FunctionBase();

        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine();
            func = func.replaceAll("\\s*", "");
            Custom custom = new Custom(func);
            functionBase.addFunction(custom);
        }

        String expr = scanner.readLine();
        expr = expr.replaceAll("\\s*", "");

        Lexer lexer = new Lexer(expr);
        Parser parser = new Parser(lexer, functionBase);

        Expr res = parser.parseExpr();
        System.out.println(res);
    }
}