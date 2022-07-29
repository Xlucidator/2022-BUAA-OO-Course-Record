// 需要先将官方包中用到的工具类import进来
import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class MainClass {

    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是一般读入模式，所以我们实例化时传递的参数为ExprInputMode.NormalMode
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);

        // 一般读入模式下，读入一行字符串时使用readLine()方法，在这里我们使用其读入表达式
        String expression = scanner.readLine();
        // Scanner scanner = new Scanner(System.in);
        // String expression = scanner.nextLine();
        // 表达式括号展开相关的逻辑
        Holder holder = new Holder(expression);
        Parser parser = new Parser(holder);
        System.out.println(parser.parseExpression().toString());
    }
}
//(x*x*x-x**3)
//x*x*x*x