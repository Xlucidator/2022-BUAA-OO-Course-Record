import java.math.BigInteger;
import java.util.ArrayList;

public class Poly {
    private ArrayList<Mono> monos;
    private int index;

    public Poly(ArrayList<Mono> monos,int index) {
        this.monos = monos;
        this.index = index;
    }

    public Poly() {
        Mono mono = new Mono();
        ArrayList<Mono> tmp = new ArrayList<>();
        tmp.add(mono);
        monos = tmp;
        this.index = 1;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Mono> getMonos() {
        if (monos == null) {
            return new ArrayList<>();
        }
        return monos;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setMonos(ArrayList<Mono> monos) {
        this.monos = monos;
    }

    public Poly mergePoly(ArrayList<Mono> a,ArrayList<Mono> b) {
        ArrayList<Mono> merge = new ArrayList<>();
        merge.addAll(a);
        merge.addAll(b);
        return new Poly(merge,1);
    }

    public Poly addPoly(Poly other) {
        return mergePoly(this.getMonos(),other.getMonos());
    } //不考虑合并同类项 直接将Poly叠加即可

    public Poly multiPoly(Poly other) {
        ArrayList<Mono> otherAL = other.getMonos();
        ArrayList<Mono> thisAL = this.getMonos();
        ArrayList<Mono> res = new ArrayList<>();
        for (Mono thisMono : thisAL) {
            for (Mono otherMono : otherAL) {
                Mono tmp = thisMono.multiMono(otherMono);
                res.add(tmp);
            }
        }
        return new Poly(res,1);
    }

    public Poly powPoly(int index) {
        if (index == 0) {
            Mono tmp = new Mono();
            tmp.setCoef(BigInteger.valueOf(1));
            tmp.setIndex(0);
            return tmp.toPoly();
        }
        Poly unit = new Poly(this.getMonos(),1);
        Poly res = new Poly(this.getMonos(),1);
        for (int i = 1; i < index; i++) {
            res = res.multiPoly(unit);
        }
        return res;
    }

    public Poly getOpposite() {
        ArrayList<Mono> res = new ArrayList<>();
        for (Mono mono : monos) {
            mono.setCoef(mono.getCoef().negate());
            res.add(mono);
        }
        return new Poly(res,1);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Mono mono : monos) {
            sb.append(mono.toString());
        }
        return sb.toString();
    }
}
