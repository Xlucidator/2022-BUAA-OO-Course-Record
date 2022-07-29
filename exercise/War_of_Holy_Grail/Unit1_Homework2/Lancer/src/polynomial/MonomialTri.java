package polynomial;

import java.math.BigInteger;

public class MonomialTri {
    private int power;
    private BigInteger number;
    private boolean symbolOfN;
    private Polynomial polynomial;

    public MonomialTri(int power, BigInteger number, boolean symbolOfN, Polynomial polynomial) {
        this.power = power;
        this.number = number;
        this.symbolOfN = symbolOfN;
        this.polynomial = polynomial;
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

    @Override
    public String toString() {
        return null;
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }
}
