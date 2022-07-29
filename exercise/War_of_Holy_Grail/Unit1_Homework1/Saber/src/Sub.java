import java.math.BigInteger;

public class Sub {
    public Sub(String left, String right) {
        this.left = left;
        this.right = right;
    }

    private String left;
    private String right;

    public MiniList getResult() {
        Parser pleft = new Parser();
        Parser pright = new Parser();
        return sub(pleft.operate(left), pright.operate(right));
    }

    public MiniList sub(MiniList m1, MiniList m2) {
        MiniList ans = new MiniList();
        int i;
        for (i = 0; i < 20; i++) {
            if (m1.getVariables(i) != null) {
                ans.setVariables(i, m1.getVariables(i));
            }
        }
        for (i = 0; i < 20; i++) {
            if (m2.getVariables(i) != null) {
                if (ans.getVariables(i) == null) {
                    ans.setVariables(i, m2.getVariables(i).negate());
                } else {
                    BigInteger a = ans.getVariables(i);
                    a = a.subtract(m2.getVariables(i));
                    ans.setVariables(i, a);
                }
            }
        }
        return ans;
    }
}
