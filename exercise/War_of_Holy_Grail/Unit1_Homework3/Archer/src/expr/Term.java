package expr;

import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private ArrayList<Factor> factors;
    private ArrayList<String> signs;
    private String pnSign;

    public Term() {
        this.factors = new ArrayList<>();
        this.signs = new ArrayList<>();
        pnSign = "+";
    }

    public void changePnSign(String pnSign) {
        this.pnSign = pnSign;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public void addSign(String sign) {
        signs.add(sign);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(pnSign);
        sb.append(factors.get(0));
        for (int i = 0; i < signs.size(); i++) {
            sb.append(signs.get(i));
            sb.append(factors.get(i + 1).toString());
        }
        return sb.toString();
    }

    public Poly toPoly() {
        Poly p = new Poly();
        p.addItem(new CoPo(BigInteger.ONE, BigInteger.ZERO));
        int size = factors.size();

        for (int i = 0; i < size; i++) {
            if (i != size - 1 && signs.get(i).equals("**")) {
                Poly temp = factors.get(i).toPoly();
                if (factors.get(i + 1) instanceof Param) {
                    Param aram = (Param) factors.get(i + 1);
                    temp = temp.power((Number) aram.getFactor());
                } else {
                    temp = temp.power((Number) factors.get(i + 1));
                }
                p = p.mult(temp);
                i++;
            } else {
                p = p.mult(factors.get(i).toPoly());
            }
        }

        if (pnSign.equals("-")) {
            p = p.toNeg();
        }
        return p;
    }
}
