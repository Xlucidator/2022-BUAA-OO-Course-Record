import java.math.BigInteger;

public class PreDeal {

    public String singleAdd(String input) {
        String newString = input;
        while (!newString.equals(newString.replace("++", "+")) ||
                !newString.equals(newString.replace("--", "+")) ||
                !newString.equals(newString.replace("+-", "-")) ||
                !newString.equals(newString.replace("-+", "-"))) {
            newString = newString.replace("++", "+");
            newString = newString.replace("--", "+");
            newString = newString.replace("+-", "-");
            newString = newString.replace("-+", "-");
        }
        return newString;
    }

    public String save(String expr) {
        String result;
        result = expr.replace("sum", "a");
        result = result.replace("sin", "u");
        result = result.replace("cos", "v");
        return result;
    }

    public String dealSum(String expr) {
        String result;
        int pos = 0;
        result = this.save(expr);
        while (true) {
            String s = "";
            String e = "";
            String t = "";
            if (result.indexOf("a") == -1) {
                break;
            }
            String sum = "a(i,";
            while (result.charAt(pos) != 'a') {
                pos++;
            }
            pos = pos + 4;
            int bracket = 1;
            while (bracket != 0) {
                sum = sum + String.valueOf(result.charAt(pos));
                if (result.charAt(pos) == '(') {
                    bracket++;
                } else if (result.charAt(pos) == ')') {
                    bracket--;
                }
                pos++;
            }
            String newSum = "(";
            int j = sum.indexOf(",");
            j++;
            while (sum.charAt(j) != ',') {
                s = s + String.valueOf(sum.charAt(j));
                j++;
            }
            j++;
            while (sum.charAt(j) != ',') {
                e = e + String.valueOf(sum.charAt(j));
                j++;
            }
            j++;
            while (j != sum.length() - 1) {
                t = t + String.valueOf(sum.charAt(j));
                j++;
            }
            BigInteger intS = new BigInteger(s);
            BigInteger intE = new BigInteger(e);
            BigInteger i = new BigInteger("0");
            for (i = intS;i.compareTo(intE) <= 0; i = i.add(new BigInteger("1"))) {
                newSum = newSum + t.replace("i", i.toString());
                if (!i.equals(intE)) {
                    newSum = newSum + "+";
                }
            }
            newSum = newSum + ")";
            result = result.replace(sum, newSum);
            pos = 0;
        }
        return result;
    }

    public String dealFunction(String expr, String f, String symbol) {
        String newformula;
        String parameter;
        int x = 1;
        String real = symbol + "(";
        String[] temp = f.split("=");
        int pos = 0;
        String fir = "";
        String sec = "";
        String third = "";
        if (f == "") {
            return expr;
        }
        fir = String.valueOf(temp[0].charAt(2));
        temp[1] = temp[1].replace(fir, "A");
        if (temp[0].charAt(3) == ',') {
            sec = String.valueOf(temp[0].charAt(4));
            temp[1] = temp[1].replace(sec, "B");
            x++;
            if (temp[0].charAt(5) == ',') {
                third = String.valueOf(temp[0].charAt(6));
                temp[1] = temp[1].replace(third, "C");
                x++;
            }
        }
        String result = expr;
        result = this.second(expr, temp[1], symbol, x);
        return result;
    }

    public String second(String expr, String temp, String symbol, int x) {
        String real = symbol + "(";
        String parameter = "";
        String result = expr.replaceAll("\\s*", "");
        int pos = 0;
        String newformula = "";
        while (true) {
            parameter = "";
            real = symbol + "(";
            if (result.indexOf(symbol) == -1) {
                break;
            }
            pos = result.indexOf(symbol);
            pos = pos + 2;
            int tool = 1;
            while (tool != 0) {
                if (result.charAt(pos) == '(') {
                    tool++;
                } else if (result.charAt(pos) == ')') {
                    tool = tool - 1;
                }
                if (tool != 0) {
                    parameter = parameter + String.valueOf(result.charAt(pos));
                }
                pos++;
            }
            real = real + parameter + ")";
            String[] num = parameter.split(",");
            if (x == 1) {
                parameter = "(" + parameter + ")";
                newformula = temp.replace("A", parameter);
            } else if (x == 2) {
                String test = "(" + num[0] + ")";
                newformula = temp.replace("A", test);
                test = "(" + num[1] + ")";
                newformula = newformula.replace("B", test);
            } else if (x == 3) {
                String test = "(" + num[0] + ")";
                newformula = temp.replace("A", test);
                test = "(" + num[1] + ")";
                newformula = newformula.replace("B", test);
                test = "(" + num[2] + ")";
                newformula = newformula.replace("C", test);
            }
            newformula = "(" + newformula + ")";
            result = result.replace(real, newformula);
        }
        return result;
    }

}
