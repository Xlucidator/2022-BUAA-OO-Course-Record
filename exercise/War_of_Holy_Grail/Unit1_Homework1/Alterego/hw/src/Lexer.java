import java.math.BigInteger;

public class Lexer {
    private String string;
    private int pos = 0;
    private int length;

    public Lexer(String e) {
        string = e.replaceAll(" ", "");
        string = string.replaceAll("\t", "");
        length = string.length();
    }

    public void goNext() {
        ++pos;
    }

    public String getchar() {
        if (pos >= length) {
            return "?";
        }
        return String.valueOf(string.charAt(pos));
    }

    public int getPos() {
        return pos;
    }

    public void jumpSpace() {
        while (getchar() == " " || getchar() == "\t") {
            goNext();
        }
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean notTheEnd() {
        if (pos < length - 1) {
            return true;
        }
        return false;
    }

    public boolean nextIsExp() {
        if (pos >= length) {
            return false;
        }
        if (getchar().equals("*") && String.valueOf(string.charAt(pos + 1)).equals("*")) {
            return true;
        }
        return false;
    }

    public BigInteger nextNum() {
        StringBuilder sb = new StringBuilder();
        if ("+-".indexOf(getchar()) != -1) {
            sb.append(getchar());
            goNext();
        }
        while ("0123456789".indexOf(getchar()) != -1) {
            sb.append(getchar());
            goNext();
        }
        return new BigInteger(sb.toString());
    }
}
