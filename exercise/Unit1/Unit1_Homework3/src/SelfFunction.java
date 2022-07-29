import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelfFunction {
    private String expr;
    private final ArrayList<String> parameters;

    public SelfFunction(String expr, String[] parameters) {
        this.expr = expr;
        this.parameters = new ArrayList<>();
        this.parameters.addAll(Arrays.asList(parameters));
    }

    public void refractExpr() {
        Pattern parameterPowerPattern = Pattern.compile("([xyz])\\*{2}(\\+?\\d+)");
        while (true) {
            Matcher matcher = parameterPowerPattern.matcher(expr);
            if (!matcher.find()) {
                break;
            }
            String p = matcher.group(1);
            int exp = Integer.parseInt(matcher.group(2));
            String mulConvert = "";
            for (int i = 0; i < exp; i++) {
                mulConvert = mulConvert.concat(i == 0 ? p : "*" + p);
            }
            if (mulConvert.isEmpty()) {
                mulConvert = "1";
            }
            expr = expr.substring(0, matcher.start()) + mulConvert + expr.substring(matcher.end());
        }
    }

    public String getExpr() {
        return expr;
    }

    public ArrayList<String> getParameters() {
        return this.parameters;
    }

}
