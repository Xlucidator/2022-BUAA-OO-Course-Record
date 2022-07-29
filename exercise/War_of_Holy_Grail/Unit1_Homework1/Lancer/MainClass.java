import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

import java.math.BigInteger;
import java.util.ArrayList;

import java.util.HashMap;

public class MainClass {
    @SuppressWarnings("checkstyle:LocalVariableName")
    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是预解析读入模式，所以我们实例化时传递的参数为ExprInputMode.ParsedMode
        ExprInput scanner = new ExprInput(ExprInputMode.ParsedMode);

        // 预解析读入模式下，使用getCount()读入接下来还要读取多少行解析后的表达式
        int n = scanner.getCount();
        ArrayList<Express> exprs = new ArrayList<>();

        //LinkedHashMap<String, String> exprs = new LinkedHashMap<>();
        // 调用n次readLine()方法，读入解析后的表达式，并存储到容器exprs中
        //List<String> exprs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] linshi = scanner.readLine().split(" ");//每一项
            String[] linshi2 = new String[linshi.length - 1];//order
            int index = 0;
            for (int j = 1; j < linshi.length; j++) {
                if (!linshi[j].equals("")) {
                    linshi2[index] = linshi[j];
                    index++;
                }
            }
            String s = String.join(" ", linshi2);
            Express express = new Express(linshi[0], s, new HashMap<>());
            exprs.add(express);
        }

        for (Express item : exprs) {
            String[] canshu = item.getOrder().split(" ");
            Parser parser1 = new Parser();
            if (canshu.length != 1 && !canshu[1].equals("null")) {
                HashMap<Integer, BigInteger> h1 = parser1.hi(canshu[1], exprs);
                if (canshu[0].equals("neg")) {
                    Neg neg = new Neg();
                    HashMap<Integer, BigInteger> h3 = neg.cal(h1);
                    item.setHashmap(h3);
                } else if (canshu[0].equals("pos")) {
                    item.setHashmap(h1);
                } else if (canshu[0].equals("add")) {
                    HashMap<Integer, BigInteger> h2 = parser1.hi(canshu[2], exprs);
                    Add add = new Add();
                    HashMap<Integer, BigInteger> h3 = add.cal(h1, h2);
                    item.setHashmap(h3);
                } else if (canshu[0].equals("sub")) {
                    HashMap<Integer, BigInteger> h2 = parser1.hi(canshu[2], exprs);
                    Sub sub = new Sub();
                    HashMap<Integer, BigInteger> h3 = sub.cal(h1, h2);
                    item.setHashmap(h3);
                } else if (canshu[0].equals("mul")) {
                    HashMap<Integer, BigInteger> h2 = parser1.hi(canshu[2], exprs);
                    Mul mul = new Mul();
                    HashMap<Integer, BigInteger> h3 = mul.cal(h1, h2);
                    item.setHashmap(h3);
                } else if (canshu[0].equals("pow")) {
                    Pow pow = new Pow();
                    HashMap<Integer, BigInteger> h3 = pow.cal(h1, Integer.parseInt(canshu[2]));
                    item.setHashmap(h3);
                }
            } else {
                HashMap<Integer, BigInteger> h1 = parser1.hi(canshu[0], exprs);
                item.setHashmap(h1);
            }
        }

        Toanswer toanswer = new Toanswer();
        String ans = toanswer.end(exprs.get(exprs.size() - 1).getHashmap());
        Tiaozheng t = new Tiaozheng();
        ans = t.simple(ans);
        System.out.println(ans);

    }
}
