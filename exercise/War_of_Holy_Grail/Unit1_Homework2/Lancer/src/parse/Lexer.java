package parse;

public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }

        return sb.toString();
    }

    public String addAndSub() {
        boolean symbol = true;
        while (pos < input.length()) {
            if (String.valueOf(input.charAt(pos)).equals("-")) {
                symbol = !symbol;
            } else if ("+ \t".indexOf(input.charAt(pos)) == -1) {
                break;
            }
            pos++;
        }
        if (symbol) {
            return "+";
        } else {
            return "-";
        }
    }

    public void next() {
        if (pos == input.length()) {
            curToken = "";
            return;
        }

        char c = input.charAt(pos);
        while (String.valueOf(c).equals(" ") | String.valueOf(c).equals("\t")) {
            pos++;
            if (pos < input.length()) {
                c = input.charAt(pos);
            } else {
                return;
            }
        }

        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if (String.valueOf(c).equals("*")) {
            if (String.valueOf(input.charAt(pos + 1)).equals("*")) {
                pos += 2;
                curToken = "**";
            } else {
                pos += 1;
                curToken = String.valueOf(c);
            }
        } else if ("()xyzfghi,=".indexOf(c) != -1) {
            pos++;
            curToken = String.valueOf(c);
        } else if ("+-".indexOf(c) != -1) {
            curToken = addAndSub();
        } else if (String.valueOf(c).equals("s")) {
            if (String.valueOf(input.charAt(pos + 1)).equals("i")) {
                curToken = "sin";
            } else {
                curToken = "sum";
            }
            pos += 3;
        } else if (String.valueOf(c).equals("c")) {
            pos += 3;
            curToken = "cos";
        }
    }

    public String peek() {
        return this.curToken;
    }
}
