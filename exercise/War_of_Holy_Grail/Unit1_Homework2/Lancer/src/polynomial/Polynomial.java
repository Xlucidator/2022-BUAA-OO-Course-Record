package polynomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Polynomial {
    private ArrayList<WholeMonomial> wholeMonomials;

    public Polynomial() {
        this.wholeMonomials = new ArrayList<>();
    }

    public void addMonomial(WholeMonomial wholeMonomial) {
        this.wholeMonomials.add(wholeMonomial);
    }

    public int getSize() {
        return this.wholeMonomials.size();
    }

    public Polynomial multiply(Polynomial a, Polynomial b) {
        Polynomial result = new Polynomial();
        for (WholeMonomial theA : a.wholeMonomials) {
            for (WholeMonomial theB : b.wholeMonomials) {
                result.addMonomial(mul(theA, theB));
            }
        }
        return result;
    }

    public WholeMonomial mul(WholeMonomial a, WholeMonomial b) {
        final boolean toC = (a.getSymbolOfN() == b.getSymbolOfN());

        final MonomialX monomialX = new MonomialX(a.getMonomialX().getPower() +
                b.getMonomialX().getPower(),
                a.getMonomialX().getNumber().multiply(b.getMonomialX().getNumber()),
                a.getMonomialX().getSymbolOfN() == b.getMonomialX().getSymbolOfN());
        ArrayList<MonomialSin> monomialSins = new ArrayList<>();
        monomialSins.addAll(a.getMonomialSins());
        monomialSins.addAll(b.getMonomialSins());
        ArrayList<MonomialCos> monomialCos = new ArrayList<>();
        monomialCos.addAll(a.getMonomialCoses());
        monomialCos.addAll(b.getMonomialCoses());
        BigInteger num = a.getNumber().multiply(b.getNumber());
        return new WholeMonomial(monomialX, monomialSins, monomialCos, num, toC);
    }

    public Polynomial addPolynomial(Polynomial a, Polynomial b) {
        Polynomial result1 = new Polynomial();
        for (WholeMonomial theA : a.wholeMonomials) {
            result1.addMonomial(theA);
        }
        for (WholeMonomial theB : b.wholeMonomials) {
            result1.addMonomial(theB);
        }
        return result1;
    }

    /*public Polynomial add() {
        ArrayList<Integer> done = new ArrayList<>();
        Polynomial result = new Polynomial();
        for (int i = 0; i < this.getSize(); i++) {
            int thisPow = this.monomials.get(i).getPower();
            if (!done.contains(thisPow)) {
                done.add(thisPow);
                MonomialX monomial = new MonomialX(thisPow, BigInteger.ZERO, true);
                for (int j = i; j < this.getSize(); j++) {
                    if (this.monomials.get(j).getPower() == thisPow) {
                        monomial = monomial.add(this.monomials.get(j));
                    }
                }
                result.addMonomial(monomial);
            }
        }
        return result;
    }*/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<WholeMonomial> iter = wholeMonomials.iterator();
        if (iter.hasNext()) {
            sb.append(iter.next().toString());
        }
        while (iter.hasNext()) {
            String s = iter.next().toString();
            if (!s.equals("")) {
                sb.append("+");
                sb.append(s);
            }
        }
        if (sb.toString().length() == 0) {
            sb.append("0");
        }
        return sb.toString();
    }
}
