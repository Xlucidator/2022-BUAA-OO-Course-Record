import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;

public class Adventurer implements Comparable<ValueBody>, ValueBody {
    private final int id;
    private final String name;
    private double health;
    private double exp;
    private double money;
    private final ArrayList<ValueBody> valueBodies;

    public Adventurer() {
        id = 0;
        name = "";
        health = 100.0;
        exp = money = 0.0;
        valueBodies = new ArrayList<>();
    }

    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
        this.health = 100.0;
        this.exp = 0.0;
        this.money = 0.0;
        this.valueBodies = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public ArrayList<ValueBody> getValueBodies() {
        return valueBodies;
    }

    public BigInteger getValue() {
        BigInteger totalValue = BigInteger.ZERO;
        for (ValueBody valueBody : valueBodies) {
            totalValue = totalValue.add(valueBody.getValue());
        }
        return totalValue;
    }

    @Override
    public String toString() {
        return "The adventurer's id is " + getId() +
                ", name is " + getName() +
                ", health is " + getHealth() +
                ", exp is " + getExp() +
                ", money is " + getMoney();
    }

    public void addValueBody(ValueBody valueBody) {
        this.valueBodies.add(valueBody);
    }

    public void delValueBody(int eqpId) {
        int i = 0;
        while (i < valueBodies.size() && valueBodies.get(i).getId() != eqpId) { i++; }
        if (valueBodies.get(i).getId() == eqpId) {
            valueBodies.remove(i);
        }
    }

    public BigInteger sumValue() {
        BigInteger sum = new BigInteger("0");
        for (ValueBody valueBody : valueBodies) {
            sum = sum.add(valueBody.getValue());
        }
        return sum;
    }

    public BigInteger findMaxValue() {
        BigInteger maxValue = BigInteger.ZERO;
        for (ValueBody valueBody : valueBodies) {
            if (maxValue.compareTo(valueBody.getValue()) < 0) {
                maxValue = valueBody.getValue();
            }
        }
        return maxValue;
    }

    public ValueBody findValueBody(int eqpId) {
        for (ValueBody valueBody : valueBodies) {
            if (valueBody.getId() == eqpId) {
                return valueBody;
            }
        }
        return null;
    }

    public void valueUsedBy(Adventurer user) {
        ArrayList<ValueBody> sortedValueBodies = new ArrayList<>(valueBodies);
        sortedValueBodies.sort(Comparator.reverseOrder());
        for (ValueBody valueBody : sortedValueBodies) {
            try {
                valueBody.valueUsedBy(user);    // this : object that is calling the function
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public int compareTo(ValueBody other) {
        int cmp = this.getValue().compareTo(other.getValue());
        if (cmp < 0) {
            return -1;
        } else if (cmp > 0) {
            return 1;
        }
        if (this.getId() != other.getId()) {
            return this.getId() - other.getId();
        }
        return 0;
    }
}
