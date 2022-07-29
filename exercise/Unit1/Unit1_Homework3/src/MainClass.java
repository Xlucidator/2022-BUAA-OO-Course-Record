import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    public static final HashMap<String, SelfFunction> MYFUNCTIONS = new HashMap<>();

    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        int cnt = scanner.getCount();
        Pattern funcPattern = Pattern.compile("([fgh])\\(([xyz,]+)\\)=(.+)");

        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine().replaceAll("[ \t]", "");
            Matcher funcMatcher = funcPattern.matcher(func);
            if (funcMatcher.find()) {
                String[] parameters = funcMatcher.group(2).split(",");
                SelfFunction tmp = new SelfFunction(funcMatcher.group(3), parameters);
                tmp.refractExpr();
                MYFUNCTIONS.put(funcMatcher.group(1), tmp);
            }
        }

        String input = scanner.readLine().replaceAll("[ \t]", "");
        String exprAfterPreTreatment = clipDupOperator(input);

        Lexer lexer = new Lexer(exprAfterPreTreatment);
        Parser parser = new Parser(lexer);

        Expression expression = parser.parseExpression();
        //System.out.println(expression);
        expression.sortInCoefficientDescendingOrder();
        System.out.println(clipDupOperator(expression.inFormat(false, true)));
    }

    public static String clipDupOperator(String origin) {
        String modifier = origin;
        for (int i = 0; i < modifier.length() - 1; i++) {
            String detectTwo = modifier.substring(i, i + 2);
            if (detectTwo.matches("[+-]{2}")) {
                String op = (detectTwo.matches("(\\+\\+)|(--)")) ? "+" : "-";
                modifier = modifier.substring(0, i) + op + modifier.substring(i + 2);
                i--;
            }
            if (detectTwo.matches("\\*{2}") && modifier.charAt(i + 2) == '+') {
                modifier = modifier.substring(0, i + 2) + modifier.substring(i + 3);
            }
        }
        return modifier;
    }

}
