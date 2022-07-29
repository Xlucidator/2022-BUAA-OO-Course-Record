import java.math.BigInteger;
import java.util.ArrayList;

public class Factor {
    //private boolean symbol;
    private BigInteger coe;
    private int pow;
    private ArrayList<Circular> circulars = new ArrayList<>();

    public Factor() {

    }
    //常数因子构造

    public Factor(BigInteger coe) {
        this.coe = coe;
        this.pow = 0;
    }
    //构造幂函数

    public Factor(int pow) {
        this.coe = BigInteger.ONE;
        this.pow = pow;
    }
    //构造三角函数
    //type为三角函数的类型

    public Factor(boolean type,Factor factor) {
        this.coe = BigInteger.ONE;
        //circulars = new ArrayList<>();
        Circular item;
        //factor为常数
        //常数的正负处理
        if (factor.getPow() == 0) {
            item = new Circular(type,factor.coe);
        }
        //factor为幂函数
        else {
            item = new Circular(type,factor.pow);
        }
        this.circulars.add(item);
    }

    public void revSymbol() {
        this.coe = this.coe.negate();
    }

    public ArrayList<Circular> getCirculars() {
        return this.circulars;
    }

    public BigInteger getCoe() {
        return this.coe;
    }

    public void setCoe(BigInteger coe) {
        this.coe = coe;
    }

    public int getPow() {
        return this.pow;
    }

    public void setPow(int pow) {
        this.pow = pow;
    }

    public void addCirculars(Circular circular) {
        this.circulars.add(circular);
    }
}
