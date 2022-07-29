package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Poly {
    private ArrayList<CoPo> items;

    public boolean equal(Poly other) {
        Poly pthis = new Poly();
        Poly pother = new Poly();

        for (CoPo cp : this.getAllItems()) {
            if (cp.getCoefficient().compareTo(BigInteger.ZERO) != 0) {
                pthis.addItem(cp);
            }
        }

        for (CoPo cp : other.getAllItems()) {
            if (cp.getCoefficient().compareTo(BigInteger.ZERO) != 0) {
                pother.addItem(cp);
            }
        }

        if (pthis.getAllItems().size() != pother.getAllItems().size()) {
            return false;
        } else {
            HashMap<CoPo, Integer> thisMap = new HashMap<>();
            HashMap<CoPo, Integer> otherMap = new HashMap<>();
            ArrayList<CoPo> thisArray = pthis.getAllItems();
            ArrayList<CoPo> otherArray = pother.getAllItems();
            for (CoPo cp : thisArray) {
                thisMap.put(cp, 0);
            }
            for (CoPo cp : otherArray) {
                otherMap.put(cp, 0);
            }
            out: for (CoPo tcp : thisArray) {
                for (CoPo ocp : otherArray) {
                    if (thisMap.get(tcp).equals(0) && otherMap.get(ocp).equals(0) &&
                            tcp.equal(ocp)) {
                        thisMap.replace(tcp, 1);
                        otherMap.replace(ocp, 1);
                        continue out;
                    }
                }
                return false;
            }
            return true;
        }
    }

    public Poly() {
        items = new ArrayList<>();
    }

    public Poly(CoPo cp) {
        items = new ArrayList<>();
        items.add(cp);
    }

    public ArrayList<CoPo> getAllItems() {
        return this.items;
    }

    public CoPo getItem(int i) { return this.items.get(i); }

    public void addItem(CoPo cp) { items.add(cp); }

    public Poly add(Poly other) {
        Poly p = new Poly();
        p.getAllItems().addAll(this.items);
        p.getAllItems().addAll(other.items);
        return p;
    }

    public Poly toNeg() {
        Poly p = new Poly();
        for (CoPo cp : this.items) {
            CoPo c = new CoPo(cp.getCoefficient().negate(), cp.getPowerX());
            c.setTriItems(cp.getTriItems());
            p.addItem(c);
        }
        return p;
    }

    public Poly sub(Poly other) {
        return this.add(other.toNeg());
    }

    public Poly mult(Poly other) {
        Poly p = new Poly();
        for (CoPo cp1 : this.items) {
            for (CoPo cp2 : other.items) {
                CoPo cp = new CoPo(cp1.getCoefficient().multiply(cp2.getCoefficient()),
                        cp1.getPowerX().add(cp2.getPowerX()));
                cp.setTriItems(cp1.getTriItems());
                cp.addTriItems(cp2.getTriItems());
                p.addItem(cp);
            }
        }
        return p;
    }

    public Poly power(Number num) {
        Poly p = new Poly();
        p.addItem(new CoPo(BigInteger.ONE, BigInteger.ZERO));
        if (num.getNum().compareTo(BigInteger.ZERO) == 0) {
            return p;
        } else {
            BigInteger i = BigInteger.ZERO;
            while (i.compareTo(num.getNum()) < 0) {
                p = p.mult(this);
                i = i.add(BigInteger.ONE);
            }
            return p;
        }
    }

    public Poly simplify1(Poly p) {
        for (CoPo cp : p.items) {
            ArrayList<TriItem> nts = new ArrayList<>();
            ArrayList<TriItem> ts = cp.getTriItems();
            while (cp.sizeTriItems() != 0) {
                // 指向cp的三角函数数组
                TriItem t = new TriItem(ts.get(0).getPower(),
                        ts.get(0).getType(), ts.get(0).getContent());
                ts.remove(0);
                for (int i = 0; !ts.isEmpty() && i < ts.size(); ) {
                    if (t.getType().equals(ts.get(i).getType()) &&
                            t.getContent().equal(ts.get(i).getContent())) {
                        t.setPower(t.getPower().add(ts.get(i).getPower()));
                        ts.remove(i);
                    } else {
                        i++;
                    }
                }
                nts.add(t);
            }
            cp.setTriItems(nts);
        }
        return p;
    }

    public Poly simplify2(Poly p1) {
        Poly p2 = new Poly();
        for (CoPo cp : p1.items) {
            CoPo ncp = new CoPo(cp.getCoefficient(), cp.getPowerX());
            ArrayList<TriItem> nts = new ArrayList<>();
            ArrayList<TriItem> ts = cp.getTriItems();
            boolean add = true;
            for (TriItem t : ts) {
                if (t.getContent().equal(new Poly())) {
                    if (t.getType().equals("sin")) {
                        add = false;
                        break;
                    } else if (t.getType().equals("cos")) {
                        continue;
                    }
                } else {
                    nts.add(t);
                }
            }
            if (add == true) {
                ncp.setTriItems(nts);
                p2.addItem(ncp);
            }
        }
        return p2;
    }

    public Poly simplify3Cos(Poly p2) {
        boolean noSinSquare = false;

        while (noSinSquare == false) {
            noSinSquare = true;
            out: for (CoPo cp : p2.items) {
                ArrayList<TriItem> ts = cp.getTriItems();
                in: for (int i = 0; i < ts.size(); i++) {
                    if (ts.get(i).getType().equals("sin") &&
                            ts.get(i).getPower().compareTo(BigInteger.valueOf(2)) == 0) {
                        TriItem nt = new TriItem(BigInteger.valueOf(2), "cos",
                                ts.get(i).getContent());
                        cp.removeTriItem(i);
                        CoPo nc = new CoPo(cp.getCoefficient().negate(), cp.getPowerX());
                        nc.addTriItems(cp.getTriItems());
                        nc.addTriItem(nt);
                        p2.addItem(nc);

                        noSinSquare = false;
                        break out;
                    }
                }
            }
        }
        return p2;
    }

    public Poly simplify3Sin(Poly p2) {
        boolean noCosSquare = false;

        while (noCosSquare == false) {
            noCosSquare = true;
            out: for (CoPo cp : p2.items) {
                ArrayList<TriItem> ts = cp.getTriItems();
                in: for (int i = 0; i < ts.size(); i++) {
                    if (ts.get(i).getType().equals("cos") &&
                            ts.get(i).getPower().compareTo(BigInteger.valueOf(2)) == 0) {
                        TriItem nt = new TriItem(BigInteger.valueOf(2), "sin",
                                ts.get(i).getContent());
                        cp.removeTriItem(i);
                        CoPo nc = new CoPo(cp.getCoefficient().negate(), cp.getPowerX());
                        nc.addTriItems(cp.getTriItems());
                        nc.addTriItem(nt);
                        p2.addItem(nc);

                        noCosSquare = false;
                        break out;
                    }
                }
            }
        }
        return p2;
    }

    public Poly simplify4(Poly p3) {
        Poly p4 = new Poly();
        while (!p3.items.isEmpty()) {
            BigInteger coefficient = p3.items.get(0).getCoefficient();
            BigInteger powerX = p3.items.get(0).getPowerX();
            CoPo c = new CoPo(coefficient, powerX);
            c.setTriItems(p3.items.get(0).getTriItems());
            p3.items.remove(0);

            for (int i = 0; !p3.items.isEmpty() && i < p3.items.size(); ) {
                if (c.polymeriza(p3.items.get(i))) {
                    c.setCoefficient(c.getCoefficient().add(p3.items.get(i).getCoefficient()));
                    p3.items.remove(i);
                } else {
                    i++;
                }
            }
            p4.addItem(c);
        }
        return p4;
    }

    public Poly simplifyCos() {
        Poly p;
        p = simplify1(this);
        p = simplify2(p);
        p = simplify3Cos(p);
        p = simplify1(p);
        p = simplify4(p);
        for (CoPo cp : p.items) {
            if (cp.getCoefficient().compareTo(BigInteger.ZERO) > 0) {
                p.items.remove(cp);
                p.items.add(0, cp);
                break;
            }
        }
        return p;
    }

    public Poly simplifySin() {
        Poly p;
        p = simplify1(this);
        p = simplify2(p);
        p = simplify3Sin(p);
        p = simplify1(p);
        p = simplify4(p);
        for (CoPo cp : p.items) {
            if (cp.getCoefficient().compareTo(BigInteger.ZERO) > 0) {
                p.items.remove(cp);
                p.items.add(0, cp);
                break;
            }
        }
        return p;
    }

    public Poly simplifyNone() {
        Poly p;
        p = simplify1(this);
        p = simplify2(p);
        p = simplify4(p);
        for (CoPo cp : p.items) {
            if (cp.getCoefficient().compareTo(BigInteger.ZERO) > 0) {
                p.items.remove(cp);
                p.items.add(0, cp);
                break;
            }
        }
        return p;
    }

    public Poly simplify() {
        Poly p = this.add(new Poly());
        for (CoPo cp : p.getAllItems()) {
            for (TriItem t : cp.getTriItems()) {
                t.setContent(t.getContent().simplify());
            }
        }
        Poly p1 = p.simplifySin();
        Poly p2 = p.simplifyCos();
        Poly p3 = p.simplifyNone();
        String str1 = p1.toString();
        String str2 = p2.toString();
        String str3 = p3.toString();
        if (str1.length() < str2.length()) {
            if (str1.length() < str3.length()) {
                return p1;
            } else {
                return p3;
            }
        } else {
            if (str2.length() < str3.length()) {
                return p2;
            } else {
                return p3;
            }
        }
    }

    public boolean isExpression(Poly temp) {
        boolean isExpr;
        if (temp.getAllItems().size() == 0) {
            isExpr = false;
        } else if (temp.getAllItems().size() == 1) {
            if (temp.getItem(0).getPowerX().compareTo(BigInteger.ZERO) == 0 &&
                    temp.getItem(0).sizeTriItems() == 0) {
                isExpr = false;
            } else if (temp.getItem(0).getCoefficient().compareTo(BigInteger.ONE) == 0 &&
                    temp.getItem(0).getPowerX().compareTo(BigInteger.ZERO) != 0 &&
                    temp.getItem(0).sizeTriItems() == 0) {
                isExpr = false;
            } else if (temp.getItem(0).getCoefficient().compareTo(BigInteger.ONE) == 0 &&
                    temp.getItem(0).getPowerX().compareTo(BigInteger.ZERO) == 0 &&
                    temp.getItem(0).sizeTriItems() == 1) {
                isExpr = false;
            } else {
                isExpr = true;
            }
        } else {
            isExpr = true;
        }
        return isExpr;
    }

    public String toTriString(boolean isExpr) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        Poly temp;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getCoefficient().compareTo(BigInteger.ZERO) != 0) {
                if (firstTime == true) {
                    if (items.get(i).getCoefficient().compareTo(BigInteger.ZERO) < 0) {
                        sb.append("-");
                    }
                    firstTime = false;
                } else {
                    if (items.get(i).getCoefficient().compareTo(BigInteger.ZERO) < 0) {
                        sb.append("-");
                    } else if (items.get(i).getCoefficient().compareTo(BigInteger.ZERO) > 0) {
                        sb.append("+");
                    }
                }
                if (items.get(i).getCoefficient().abs().compareTo(BigInteger.ONE) != 0 ||
                        (items.get(i).getPowerX().compareTo(BigInteger.ZERO) == 0 &&
                                items.get(i).getTriItems().isEmpty())) {
                    // 如果系数不为1，或没有x项且tri项，要输出系数
                    sb.append(items.get(i).getCoefficient().abs());
                }
                if (items.get(i).getPowerX().compareTo(BigInteger.ZERO) > 0) {
                    if (items.get(i).getCoefficient().abs().compareTo(BigInteger.ONE) != 0) {
                        // 如果有x项，且前面输出了系数(也即系数不为1),要加乘号
                        sb.append("*");
                    }
                    sb.append("x");
                    if (items.get(i).getPowerX().compareTo(BigInteger.ONE) > 0) {
                        if (items.get(i).getPowerX().equals(new BigInteger("2")) &&
                                isExpr == true) {
                            sb.append("*x");
                        } else {
                            sb.append("**" + items.get(i).getPowerX());
                        }
                    }
                }
                if (!items.get(i).getTriItems().isEmpty()) {
                    appendFirstPartOfTri(sb, items, i);
                    for (int j = 1; j < items.get(i).sizeTriItems(); j++) {
                        temp = items.get(i).getTriItem(j).getContent();
                        if (isExpression(temp) == true) {
                            sb.append("*" + items.get(i).getTriItem(j).getType() + "((" +
                                    items.get(i).getTriItem(j).getContent().toTriString(true) +
                                    "))");
                        } else {
                            sb.append("*" + items.get(i).getTriItem(j).getType() + "(" +
                                    items.get(i).getTriItem(j).getContent().toTriString(false) +
                                    ")");
                        }
                        if (items.get(i).getTriItem(j).getPower().compareTo(BigInteger.ONE) > 0) {
                            sb.append("**" + items.get(i).getTriItem(j).getPower());
                        }
                    }
                }
            }
        }
        if (sb.toString().equals("")) { sb.append("0"); }
        return sb.toString();
    }

    public void appendFirstPartOfTri(StringBuilder sb, ArrayList<CoPo> items, int i) {
        Poly temp;
        if (items.get(i).getCoefficient().abs().compareTo(BigInteger.ONE) != 0 ||
                items.get(i).getPowerX().compareTo(BigInteger.ZERO) > 0) {
            // 如果前面有x项，或者前面输出了系数(即系数不为1)，则要输出乘号
            sb.append("*");
        }
        temp = items.get(i).getTriItem(0).getContent();

        if (isExpression(temp) == true) {
            sb.append(items.get(i).getTriItem(0).getType() + "((" +
                    items.get(i).getTriItem(0).getContent().toTriString(true) + "))");
        } else {
            sb.append(items.get(i).getTriItem(0).getType() + "(" +
                    items.get(i).getTriItem(0).getContent().toTriString(false) + ")");
        }

        if (items.get(i).getTriItem(0).getPower().compareTo(BigInteger.ONE) > 0) {
            sb.append("**" + items.get(i).getTriItem(0).getPower());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        Poly temp;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getCoefficient().compareTo(BigInteger.ZERO) != 0) {
                if (firstTime == true) {
                    if (items.get(i).getCoefficient().compareTo(BigInteger.ZERO) < 0) {
                        sb.append("-");
                    }
                    firstTime = false;
                } else {
                    if (items.get(i).getCoefficient().compareTo(BigInteger.ZERO) < 0) {
                        sb.append("-");
                    } else if (items.get(i).getCoefficient().compareTo(BigInteger.ZERO) > 0) {
                        sb.append("+");
                    }
                }
                if (items.get(i).getCoefficient().abs().compareTo(BigInteger.ONE) != 0 ||
                        (items.get(i).getPowerX().compareTo(BigInteger.ZERO) == 0 &&
                                items.get(i).getTriItems().isEmpty())) {
                    // 如果系数不为1，或没有x项且tri项，要输出系数
                    sb.append(items.get(i).getCoefficient().abs());
                }
                if (items.get(i).getPowerX().compareTo(BigInteger.ZERO) > 0) {
                    if (items.get(i).getCoefficient().abs().compareTo(BigInteger.ONE) != 0) {
                        // 如果有x项，且前面输出了系数(也即系数不为1),要加乘号
                        sb.append("*");
                    }
                    sb.append("x");
                    if (items.get(i).getPowerX().compareTo(BigInteger.ONE) > 0) {
                        if (items.get(i).getPowerX().equals(new BigInteger("2"))) {
                            sb.append("*x");
                        } else {
                            sb.append("**" + items.get(i).getPowerX());
                        }
                    }
                }
                if (!items.get(i).getTriItems().isEmpty()) {
                    appendFirstPartOfTri(sb, items, i);
                    for (int j = 1; j < items.get(i).sizeTriItems(); j++) {
                        temp = items.get(i).getTriItem(j).getContent();
                        if (isExpression(temp) == true) {
                            sb.append("*" + items.get(i).getTriItem(j).getType() + "((" +
                                    items.get(i).getTriItem(j).getContent().toTriString(true) +
                                    "))");
                        } else {
                            sb.append("*" + items.get(i).getTriItem(j).getType() + "(" +
                                    items.get(i).getTriItem(j).getContent().toTriString(false) +
                                    ")");
                        }
                        if (items.get(i).getTriItem(j).getPower().compareTo(BigInteger.ONE) > 0) {
                            sb.append("**" + items.get(i).getTriItem(j).getPower());
                        }
                    }
                }
            }
        }
        if (sb.toString().equals("")) { sb.append("0"); }
        return sb.toString();
    }
}
