import java.math.BigInteger;
import java.util.TreeMap;
import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        ExprInput sc = new ExprInput(ExprInputMode.NormalMode);
        Lexer lexer = new Lexer(sc.readLine());
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        TreeMap<BigInteger, BigInteger> res = expr.simplify();
        printRes(res);
    }

    public static void printRes(TreeMap<BigInteger, BigInteger> res) {
        int flag = 1;
        int printed = 0;
        for (BigInteger key : res.keySet()) {
            if (res.get(key).equals(BigInteger.valueOf(0))) {
                continue;
            }
            if (key.compareTo(BigInteger.valueOf(0)) == 0) {
                if (res.get(key).compareTo(BigInteger.valueOf(0)) != 0) {
                    System.out.print(res.get(key));
                    printed = 1;
                }
            }
            else {
                if (res.get(key).compareTo(BigInteger.valueOf(-1)) == 0) {
                    System.out.print("-");
                    printed = 1;
                } else if (res.get(key).compareTo(BigInteger.valueOf(1)) == 0) {
                    if (flag != 1) {
                        System.out.print("+");
                        printed = 1;
                    }
                } else {
                    if (flag == 0 && res.get(key).compareTo(BigInteger.valueOf(0)) > 0) {
                        System.out.print("+");
                        printed = 1;
                    }
                    System.out.print(res.get(key) + "*");
                }
                if (key.compareTo(BigInteger.valueOf(1)) == 0) {
                    System.out.print("x");
                    printed = 1;
                } else if (key.compareTo(BigInteger.valueOf(2)) == 0) {
                    System.out.print("x*x");
                    printed = 1;
                } else {
                    System.out.print("x**" + key);
                    printed = 1;
                }
            }
            if (flag == 1) {
                flag = 0;
            }
        }
        if (printed == 0) {
            System.out.println(0);
        }
    }
}
