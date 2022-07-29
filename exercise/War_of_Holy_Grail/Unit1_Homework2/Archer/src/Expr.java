import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

public class Expr implements Factor {

    private int power;
    private final HashSet<Term> terms;

    public Expr() {
        this.terms = new HashSet<>();
        this.power = 1;
    }

    public void setPower(int i) {
        this.power = i;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public ArrayList<Variable> cal() {
        ArrayList<Variable> result = new ArrayList<>();
        Variable initial = new Variable().setVariable(new BigInteger("1"), new BigInteger("0"),
                new BigInteger("0"), "");
        result.add(initial);
        Variable tool = new Variable();
        ArrayList<Variable> temp = new ArrayList<>();
        for (Term item : this.terms) {
            temp.addAll(item.cal());
        }
        for (int i = 1; i <= this.power; i++) {
            result = tool.multMany(result, temp);
        }
        return result;
    }

    public void print(ArrayList<Variable> a) {
        ArrayList<Variable> result = new ArrayList<>();
        result = this.simple(a);
        String y = "";
        for (Variable item : result) {
            //System.out.println(item.getCoefficient().toString());
            //System.out.println(item.getPower().toString());
            //System.out.println(item.getTrian());
            y = y + "+";
            if (!item.getCoefficient().toString().equals("1")) {
                y = y + item.getCoefficient().toString() + "*";
            }
            if (!item.getTrian().equals("")) {
                if (!item.getPower().toString().equals("1")) {
                    if (item.getPower().toString().equals("0")) {
                        y = y + item.getTrian();
                    } else {
                        y = y + "x" + "**" + item.getPower().toString() + "*" + item.getTrian();
                    }
                } else {
                    y = y + "x" + "*" + item.getTrian();
                }

            } else {
                y = y + "x";
                if (!item.getPower().toString().equals("1")) {
                    y = y + "**" + item.getPower().toString();
                }
            }

        }
        y = y.replace("u", "sin");
        y = y.replace("v", "cos");
        if (y.equals("")) {
            y = y + "0";
        }
        y = new PreDeal().singleAdd(y);
        y = y.replace("x**0", "1");
        if (y.charAt(0) == '+') {
            y = y.substring(1, y.length());
        }
        y = y.replace("((","(");
        y = y.replace("))",")");
        System.out.println(y);
    }

    public ArrayList<Variable> simple(ArrayList<Variable> a) {
        ArrayList<Variable> number = new ArrayList<>();
        ArrayList<Variable> trian = new ArrayList<>();
        ArrayList<Variable> result = new ArrayList<>();
        Variable initial = new Variable().setVariable(new BigInteger("0"),
                new BigInteger("0"), new BigInteger("0"), "");
        result.add(initial);
        int ok = 0;
        for (Variable item : a) {
            if (item.getTrian().equals("")) {
                number.add(item);
            } else {
                trian.add(item);
            }
        }

        for (Variable item : number) {
            ok = 0;
            for (Variable i : result) {
                if (i.getPower().equals(item.getPower())) {
                    BigInteger big = new BigInteger("0");
                    big = big.add(i.getCoefficient());
                    big = big.add(item.getCoefficient());
                    result.remove(i);
                    Variable temp = new Variable().setVariable(big,
                            i.getPower(), new BigInteger("0"), "");
                    result.add(temp);
                    ok = 1;
                    break;
                }
            }
            if (ok == 0) {
                result.add(item);
            }
        }
        result.remove(initial);
        trian = this.dealTrian(trian);
        result.addAll(trian);
        return result;
    }

    public ArrayList<Variable> dealTrian(ArrayList<Variable> a) {
        String temp;
        ArrayList<Variable> fine = new ArrayList<>();
        for (Variable item : a) {
            temp = item.getTrian().toString().replace("**", "<");
            String[] singleGroup = temp.split("\\*");
            for (int i = 0; i < singleGroup.length; i++) {
                for (int j = i + 1; j < singleGroup.length; j++) {
                    if (!singleGroup[i].equals("") && !singleGroup[j].equals("")) {
                        String[] tool1 = singleGroup[i].split("<");
                        String[] tool2 = singleGroup[j].split("<");
                        if (tool1[0].equals(tool2[0])) {
                            singleGroup[j] = "";
                            BigInteger big = new BigInteger("0");
                            if (tool1.length == 1) {
                                big = big.add(new BigInteger("1"));
                            } else {
                                big = big.add(new BigInteger(tool1[1]));
                            }
                            if (tool2.length == 1) {
                                big = big.add(new BigInteger("1"));
                            } else {
                                big = big.add(new BigInteger(tool2[1]));
                            }
                            singleGroup[i] = tool1[0] + "<" + big.toString();
                        }
                    }

                }
            }
            String newTri = singleGroup[0];
            for (int i = 1; i < singleGroup.length; i++) {
                if (!singleGroup[i].equals("")) {
                    newTri = newTri + "*" + singleGroup[i];
                }
            }
            newTri = newTri.replace("<", "**");
            item.change(newTri);
            fine.add(item);
        }
        return fine;
    }
}
