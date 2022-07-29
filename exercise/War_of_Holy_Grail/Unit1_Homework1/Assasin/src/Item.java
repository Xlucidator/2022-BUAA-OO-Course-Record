import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item {
    private String str;
    private char sym;
    private ArrayList<Factor> factorArray = new ArrayList<>();

    public Item() {
    }

    public Item(String str) {
        int type;
        this.str = str;
        this.sym = str.charAt(0);
        String s = this.str.substring(1, this.str.length());
        String sample1 = "[\\+-]?\\d+";
        String sample2 = "x";
        String sample3 = "x\\*\\*\\d+";
        String sample4 = "\\([^\\(]*?\\)";
        String sample5 = "\\([^\\(]*?\\)\\*\\*\\d+";
        Pattern p = Pattern.compile(sample5 + '|' + sample4 + '|' + sample3 +
                '|' + sample2 + '|' + sample1);
        Pattern p1 = Pattern.compile(sample1);
        Pattern p2 = Pattern.compile(sample2);
        Pattern p3 = Pattern.compile(sample3);
        Pattern p4 = Pattern.compile(sample4);
        Pattern p5 = Pattern.compile(sample5);

        Matcher matcher = p.matcher(s);
        while (matcher.find()) {
            Matcher m1 = p1.matcher(matcher.group());
            Matcher m2 = p2.matcher(matcher.group());
            Matcher m3 = p3.matcher(matcher.group());
            Matcher m4 = p4.matcher(matcher.group());
            Matcher m5 = p5.matcher(matcher.group());
            if (m5.find()) {
                type = 5;
            } else if (m4.find()) {
                type = 4;
            } else if (m3.find()) {
                type = 3;
            } else if (m2.find()) {
                type = 2;
            } else {
                type = 1;
            }
            factorArray.add(new Factor(matcher.group(), type));
        }
    }

    public String getStr() {
        return str;
    }

    public String calculate() {
        String s1 = factorArray.get(0).getStr();
        String s2 = new String();
        for (int i = 1; i < factorArray.size(); i++) {
            s2 = factorArray.get(i).getStr();
            s1 = Algebra.polyMulPoly(s1, s2);
        }
        if (this.sym == '-') {
            s1 = s1.replaceAll("\\+", "!");
            s1 = s1.replaceAll("-", "\\+");
            s1 = s1.replaceAll("!", "-");
        }
        return s1;
    }

}
