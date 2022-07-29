import java.math.BigInteger;

public class Mul {
    public Mul(String left, String right) {
        this.left = left;
        this.right = right;
    }

    private String left;
    private String right;

    public MiniList getResult() {
        Parser pleft = new Parser();
        Parser pright = new Parser();
        return mul(pleft.operate(left), pright.operate(right));
    }

    public MiniList mul(MiniList m1, MiniList m2) {
        MiniList ans = new MiniList();
        for (int i = 0; i < 20; i++) {
            if (m1.getVariables(i) != null) {
                for (int j = 0; j < 20; j++) {
                    if (m2.getVariables(j) != null) {
                        if (ans.getVariables(i + j) == null) {
                            BigInteger t = new BigInteger(m1.getVariables(i).toString());
                            t = t.multiply(m2.getVariables(j));
                            ans.put(i + j, t);
                        } else {
                            BigInteger t = ans.getVariables(i + j);
                            BigInteger t1 = new BigInteger(m1.getVariables(i).toString());
                            t1 = t1.multiply(m2.getVariables(j));
                            t = t.add(t1);
                            ans.put(i + j, t);
                        }
                    }
                }
            }
        }
        return ans;
    }
}
