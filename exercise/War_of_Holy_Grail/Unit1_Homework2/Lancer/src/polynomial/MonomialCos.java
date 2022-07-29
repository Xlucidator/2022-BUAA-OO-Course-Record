package polynomial;

import java.math.BigInteger;

public class MonomialCos extends MonomialTri {
    public MonomialCos(int power, BigInteger number, boolean symbolOfN, Polynomial polynomial) {
        super(power, number, symbolOfN, polynomial);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.getPower() == 0 | this.getNumber().equals(BigInteger.ZERO)) {
            return sb.toString();
        } else {
            sb.append("cos(");
            sb.append(this.getPolynomial().toString());
            sb.append(")");
            if (this.getPower() != 1) {
                sb.append("**");
                sb.append(this.getPower());
            }
        }
        return sb.toString();
    }
}
