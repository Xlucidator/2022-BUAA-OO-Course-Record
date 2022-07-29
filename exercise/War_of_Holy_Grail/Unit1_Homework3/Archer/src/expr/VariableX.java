package expr;

import java.math.BigInteger;

public class VariableX implements Factor {
    public String toString() { return "x"; }

    public Poly toPoly() {
        Poly p = new Poly();
        p.addItem(new CoPo(BigInteger.ONE, BigInteger.ONE));
        return p;
    }
}
