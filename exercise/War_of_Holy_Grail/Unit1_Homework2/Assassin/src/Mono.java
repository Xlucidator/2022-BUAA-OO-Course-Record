import java.math.BigInteger;
import java.util.ArrayList;

public class Mono {
    private BigInteger coef; //系数
    private int index; //指数
    private ArrayList<TrigoFunc> sin; // 关于sin函数的集合 在toString中为乘积关系
    private ArrayList<TrigoFunc> cos; // 关于cos函数的集合 在toString中为乘积关系

    public Mono() {
        this.coef = BigInteger.ZERO;
        this.index = 0;
        this.sin = new ArrayList<>();
        this.cos = new ArrayList<>();
    } ///默认类型的构造 只需要将系数设置成0

    public Mono(BigInteger coef,int index,
                ArrayList<TrigoFunc> sin,ArrayList<TrigoFunc> cos) {
        this.coef = coef;
        this.index = index;
        this.sin = sin;
        this.cos = cos;
    } ////对于指定类型数的构造

    public BigInteger getCoef() {
        return coef;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<TrigoFunc> getSin() {
        return sin;
    }

    public ArrayList<TrigoFunc> getCos() {
        return cos;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCoef(BigInteger coef) {
        this.coef = coef;
    }

    public ArrayList<TrigoFunc> mergeList(ArrayList<TrigoFunc> a, ArrayList<TrigoFunc> b) {
        ArrayList<TrigoFunc> merge = new ArrayList<>();
        merge.addAll(a);
        merge.addAll(b);
        return merge;
    } ////将两个ArrayList相加 对于三角函数相乘 如果不用考虑去重的话 直接将两个ArrayList叠在一起就行

    public Mono multiMono(Mono other) {
        if (this.coef.multiply(other.getCoef()).equals(BigInteger.ZERO)) {
            return new Mono();  ///对于Mono相乘 coef是0这一项就是0
        } else {
            BigInteger newCoef = this.coef.multiply(other.getCoef()); //系数相乘
            int newIndex = this.getIndex() + other.getIndex(); //指数相加
            ArrayList<TrigoFunc> newSin;
            if (this.getSin() == null && other.getSin() == null) {
                newSin = new ArrayList<>();
            } else if (this.getSin() == null && other.getSin() != null) {
                newSin = other.getSin();
            } else if (this.getSin() != null && other.getSin() == null) {
                newSin = this.getSin();
            } else {
                newSin = mergeList(this.getSin(), other.getSin());
            }
            ArrayList<TrigoFunc> newCos;
            if (this.getCos() == null && other.getCos() == null) {
                newCos = new ArrayList<>();
            } else if (this.getCos() == null && other.getCos() != null) {
                newCos = other.getCos();
            } else if (this.getCos() != null && other.getCos() == null) {
                newCos = this.getCos();
            } else {
                newCos = mergeList(this.getCos(), other.getCos());
            }
            return new Mono(newCoef,newIndex,newSin,newCos);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (coef.compareTo(BigInteger.ZERO) == 0) {
            return "+0";
        }
        if (coef.compareTo(BigInteger.valueOf(0)) > 0) {
            sb.append("+");
        } //如果是正数的话要在前面把+补上
        if (sin.size() == 0 && cos.size() == 0 && index == 0) {
            sb.append(coef);
            return sb.toString();
        } //如果sin和cos为空 系数是0的话就必须完整的输出coef
        sb.append(coef);
        //index如果是0就直接不用输出幂函数的项
        if (index == 1) {
            sb.append("*x");//是1就不用输出指数部分
        } else if (index == 2) {
            sb.append("*x*x"); //2的话进行特殊处理
        } else if (index > 2) {
            sb.append("*x**").append(index); //大于2进行一般化的处理
        }
        if (sin.size() > 0) {
            for (TrigoFunc t : sin) {
                sb.append("*").append(t.toString());
            }
        }
        if (cos.size() > 0) {
            for (TrigoFunc t : cos) {
                sb.append("*").append(t.toString());
            }
        }
        return sb.toString();
    }

    public Poly toPoly() {
        ArrayList<Mono> res = new ArrayList<>();
        res.add(this);
        return new Poly(res,1);
    }

}




    /*public String toString() {
        StringBuilder sb = new StringBuilder();
        if (coef.compareTo(BigInteger.ZERO) == 0) {
            return null;
        }
        if (coef.compareTo(BigInteger.valueOf(0)) > 0) {
            sb.append("+");
        } //如果是正数的话要在前面把+补上
        if (sin == null && cos == null && index == 0) {
            sb.append(coef);
            return sb.toString();
        } //如果sin和cos为空 系数是0的话就必须完整的输出coef
        if (index == 0 &&
                (coef.equals(BigInteger.valueOf(-1)) || coef.equals(BigInteger.valueOf(1)))) {
            StringBuilder tmp = new StringBuilder();
            if (sin != null) {
                for (TrigoFunc t : sin) {
                    tmp.append("*").append(t.toString());
                }
            }
            if (cos != null) {
                for (TrigoFunc t : cos) {
                    tmp.append("*").append(t.toString());
                }
            }
            tmp.delete(0,1);
            sb.append(tmp);
            return sb.toString();
        }
        if (!coef.equals(BigInteger.valueOf(-1)) && !coef.equals(BigInteger.valueOf(1))) {
            sb.append(coef);
        } //如果不是是+-1的话就需要把coef放进去
        //index如果是0就直接不用输出幂函数的项
        if (index == 1) {
            sb.append("*x");//是1就不用输出指数部分
        } else if (index == 2) {
            sb.append("*x*x"); //2的话进行特殊处理
        } else if (index > 2) {
            sb.append("*x*").append(index); //大于2进行一般化的处理
        }
        if (sin != null) {
            for (TrigoFunc t : sin) {
                sb.append("*").append(t.toString());
            }
        }
        if (cos != null) {
            for (TrigoFunc t : cos) {
                sb.append("*").append(t.toString());
            }
        }
        return sb.toString();
    }*/




/*public HashMap<Integer,TrigoFunc> multiTrigoFunc(
            HashMap<Integer,TrigoFunc> a, HashMap<Integer,TrigoFunc> b) {
        for (TrigoFunc ta : a.values()) {
            int key = ta.getInnerIndex();
            if (key != 0) {
                ///如果ta内部项的指数不是0 （代表sin(x**m)的形式）
                if (b.containsKey(key)) {
                    ////如果此时b中存在对应的项
                    TrigoFunc tmp = b.get(key);
                    tmp.setOuterIndex(ta.getOuterIndex() + tmp.getOuterIndex());
                    b.put(key,tmp);
                } else {
                    b.put(ta.getInnerIndex(),ta);
                }
            } else {
                /////如果是sin(a)的形式
                BigInteger innerCoefOfta = ta.getInnerCoef();
                int outerIndexOfSame = -1;
                for (TrigoFunc tb : b.values()) {
                    if (tb.getInnerCoef().equals(innerCoefOfta)) {
                        outerIndexOfSame = tb.getOuterIndex();
                    }
                }
                if (outerIndexOfSame != -1) {
                    ta.setOuterIndex(outerIndexOfSame + ta.getOuterIndex());
                }
                b.put(ta.getInnerIndex(), ta);
            }
        }
        return b;
    }*/