import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class MainClass {
    public static String rmAddOrSub(String input) {
        String s = input;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                for (int j = i + 1; j < s.length() && (s.charAt(i) == '+' ||
                        s.charAt(i) == '-'); j++) {
                    char c = s.charAt(j);
                    if (c != ' ' && c != '\t' && c != '+' && c != '-') {
                        break;
                    }
                    else if (c == '+') {
                        if (s.charAt(i) == '+') {
                            if (i > 0) {
                                s = s.substring(0, i) + "+"
                                        + s.substring(j + 1);
                            } else {
                                s = "+" + s.substring(j + 1);
                            }
                        }
                        else if (s.charAt(i) == '-') {
                            if (i > 0) {
                                s = s.substring(0, i) + "-"
                                        + s.substring(j + 1);
                            } else {
                                s = "-" + s.substring(j + 1);
                            }
                        }
                        j = i;
                    }
                    else if (c == '-') {
                        if (s.charAt(i) == '-') {
                            if (i > 0) {
                                s = s.substring(0, i) + "+"
                                        + s.substring(j + 1);
                            } else {
                                s = "+" + s.substring(j + 1);
                            }
                        }
                        else if (s.charAt(i) == '+') {
                            if (i > 0) {
                                s = s.substring(0, i) + "-"
                                        + s.substring(j + 1);
                            } else {
                                s = "-" + s.substring(j + 1);
                            }
                        }
                        j = i;
                    }
                }
            }
        }
        return s;
    }

    public static void main(String[] args) {
        ExprInputMode mode = ExprInputMode.NormalMode;
        ExprInput in = new ExprInput(mode);
        String input = in.readLine();
        Parser parser = new Parser();
        String expression = rmAddOrSub(input);
        parser.operate(expression).print();
    }
}
