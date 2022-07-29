import java.math.BigInteger;
import java.util.ArrayList;

public class Print {
    private ArrayList<Factor> funtion;
    private ArrayList<Factor> collection;

    public Print(ArrayList<Factor> funtion) {
        this.funtion = funtion;
    }

    public void out() {
        this.mergeCollection();
        int first = 0;
        for (Factor item : this.funtion) {
            if (!item.getCoe().equals(BigInteger.ZERO)) {
                if (item.getCoe().compareTo(BigInteger.ZERO) > 0 && first == 1) {
                    System.out.print("+");
                }

                first = 1;
                int flag = 0;
                if (item.getPow() == 0 && item.getCirculars().isEmpty()) {
                    System.out.print(item.getCoe());
                    flag = 1;
                } else if (item.getCoe().equals(BigInteger.ONE.negate())) {
                    System.out.print("-");
                } else if (!item.getCoe().equals(BigInteger.ONE)) {
                    System.out.print(item.getCoe());
                    flag = 1;
                }

                if (item.getPow() != 0) {
                    if (flag == 1) {
                        System.out.print("*");
                    }
                    if (item.getPow() == 1) {
                        System.out.print("x");
                    } else {
                        System.out.print("x**" + item.getPow());
                    }
                    flag = 1;
                }

                if (!item.getCirculars().isEmpty()) {
                    for (Circular cir : item.getCirculars()) {
                        if (flag == 1) {
                            System.out.print("*");
                        } else {
                            flag = 1;
                        }
                        if (cir.getType() == true) {
                            System.out.print("sin(");
                        } else {
                            System.out.print("cos(");
                        }
                        if (cir.getPowin() == 0) {
                            System.out.print(cir.getCoe());
                        } else {
                            if (cir.getPowin() == 1) {
                                System.out.print("x");
                            } else {
                                System.out.print("x**" + cir.getPowin());
                            }
                        }
                        System.out.print(")");
                        if (cir.getPowout() != 0 && cir.getPowout() != 1) {
                            System.out.print("**" + cir.getPowout());
                        }
                    }
                }
            }
        }
    }

    public void mergeCir() {
        for (Factor item : this.funtion) {
            Factor temp = item;
            ArrayList<Circular> circulars = temp.getCirculars();
            for (int i = 0;i < circulars.size();i++) {
                Circular cir1 = circulars.get(i);
                for (int j = i + 1;j < circulars.size();j++) {
                    Circular cir2 = circulars.get(j);
                    if (cir1.isEqual(cir2)) {
                        cir1.mulUp(cir2);
                        circulars.remove(j);
                        j--;
                    }
                }
            }
        }
    }

    public void mergeCollection() {
        this.mergeCir();
        for (int i = 0;i < this.funtion.size();i++) {
            Factor a = funtion.get(i);
            for (int j = i + 1;j < this.funtion.size();j++) {
                Factor b = funtion.get(j);
                if (cirsEqual(a.getCirculars(),b.getCirculars()) && a.getPow() == b.getPow()) {
                    a.setCoe(a.getCoe().add(b.getCoe()));
                    this.funtion.remove(j);
                    j--;
                }
            }
        }
    }

    public boolean cirsEqual(ArrayList<Circular> a,ArrayList<Circular> b) {
        if (a.size() == 0 && b.size() == 0) {
            return true;
        } else if (a.size() != b.size()) {
            return false;
        } else {
            boolean isContain = false;
            ArrayList<Circular> tempa = new ArrayList<>(a);
            ArrayList<Circular> tempb = new ArrayList<>(b);
            for (Circular itema : tempa) {
                for (Circular itemb : tempb) {
                    if (itemb.isEqualp(itema)) {
                        isContain = true;
                        tempb.remove(itemb);
                        break;
                    }
                }

            }
            if (tempb.size() == 0 && isContain == true) {
                return true;
            } else {
                return false;
            }
        }
    }
}
