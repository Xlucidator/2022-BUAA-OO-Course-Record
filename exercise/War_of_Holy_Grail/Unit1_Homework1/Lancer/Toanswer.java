import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Toanswer {
    @SuppressWarnings("checkstyle:LineLength")
    public String end(HashMap<Integer, BigInteger> hashMap) {
        String ans;
        ans = "";
        for (Map.Entry<Integer, BigInteger> entry : hashMap.entrySet()) {
            if (!entry.getValue().equals(BigInteger.valueOf(0))) {
                if (entry.getValue().compareTo(BigInteger.valueOf(0)) < 0) {
                    //负的系数
                    if (entry.getValue().equals(BigInteger.valueOf(-1))) {
                        if (entry.getKey() == 0) {
                            ans = ans + "-" + "1";
                        } else if (entry.getKey() == 1) {
                            ans = ans + "-" + "x";
                        } else if (entry.getKey() == 2) {
                            ans = ans + "-" + "x*x";
                        } else {
                            ans = ans + "-" + "x" + "**" + entry.getKey().toString();
                        }
                    } else {
                        if (entry.getKey() == 0) {
                            ans = ans + entry.getValue().toString();
                        } else if (entry.getKey() == 1) {
                            ans = ans + entry.getValue().toString() + "*x";
                        } else if (entry.getKey() == 2) {
                            ans = ans + entry.getValue().toString() + "*x*x";
                        } else {
                            ans = ans + entry.getValue().toString() +
                                    "*x" + "**" + entry.getKey().toString();
                        }
                    }
                } else {
                    if (entry.getValue().equals(BigInteger.valueOf(1))) {
                        if (entry.getKey() == 0) {
                            ans = ans + "+" + "1";
                        } else if (entry.getKey() == 1) {
                            ans = ans + "+" + "x";
                        } else if (entry.getKey() == 2) {
                            ans = ans + "+" + "x*x";
                        } else {
                            ans = ans + "+" + "x" + "**" + entry.getKey().toString();
                        }
                    } else {
                        if (entry.getKey() == 0) {
                            ans = ans + "+" + entry.getValue().toString();
                        } else if (entry.getKey() == 1) {
                            ans = ans + "+" + entry.getValue().toString() + "*x";
                        } else if (entry.getKey() == 2) {
                            ans = ans + "+" + entry.getValue().toString() + "*x*x";
                        } else {
                            ans = ans + "+" + entry.getValue().toString() +
                                    "*x" + "**" + entry.getKey().toString();
                        }
                    }
                }
            }
        }

        return ans;
    }
}
