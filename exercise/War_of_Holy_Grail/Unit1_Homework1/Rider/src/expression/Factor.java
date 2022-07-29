package expression;

import java.math.BigInteger;

public class Factor implements Comparable<Factor> {
    private int power;
    private Base base;
    private Expression complexBase;

    public Factor() {
        this.power = 1;
        this.base = new Number(BigInteger.ZERO);
    }

    public Factor(int power, Base base) {
        this.power = power;
        this.base = base;
    }

    public int getPower() {
        return power;
    }

    public Base getBase() {
        return base;
    }

    public Expression getComplexBase() {
        return complexBase;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public int compareTo(Factor another) {
        return ((Variable) base).getName()
               .compareTo(((Variable) another.base).getName());
    }

    public void addBase(BaseExpression expressionBase) {
        if (expressionBase.getClass() == Number.class ||
            expressionBase.getClass() == Variable.class) {  // Number or Variable
            base = (Base) expressionBase;
        } else {
            complexBase = (Expression) expressionBase;
        }
    }

    public void addPower(int power) {
        if (power == 0) {
            this.power = 1;
            base = new Number(BigInteger.ONE);
            complexBase = null;
        }
        else {
            if (complexBase == null) {
                this.power = power;
            }
            else {  // unfold complexBase
                this.power = 1;
                Expression expression = new Expression(BigInteger.ONE);
                for (int i = 0;i < power;i++) {
                    expression.expressionMult(complexBase);
                }
                complexBase = expression;
            }
        }
    }

    @Override
    public String toString() {
        if (power == 1) {
            return base.toString();
        }
        if (power == 2) {
            return base.toString() + "*" + base.toString();
        }
        return base.toString() + "**" + power;
    }
}
