import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是一般读入模式，所以我们实例化时传递的参数为ExprInputMode.NormalMode
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);

        // 获取自定义函数个数
        int cnt = scanner.getCount();

        // 读入自定义函数
        ArrayList<Custom> funcs = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine();
            Lexer lexer = new Lexer(func);
            Parser parser = new Parser(lexer);
            funcs.add(parser.parseFunc());
        }

        // 读入最后一行表达式
        String exprStr = scanner.readLine();

        // 表达式括号展开相关的逻辑
        Lexer lexer = new Lexer(exprStr);
        Parser parser = new Parser(lexer, funcs);

        Expr expr = parser.parseExpr();
        expr.compute();
        System.out.println(expr);
    }
}
