import java.math.BigInteger;

public class Circular {
    private boolean type;
    private BigInteger coe;
    private int powin;
    private int powout;

    //三角函数内部为常数因子
    public Circular(boolean type,BigInteger coe) {
        //this.symbol = symbol;
        this.type = type;
        this.coe = coe;
        this.powin = 0;
        this.powout = 1;
    }

    //三角函数内部为幂函数
    public Circular(boolean type,int powin) {
        //this.symbol = true;
        this.type = type;
        this.coe = BigInteger.ONE;
        this.powin = powin;
        this.powout = 1;
    }
    //无脑新建

    public Circular(boolean type,BigInteger coe,int powin,int powout) {
        this.type = type;
        this.coe = coe;
        this.powin = powin;
        this.powout = powout;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean getType() {
        return this.type;
    }

    public void setCoe(BigInteger coe) {
        this.coe = coe;
    }

    public BigInteger getCoe() {
        return this.coe;
    }

    public void setPowin(int powin) {
        this.powin = powin;
    }

    public int getPowin() {
        return this.powin;
    }

    public void setPowout(int powout) {
        this.powout = powout;
    }

    public int getPowout() {
        return this.powout;
    }

    //判断三角函数内是否相同
    public boolean isEqual(Circular a) {
        if ((this.getCoe().compareTo(a.getCoe()) == 0) && this.getPowin() == a.getPowin() &&
                this.getType() == a.getType()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isEqualp(Circular a) {
        if ((this.getCoe().compareTo(a.getCoe()) == 0) && this.getPowin() == a.getPowin() &&
                this.getType() == a.getType() && this.getPowout() == a.getPowout()) {
            return true;
        }
        else {
            return false;
        }
    }
    //三角函数乘法的合并
    /*public Circular mulUp(Circular a,Circular b) {
        //若三角函数内为常数因子
        Circular cir;
        if (a.getPowin() == 0) {
            cir = new Circular(a.getType(),a.getCoe());
        }
        //三角函数因子为幂函数
        else {
            cir = new Circular(a.getType(),a.getPowin());
        }
        cir.setPowout(a.getPowout() + b.getPowout());
        return cir;
    }*/

    public void mulUp(Circular a) {
        this.setPowout(this.getPowout() + a.getPowout());
    }
}
