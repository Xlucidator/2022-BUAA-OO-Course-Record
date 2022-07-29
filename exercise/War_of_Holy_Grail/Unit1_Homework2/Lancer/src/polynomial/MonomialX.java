package polynomial;

import java.math.BigInteger;

public class MonomialX {
    private int power;
    private BigInteger number;
    private boolean symbolOfN;

    public MonomialX(int power, BigInteger number, boolean symbolOfN) {
        this.power = power;
        this.number = number;
        this.symbolOfN = symbolOfN;
    }

    public int getPower() {
        return this.power;
    }

    public BigInteger getNumber() {
        return this.number;
    }

    public boolean getSymbolOfN() {
        return this.symbolOfN;
    }

    public MonomialX add(MonomialX b) {
        if (this.symbolOfN == b.getSymbolOfN()) {
            this.number = this.number.add(b.getNumber());
        } else if (this.number.compareTo(b.getNumber()) == 0) {
            this.number = BigInteger.ZERO;
            this.symbolOfN = true;
        } else if (this.number.compareTo(b.getNumber()) == 1) {
            this.number = this.number.subtract(b.getNumber());
        } else {
            this.number = b.getNumber().subtract(this.number);
            this.symbolOfN = b.getSymbolOfN();
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.getNumber().equals(BigInteger.ZERO) | power == 0) {
            return sb.toString();
        }
        if (power == 1) {
            sb.append("x");
        } else {
            sb.append("x**");
            sb.append(power);
        }
        return sb.toString();
    }
}
