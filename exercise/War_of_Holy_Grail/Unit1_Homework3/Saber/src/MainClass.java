import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;

import java.util.HashMap;
import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.ParsedMode);

        int n = scanner.getCount();
        ArrayList<String> exprs = new ArrayList<>();
        HashMap<String, Expr> lexprs = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String curExpr = scanner.readLine();
            Parser parser = new Parser(lexprs);
            exprs.add(curExpr);
            parser.parserStr(curExpr);
        }
        // 读取最后的表达式
        String str = exprs.get(n - 1);
        String[] temp = str.split(" ");
        Expr expr = lexprs.get(temp[0]);
        // 输出最后的表达式
        Lexer lexer = new Lexer(expr);
        System.out.print(lexer.out());
    }
}

