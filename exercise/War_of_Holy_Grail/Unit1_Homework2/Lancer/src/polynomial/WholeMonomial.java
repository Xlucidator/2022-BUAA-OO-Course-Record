package polynomial;

import java.math.BigInteger;
import java.util.ArrayList;

public class WholeMonomial {
    private MonomialX monomialX;
    private ArrayList<MonomialSin> monomialSins;
    private ArrayList<MonomialCos> monomialCoses;
    private BigInteger number;
    private boolean symbolOfN;

    public WholeMonomial(MonomialX monomialX) {
        this.monomialX = monomialX;
        this.monomialSins = new ArrayList<>();
        this.monomialCoses = new ArrayList<>();
        this.number = monomialX.getNumber();
        this.symbolOfN = monomialX.getSymbolOfN();
    }

    public WholeMonomial(MonomialSin monomialSin) {
        this.monomialX = new MonomialX(0, BigInteger.ONE, true);
        this.monomialSins = new ArrayList<>();
        monomialSins.add(monomialSin);
        this.monomialCoses = new ArrayList<>();
        this.number = monomialSin.getNumber();
        this.symbolOfN = monomialSin.getSymbolOfN();
    }

    public WholeMonomial(MonomialCos monomialCos) {
        this.monomialX = new MonomialX(0, BigInteger.ONE, true);
        this.monomialCoses = new ArrayList<>();
        monomialCoses.add(monomialCos);
        this.monomialSins = new ArrayList<>();
        this.number = monomialCos.getNumber();
        this.symbolOfN = monomialCos.getSymbolOfN();
    }

    public WholeMonomial(MonomialX monomialX, ArrayList<MonomialSin> s,
                         ArrayList<MonomialCos> c, BigInteger number, boolean symbolOfN) {
        this.monomialX = monomialX;
        this.monomialSins = s;
        this.monomialCoses = c;
        this.number = number;
        this.symbolOfN = symbolOfN;
    }

    public BigInteger getNumber() {
        return this.number;
    }

    public boolean getSymbolOfN() {
        return this.symbolOfN;
    }

    public MonomialX getMonomialX() {
        return monomialX;
    }

    public ArrayList<MonomialCos> getMonomialCoses() {
        return monomialCoses;
    }

    public ArrayList<MonomialSin> getMonomialSins() {
        return monomialSins;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.number.equals(BigInteger.ZERO)) {
            return sb.toString();
        }
        if (!this.symbolOfN) {
            sb.append("-");
        }
        if (!this.number.equals(BigInteger.ONE)) {
            sb.append(this.number);
        }
        if (!this.monomialX.toString().equals("")) {
            if (!this.number.equals(BigInteger.ONE)) {
                sb.append("*");
            }
            sb.append(this.monomialX.toString());
        }
        int j = 1;
        for (MonomialSin item : this.monomialSins) {
            if (!item.toString().equals("")) {
                if (this.number.equals(BigInteger.ONE) &
                        (this.monomialX.toString().equals("") | sb.toString().equals("-")) &
                        j == 1) {
                    j = 2;
                } else {
                    sb.append("*");
                }
                sb.append(item);
            }
        }
        for (MonomialCos item : this.monomialCoses) {
            if (!item.toString().equals("")) {
                if (this.number.equals(BigInteger.ONE) &
                        (this.monomialX.toString().equals("") | sb.toString().equals("-")) &
                        j == 1) {
                    j = 2;
                } else {
                    sb.append("*");
                }
                sb.append(item);
            }
        }
        if (this.number.equals(BigInteger.ONE) &
                (sb.toString().equals("") | sb.toString().equals("-"))) {
            sb.append("1");
        }
        return sb.toString();
    }

}
