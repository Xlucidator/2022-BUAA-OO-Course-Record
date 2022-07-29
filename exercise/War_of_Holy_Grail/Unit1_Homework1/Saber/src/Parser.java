import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public MiniList parseTerm(String expression) {
        Pattern p1 = Pattern.compile("[(](.+)[)]\\s*[**](.*?)(\\d+)");
        Matcher m1 = p1.matcher(expression);
        Pattern p2 = Pattern.compile("[(](.+)[)]");
        Matcher m2 = p2.matcher(expression);
        if (m1.find()) {
            if (Integer.parseInt(m1.group(3).trim()) == 0) {
                MiniList result = new MiniList();
                result.put(0,  new BigInteger("1"));
                return result;
            } else {
                int n = Integer.parseInt(m1.group(3));
                Parser parser = new Parser();
                String s = "( " + m1.group(1) + " )";
                for (int i = 0; i < n - 1; i++) {
                    s = s + " * ( " + m1.group(1) + " )";
                } return parser.operate(s);
            }
        } else if (m2.find()) {
            Parser parser = new Parser();
            return parser.operate(m2.group(1));
        }
        return null;
    }

    public MiniList operate(String expression) {
        int[] position = findAddOrSub(expression);
        if (position[0] != -1) {
            if (findNextOp(position[0], expression) == '+') {
                Add add = new Add(expression.substring(0, position[0]),
                        expression.substring(position[1]));
                return add.getResult();
            } else {
                Sub sub = new Sub(expression.substring(0, position[0]),
                        expression.substring(position[1]));
                return sub.getResult();
            }
        } else {
            position = findMul(expression);
            if (position[0] != -1) {
                Mul mul = new Mul(expression.substring(0, position[0]),
                        expression.substring(position[0] + 1));
                return mul.getResult();
            } else if (!expression.trim().equals("")) {
                if (expression.contains("(")) {
                    return parseTerm(expression);
                } else if (!expression.contains("x")) {
                    MiniList result = new MiniList();
                    result.setVariables(0, new BigInteger(expression.trim()));
                    return result;
                } else {
                    if (expression.contains("-x")) {
                        Pattern powerVar = Pattern.compile("\\s*-x\\s*\\*\\*.*(\\d)");
                        Matcher matcher = powerVar.matcher(expression);
                        MiniList result = new MiniList();
                        if (matcher.find()) {
                            result.put(Integer.parseInt(matcher.group(1).trim()),
                                    new BigInteger("-1"));
                        } else {
                            result.put(1, new BigInteger("-1"));
                        } return result;
                    } else {
                        Pattern powerVar = Pattern.compile("\\s*x\\s*\\*\\*.*(\\d)");
                        Matcher matcher = powerVar.matcher(expression);
                        MiniList result = new MiniList();
                        if (matcher.find()) {
                            result.put(Integer.parseInt(matcher.group(1).trim()),
                                    new BigInteger("1"));
                        } else {
                            result.put(1, new BigInteger("1"));
                        } return result;
                    }
                }
            }
        } MiniList result = new MiniList();
        result.put(0, new BigInteger("0"));
        return result;
    }

    public int[] findAddOrSub(String expression) {
        Pattern p = Pattern.compile("\\s*[+|-]\\s*");
        Matcher m = p.matcher(expression);
        int[] ans = new int[2];
        ans[0] = -1;
        while (m.find()) {
            int index = 0;
            int x = 0;
            for (; x <= m.start(); x++) {
                if (expression.charAt(x) == '(') {
                    index++;
                } else if (expression.charAt(x) == ')') {
                    index--;
                }
            }
            if (index == 0) {
                int y = m.start();
                char c = expression.charAt(y);
                while ((c == ' ' || c == '\t' || c == '+' || c == '-') && y != 0) {
                    y--;
                    c = expression.charAt(y);
                }
                if (expression.charAt(y) != '*') {
                    ans[0] = m.start();
                    ans[1] = m.end();
                }
            }
        } return ans;
    }

    public char findNextOp(int position, String expression) {
        int n = position;
        while (expression.charAt(n) != '+'
            && expression.charAt(n) != '-'
            && expression.charAt(n) != '*') {
            n++;
        }
        return expression.charAt(n);
    }

    public int[] findMul(String expression) {
        Pattern p = Pattern.compile("[^*][*][^*]");
        Matcher m = p.matcher(expression);
        int[] ans = new int[2];
        ans[0] = -1;
        while (m.find()) {
            int index = 0;
            int x = 0;
            for (; x <= m.start(); x++) {
                if (expression.charAt(x) == '(') {
                    index++;
                } else if (expression.charAt(x) == ')') {
                    index--;
                }
            }
            if (index == 0) {
                Pattern p1 = Pattern.compile("[*]");
                Matcher m1 = p1.matcher(expression.substring(m.start()));
                m1.find();
                ans[0] = m1.start() + m.start();
                ans[1] = m1.end();
                return ans;
            }
        }
        return ans;
    }
}
