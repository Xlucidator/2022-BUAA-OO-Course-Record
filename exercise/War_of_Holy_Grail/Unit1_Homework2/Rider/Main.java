import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是预解析读入模式，所以我们实例化时传递的参数为ExprInputMode.ParsedMode
        ExprInput scanner = new ExprInput(ExprInputMode.ParsedMode);

        // 预解析读入模式下，使用getCount()读入接下来还要读取多少行解析后的表达式
        int n = scanner.getCount();

        // 调用n次readLine()方法，读入解析后的表达式，并存储到容器exprs中
        List<String> exprs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String curExpr = scanner.readLine();
            exprs.add(curExpr);
        }
        Parser parser = new Parser(n,exprs);
        ArrayList<Factor> funtion = parser.parserStr();
        Print print = new Print(funtion);
        print.out();
    }
}
