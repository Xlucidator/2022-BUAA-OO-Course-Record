import java.math.BigInteger;

public class TrigoFunc {
    private String func; //函数的类型
    private BigInteger innerCoef; //sin/cos 内部的系数 当指数不是0的时候必须是1 不是0的时候可以为整数
    private int innerIndex;  //sin/cos 内部的指数
    private int outerIndex;  //外部的指数 合并同类项当且仅当func innerCoef和index 都相等

    ///按照题目中的说法 sin/cos函数内部只可能是 x**a 或者整数的形式

    public TrigoFunc(String func,BigInteger innerCoef,int innerIndex,int outerIndex) {
        this.func = func;
        this.innerCoef = innerCoef;
        this.innerIndex = innerIndex;
        this.outerIndex = outerIndex;
    }

    public String getFunc() {
        return func;
    }

    public BigInteger getInnerCoef() {
        return innerCoef;
    }

    public int getInnerIndex() {
        return innerIndex;
    }

    public int getOuterIndex() {
        return outerIndex;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public void setInnerCoef(BigInteger innerCoef) {
        this.innerCoef = innerCoef;
    }

    public void setInnerIndex(int innerIndex) {
        this.innerIndex = innerIndex;
    }

    public void setOuterIndex(int outerIndex) {
        this.outerIndex = outerIndex;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(func).append("(");
        if (innerIndex == 0) {
            sb.append(innerCoef).append(")");
        } else if (innerIndex == 1) {
            sb.append("x").append(")");
        } else if (innerIndex > 1) {
            sb.append("x**").append(innerIndex).append(")");
        }
        return sb.toString();
    }

}
