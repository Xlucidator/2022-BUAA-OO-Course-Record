import java.math.BigInteger;
import java.util.ArrayList;

public class Variable implements Factor {
    private BigInteger coefficient;
    private BigInteger power;
    private String trian = "";

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public BigInteger getPower() {
        return power;
    }

    public String getTrian() {
        return trian;
    }

    public void change(String temp) {
        this.trian = temp;
    }

    public Variable setVariable(BigInteger coefficient,BigInteger power,
                                BigInteger tpower,String t) {
        this.coefficient = coefficient;
        this.power = power;
        if (!t.equals("")) {
            this.trian = t;
            if (!tpower.toString().equals("1")) {
                this.trian = this.trian + "**" + tpower.toString();
            }
        }
        return this;
    }

    public ArrayList<Variable> cal() {
        ArrayList<Variable> temp = new ArrayList<>();
        temp.add(this);
        return temp;
    }

    public Variable mult(Variable other) {
        BigInteger co = new BigInteger("1");
        co = co.multiply(this.coefficient);
        co = co.multiply(other.coefficient);
        BigInteger po = new BigInteger("0");
        po = po.add(this.power);
        po = po.add(other.power);
        Variable temp = new Variable();
        temp.coefficient = co;
        temp.power = po;
        if (this.trian.equals("")) {
            if (!other.trian.equals("")) {
                temp.trian = other.trian;
            }
        } else {
            if (!other.trian.equals("")) {
                temp.trian = this.trian + "*" + other.trian;
            } else {
                temp.trian = this.trian;
            }
        }
        return temp;
    }

    public ArrayList<Variable> multMany(ArrayList<Variable> one,ArrayList<Variable> two) {
        ArrayList<Variable> result = new ArrayList<>();
        Variable temp;
        for (Variable i : one) {
            for (Variable j : two) {
                temp = i.mult(j);
                result.add(temp);
            }
        }
        return result;
    }

    public ArrayList<Variable> steNeg(ArrayList<Variable> one) {
        ArrayList<Variable> result = new ArrayList<>();
        for (Variable item : one) {
            BigInteger temp = item.coefficient.multiply(new BigInteger("-1"));
            item.coefficient = temp;
            result.add(item);
        }
        return result;
    }

}
