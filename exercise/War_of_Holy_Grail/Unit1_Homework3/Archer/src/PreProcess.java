import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreProcess {
    public static String replaceBlank(String string) {
        if (string == null) {
            return null;
        }
        else {
            Pattern p = Pattern.compile("\\s+");
            Matcher m = p.matcher(string);
            return m.replaceAll("");
        }
    }

    public static String[] getFuncParam(String string) {
        int index = string.indexOf('=');
        String str = string.substring(2, index - 1);
        return str.split(",");
    }

    public static String getFuncName(String string) {
        return string.substring(0, 1);
    }

    public static String getFunction(String string) {
        int index = string.indexOf('=');
        String str = string.substring(index + 1);
        return str;
    }

    public static String debug(String string) {
        Pattern p = Pattern.compile("(\\+)+");
        Matcher m = p.matcher(string);
        return m.replaceAll("+");
    }

}
