import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Factor {
    private String str;
    private int type;

    public Factor() {
    }

    public Factor(String str, int type) {
        this.str = str;
        this.type = type;
        if (type == 1 || type == 2 || type == 3) {
            this.str = Algebra.polyMulPoly(str, "1");
        }
        if (type == 4) {
            this.str = Algebra.polyMulPoly(str.substring(1, str.length() - 1), "1");
        }
        if (type == 5) {
            int exponent = str.charAt(str.length() - 1) - '0';
            String sample = "\\(.*?\\)";
            Pattern p = Pattern.compile(sample);
            Matcher m = p.matcher(str);
            m.find();
            this.str = minMal(m.group(), exponent);
        }
    }

    public int getType() {
        return type;
    }

    public String getStr() {
        return str;
    }

    public String minMal(String s, int exponent) {
        String s1 = s.substring(1, s.length() - 1);
        String s2 = s.substring(1, s.length() - 1);
        if (exponent == 0) {
            return "+1*x**0";
        }
        for (int i = 1; i < exponent; i++) {
            s1 = Algebra.polyMulPoly(s1, s2);
        }
        return s1;
    }

}
