import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Algebra {
    public static String addSym(String s) {
        String s1 = new String();
        if (s.startsWith("+") || s.startsWith("-")) {
            s1 = s;
        } else {
            s1 = "+" + s;
        }
        return s1;
    }

    public static void getArray(String s, String sample, ArrayList<String> array) {
        Pattern p1 = Pattern.compile(sample);
        Matcher m1 = p1.matcher(s);
        while (m1.find()) {
            array.add(m1.group());
        }
    }

    public static String monMulMon(String one, String two) {
        ArrayList<String> all = new ArrayList<>();
        String sample1 = "[\\+-]?\\d+";
        String sample2 = "x";
        String sample3 = "x\\*\\*\\d+";
        Pattern p1 = Pattern.compile(sample1);
        Pattern p2 = Pattern.compile(sample2);
        Pattern p3 = Pattern.compile(sample3);
        char oneSym = one.charAt(0);
        char twoSym = two.charAt(0);
        String sym = new String();
        if (oneSym == twoSym) {
            sym = "+";
        } else {
            sym = "-";
        }
        getArray(one.substring(1, one.length()), sample3 + '|' + sample2 + '|' + sample1, all);
        getArray(two.substring(1, two.length()), sample3 + '|' + sample2 + '|' + sample1, all);
        BigInteger coe = new BigInteger(String.valueOf(1));
        int exponent = 0;
        for (String s : all) {
            if (p3.matcher(s).find()) {
                exponent += s.charAt(s.length() - 1) - '0';
            } else if (p2.matcher(s).find()) {
                exponent += 1;
            } else if (p1.matcher(s).find()) {
                coe = coe.multiply(new BigInteger(s));
            }
        }
        return sym + coe.toString() + "*x**" + exponent;
    }

    public static String polyMulPoly(String s1, String s2) {
        String o1 = addSym(s1);
        String o2 = addSym(s2);
        ArrayList<String> userO1 = new ArrayList<>();
        o1 = o1.replaceAll("\\*-", "#");
        getArray(o1, "[\\+-][^\\+-]*", userO1);
        ArrayList<String> userO2 = new ArrayList<>();
        o2 = o2.replaceAll("\\*-", "#");
        getArray(o2, "[\\+-][^\\+-]*", userO2);
        ArrayList<String> result = new ArrayList<>();
        for (String first : userO1) {
            for (String second : userO2) {
                result.add(monMulMon(first.replaceAll("#", "\\*-"),
                        second.replaceAll("#", "\\*-")));
            }
        }
        String res = "";
        for (String s : result) {
            res += s;
        }
        return res;
    }

    public static String merge(String s) {
        String res = "";
        ArrayList<String> allItem = new ArrayList<>();
        getArray(s, "[\\+-][^\\+-]*", allItem);
        BigInteger[] poly = new BigInteger[10];
        for (String i : allItem) {
            getItem(poly, i);
        }
        for (int i = 9; i >= 0; i--) {
            if (poly[i] != null) {
                if (equalBig(poly[i], 0)) {
                    continue;
                }
                if (i == 0) {
                    res += poly[i].toString() + "+";
                } else if (i == 1) {
                    if (equalBig(poly[i], 1)) {
                        res += "x" + "+";
                    } else if (equalBig(poly[i], -1)) {
                        res += "-x" + "+";
                    } else {
                        res += poly[i].toString() + "*x" + "+";
                    }
                } else {
                    if (equalBig(poly[i], 1)) {
                        res += "x**" + i + "+";
                    } else if (equalBig(poly[i], -1)) {
                        res += "-x**" + i + "+";
                    } else {
                        res += poly[i].toString() + "*x**" + i + "+";
                    }
                }
            }
        }
        if (res == "") {
            return "0";
        }
        return res.substring(0, res.length() - 1);
    }

    public static void getItem(BigInteger[] poly, String s) {
        String sample = "([\\+-]\\d+)\\*x\\*\\*(\\d+)";
        Pattern p = Pattern.compile(sample);
        Matcher m = p.matcher(s);
        if (m.find() && m.group(2).charAt(0) - '0' < 10) {
            if (poly[m.group(2).charAt(0) - '0'] == null) {
                poly[m.group(2).charAt(0) - '0'] = new BigInteger(String.valueOf(0));
            }
            poly[m.group(2).charAt(0) - '0'] = poly[m.group(2).charAt(0) - '0'].add(
                    new BigInteger(m.group(1)));
        }

    }

    public static boolean equalBig(BigInteger num, int op) {
        if (op == 0) {
            return num.equals(new BigInteger(String.valueOf(0)));
        } else if (op == 1) {
            return num.equals(new BigInteger(String.valueOf(1)));
        } else {
            return num.equals(new BigInteger(String.valueOf(-1)));
        }
    }
}
