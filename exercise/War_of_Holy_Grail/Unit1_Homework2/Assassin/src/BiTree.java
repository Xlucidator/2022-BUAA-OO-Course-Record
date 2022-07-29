public class BiTree {
    private String algorithm; //该节点处的算法
    private String name; //该节点的名称
    private Poly poly; //该节点对应的值（多项式）
    private BiTree father; //该节点的父节点
    private BiTree lchild; //左子节点
    private BiTree rchild; //右子节点

    public BiTree() {
        algorithm = "";
        name = "";
        poly = new Poly();
        father = null;
        lchild = null;
        rchild = null;
    }

    public BiTree getFather() {
        return father;
    }

    public BiTree getLchild() {
        return lchild;
    }

    public BiTree getRchild() {
        return rchild;
    }

    public Poly getPoly() {
        return poly;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getName() {
        return name;
    }

    public void setPoly(Poly poly) {
        this.poly = poly;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setFather(BiTree father) {
        this.father = father;
    }

    public void setLchild(BiTree lchild) {
        this.lchild = lchild;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRchild(BiTree rchild) {
        this.rchild = rchild;
    }

}
