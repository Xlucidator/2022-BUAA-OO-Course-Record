import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String input = scanner.readLine().replaceAll("[ \t]", "");
        String exprAfterPretreatment = clipDupOperator(unfoldPower(input));
        //System.out.println(exprAfterPretreatment);

        Lexer lexer = new Lexer(exprAfterPretreatment);
        Parser parser = new Parser(lexer);

        Expression expression = parser.parseExpression();
        //System.out.println(expression);

        for (int i = 0; i < expression.size(); i++) {
            Term t = expression.get(i);
            if (t.getBracketExpressions().size() == 0) {
                continue;
            }
            ArrayList<Term> splitTerms = t.departBracket();
            expression.delTerm(i--);
            for (Term term : splitTerms) {
                expression.addTerm(term);
            }
        }
        expression.uniteLikeTerm();
        expression.sortInCoefficientDescendingOrder();
        if (expression.size() == 0) {
            System.out.println(0);
        } else {
            System.out.println(clipDupOperator(expression.inFormat()));
        }
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

    public static String unfoldPower(String origin) {
        Pattern varPowPattern = Pattern.compile("x" + "\\*{2}" + "\\+?\\d+");
        Pattern exprPowPattern = Pattern.compile("\\([^()]*?\\)" + "\\*{2}" + "\\+?\\d+");
        Pattern[] powPattern = new Pattern[]{varPowPattern, exprPowPattern};
        String modifier = origin;

        for (Pattern pattern : powPattern) { // x**N first, (..)**N next
            while (true) {
                Matcher powMatcher = pattern.matcher(modifier);
                if (!powMatcher.find()) {
                    break;
                }
                String[] part = powMatcher.group().split("\\*{2}");
                int exp = Integer.parseInt(part[1]);
                String mulConvert = "";
                for (int i = 0; i < exp; i++) {
                    mulConvert = mulConvert.concat(i == 0 ? part[0] : "*" + part[0]);
                }
                if (mulConvert.isEmpty()) {
                    mulConvert = "1";
                }
                modifier = modifier.substring(0, powMatcher.start()) + mulConvert
                        + modifier.substring(powMatcher.end());
            }
            //System.out.println("[unfold]" + modifier);
        }
        return modifier;
    }
}
