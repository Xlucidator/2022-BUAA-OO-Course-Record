package polynomial;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Set;

public class Polynomial {
    //<index , coefficient>
    private final HashMap<BigInteger, BigInteger> powers;

    public Polynomial() {
        powers = new HashMap<>();
    }

    public Polynomial(Power power) {
        powers = new HashMap<>();
        if (!power.getCoefficient().equals(BigInteger.ZERO)) {
            powers.put(power.getIndex(), power.getCoefficient());
        }
    }

    public Polynomial(Polynomial polynomial) {
        powers = new HashMap<>();
        for (BigInteger index : polynomial.getIndexSet()) {
            powers.put(index, polynomial.getPowers().get(index));
        }
    }

    private HashMap<BigInteger, BigInteger> getPowers() {
        return this.powers;
    }

    public Set<BigInteger> getIndexSet() {
        return this.powers.keySet();
    }

    public BigInteger getCoefficient(BigInteger index) {
        return powers.get(index);
    }

    public Polynomial add(Polynomial other) {
        Polynomial poly = new Polynomial(this);
        BigInteger coefficient;
        for (BigInteger index : other.getIndexSet()) {
            coefficient = other.getCoefficient(index);
            if (poly.powers.containsKey(index)) {
                BigInteger pre = poly.powers.get(index);
                if (pre.add(coefficient).equals(BigInteger.ZERO)) {
                    poly.powers.remove(index);
                } else {
                    poly.powers.replace(index, pre.add(coefficient));
                }
            } else {
                poly.powers.put(index, coefficient);
            }
        }
        return poly;
    }

    public Polynomial sub(Polynomial other) {
        Polynomial poly = new Polynomial(this);
        for (BigInteger index : other.powers.keySet()) {
            poly = poly.add(new Polynomial(new Power(index, other.getCoefficient(index).negate())));
        }
        return poly;
    }

    public Polynomial mul(Polynomial other) {
        Polynomial poly = new Polynomial();
        if (this.powers.keySet().isEmpty() || other.getIndexSet().isEmpty()) {
            return poly;
        }
        for (BigInteger index1 : this.powers.keySet()) {
            for (BigInteger index2 : other.getIndexSet()) {
                BigInteger index = index1.add(index2);
                BigInteger coefficient =
                        this.powers.get(index1).multiply(other.getCoefficient(index2));
                poly = poly.add(new Polynomial(new Power(index, coefficient)));
            }
        }
        return poly;
    }

    public Polynomial power(BigInteger index) {
        Polynomial poly = new Polynomial(new Power(BigInteger.ZERO, BigInteger.ONE));
        for (BigInteger i = BigInteger.ZERO; i.compareTo(index) < 0; i = i.add(BigInteger.ONE)) {
            poly = poly.mul(this);
        }
        return poly;
    }

    @Override
    public String toString() {
        if (this.powers.size() == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();

        //寻找一个正项
        boolean find = false;
        BigInteger positive = BigInteger.ZERO;
        for (BigInteger index : this.powers.keySet()) {
            if (this.powers.get(index).compareTo(BigInteger.ZERO) > 0) {
                positive = index;
                find = true;
                break;
            }
        }
        if (find) {
            sb.append(new Power(positive, powers.get(positive)));
            this.powers.remove(positive);
        }

        for (BigInteger index : this.powers.keySet()) {
            sb.append(new Power(index, powers.get(index)));
        }
        return sb.charAt(0) == '+' ? sb.substring(1) : sb.toString();
    }
}
