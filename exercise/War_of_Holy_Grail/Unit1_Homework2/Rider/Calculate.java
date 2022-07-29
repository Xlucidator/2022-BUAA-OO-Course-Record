import java.math.BigInteger;
import java.util.ArrayList;

public class Calculate {
    //还未合并同类项
    public ArrayList<Factor> posFun(ArrayList<Factor> a) {
        return a;
    }

    public ArrayList<Factor> negFun(ArrayList<Factor> a) {
        for (Factor item : a) {
            item.revSymbol();
        }
        return a;
    }

    public ArrayList<Factor> addFun(ArrayList<Factor> a,ArrayList<Factor> b) {
        for (Factor item : b) {
            a.add(item);
        }
        return a;
    }

    public ArrayList<Factor> subFun(ArrayList<Factor> a,ArrayList<Factor> b) {
        for (Factor item : b) {
            item.revSymbol();
            a.add(item);
        }
        return a;
    }

    public ArrayList<Factor> mulFun(ArrayList<Factor> a,ArrayList<Factor> b) {
        ArrayList<Factor> tempFun = new ArrayList<>();
        Factor temp = new Factor();
        for (Factor item1 : a) {
            for (Factor item2 : b) {
                temp = mulFac(item1,item2);
                tempFun.add(temp);
            }
        }
        return tempFun;
    }

    public ArrayList<Factor> powFun(ArrayList<Factor> a,ArrayList<Factor> b) {
        int pow = b.get(0).getCoe().intValue();
        ArrayList<Factor> tempFun = new ArrayList<>();
        if (pow == 0) {
            Factor factor = new Factor(BigInteger.ONE);
            tempFun.add(factor);
        }
        else {
            tempFun = a;
            for (int i = 1; i < pow; i++) {
                tempFun = mulFun(tempFun, a);
            }
        }
        return tempFun;
    }

    public Factor mulFac(Factor item1,Factor item2) {
        Factor temp = new Factor();
        temp.setCoe(item1.getCoe().multiply(item2.getCoe()));
        temp.setPow(item1.getPow() + item2.getPow());
        for (Circular cir : item1.getCirculars()) {
            Circular tempCir = new Circular(cir.getType(),cir.getCoe(),
                    cir.getPowin(),cir.getPowout());
            temp.addCirculars(tempCir);
        }
        for (Circular cir : item2.getCirculars()) {
            Circular tempCir = new Circular(cir.getType(),cir.getCoe(),
                    cir.getPowin(),cir.getPowout());
            temp.addCirculars(tempCir);
        }
        /*for (Circular cir : item2.getCirculars()) {
            Circular tempCir = cir;
            int flag = 0;
            for (Circular cir1 : temp.getCirculars()) {
                if (cir1.isEqual(tempCir)) {
                    cir1.mulUp(tempCir);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                temp.addCirculars(tempCir);
            }
        }*/
        return temp;
    }
}
