
public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;
    private int flag;
    private String pattern = "^[-,+]\\s*[-,+]\\s[-,+]";
    //  flag 用来标记当前种类

    public Lexer(String input) {
        this.input = input;
        flag = 3;
        this.next();
    }

    public int getFlag() {
        return flag;
    }

    public String getCurToken() {
        return curToken;
    }

    public void setFlag(int f) {
        this.flag = f;
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        while (pos < input.length() && (input.charAt(pos) == 32 || input.charAt(pos) == 9)) {
            pos += 1;
        }
        if (pos < input.length() && input.charAt(pos) == '*' && input.charAt(pos + 1) == '*') {
            sb.append("**");
            pos += 2;
            sb.append(getSignedNumber());
        }
        return sb.toString();
    }

    private String getSignedNumber() {
        while (input.charAt(pos) == 32 || input.charAt(pos) == 9) {
            pos += 1;
        }
        if (input.charAt(pos) == '+') {
            pos++;
            return "+" + getNumber();
        } else if (input.charAt(pos) == '-') {
            pos++;
            return "-" + getNumber();
        } else {
            return getNumber();
        }
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        while (input.charAt(pos) == 32 || input.charAt(pos) == 9) {
            pos += 1;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {    // the Number without sign
            curToken = getNumber();
            flag = 1;
        } else if ("(+-".indexOf(c) != -1) {    // some  operator
            pos += 1;
            curToken = String.valueOf(c);
            if (c == '(') {     // bracket
                flag = 2;
                int s = 0;
                Lexer pre = new Lexer(input.substring(pos));
                int opFirst = pre.getFlag();
                pre.next();
                int opSecond = pre.getFlag();
                String ctSecond = pre.getCurToken();
                if ((opFirst == 11 || opFirst == 10)) {
                    if (opFirst == 11) {
                        s = 1;
                    } else {
                        s = -1;
                    }
                }
                if (s == 1) {
                    flag = 21;
                } else if (s == -1) {
                    flag = 20;
                }
                // System.out.println("sign: " + sign);
            }
            if (c == '+') {
                if (c == '+' && (flag >= 2 && flag <= 5 || flag == 11 || flag == 10)
                        && Character.isDigit(input.charAt(pos))) {
                    curToken = getNumber();     // the Number with sign (pos)
                    flag = 1;
                } else {
                    while (input.charAt(pos) == 32 || input.charAt(pos) == 9) {
                        pos += 1;
                    }
                    if (c == '+' && (flag >= 2 && flag <= 4) &&
                            input.charAt(pos) != ')' && input.charAt(pos) != '*') {
                        flag = 11;
                        curToken = "+";
                    } else if (c == '+') {
                        flag = 3;
                        curToken = "+";
                    }
                }
            }
            if (c == '-') {
                if (c == '-' && (flag >= 2 && flag <= 5 || flag == 11 || flag == 10)
                        && Character.isDigit(input.charAt(pos))) {
                    curToken = "-" + getNumber();     // the Number with sign (pos)
                    flag = 1;
                }  else {
                    while (input.charAt(pos) == 32 || input.charAt(pos) == 9) {
                        pos += 1;
                    }
                    if (c == '-' && (flag >= 2 && flag <= 4)
                            && input.charAt(pos) != ')' && input.charAt(pos) != '*') {
                        flag = 10;
                        curToken = "-";
                    } else if (c == '-') {
                        flag = 4;
                        curToken = "-";
                    }
                }
            }
        } else if ("*x".indexOf(c) != -1) {
            pos += 1;
            if (c == 'x') {
                if (pos == input.length()) {
                    curToken = "x**" + "1";
                    flag = 6;
                    return;
                }
                while (input.charAt(pos) == 32 || input.charAt(pos) == 9) {
                    pos += 1;
                }
                if (input.charAt(pos) == '*' && input.charAt(pos + 1) == '*') {
                    pos += 2;
                    curToken = "x**" + getSignedNumber();
                } else {
                    curToken = "x**" + "1";
                }
                flag = 6;
            } else {
                flag = 5;
                curToken = "*";
            }
        } else if (input.charAt(pos++) == ')') {
            if (pos >= input.length()) {
                curToken = ")";
                flag = 9;
                return;
            }
            while (input.charAt(pos) == 32 || input.charAt(pos) == 9) {
                pos += 1;
            }
            if (input.charAt(pos) == '*' && input.charAt(pos + 1) == '*') {
                flag = 7;
                pos += 2;
                curToken = ")**" + getSignedNumber();
            }
        }
        //System.out.println(curToken + " flag:" + flag);
    }

    public String peek() {
        return this.curToken;
    }

    public int getNumberIn() {
        String s = curToken;
        int indexS = 0;
        while (!Character.isDigit(s.charAt(indexS))) {
            indexS += 1;
        }
        StringBuilder sb = new StringBuilder();
        while (indexS < s.length() && Character.isDigit(s.charAt(indexS))) {
            sb.append(s.charAt(indexS));
            ++indexS;
        }
        return Integer.parseInt(sb.toString());
    }
}

/*Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input.substring(pos));
        if (m.find()) {
            int sp = 0;
            if (input.charAt(pos) == '+') {
                sp = 1;
            } else if (input.charAt(pos) == '-') {
                sp = -1;
            }
            while (input.charAt(pos) == 32 || input.charAt(pos) == 9) {
                pos += 1;
            }
            if (input.charAt(pos) == '+') {
                sp = sp * 1;
            } else if (input.charAt(pos) == '-') {
                sp = sp * (-1);
            }
            if(sp==1){
                flag = 11;
                pos++;
                System.out.println(curToken + " flag:" + flag);
                return;
            }else if(sp==-1){
                flag = 10;
                pos++;
                System.out.println(curToken + " flag:" + flag);
                return;
            }
        }*/

/*else if ((c == '-' && (flag >= 2 && flag <= 5 || flag == 11 || flag == 10)
                        && input.charAt(pos) == 'x')) {
                    while (input.charAt(pos) == 32 || input.charAt(pos) == 9) {
                        pos += 1;
                    }
                    if (input.charAt(pos) == '*' && input.charAt(pos + 1) == '*') {
                        pos += 2;
                        curToken = "-x**" + getSignedNumber();
                    } else curToken = "-x**" + "1";
                    flag = 8;
                } */