import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是预解析读入模式，所以我们实例化时传递的参数为ExprInputMode.ParsedMode
        ExprInput scanner = new ExprInput(ExprInputMode.ParsedMode);

        // 预解析读入模式下，使用getCount()读入接下来还要读取多少行解析后的表达式
        int n = scanner.getCount();
        List<String> exprs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String curExpr = scanner.readLine();
            exprs.add(curExpr);
        }
        BiTree root = buildTree(exprs);
        System.out.println(root.getPoly().toString());
    }

    public static BiTree buildTree(List<String> exprs) {
        HashMap<String,BiTree> nodes = new HashMap<>();
        for (String str : exprs) {
            String[] strArray = str.split(" ");
            String name = strArray[0];
            String op = strArray[1];
            BiTree node = new BiTree();
            node.setName(name);
            node.setAlgorithm(op);
            if (strArray.length == 2) {
                String digit = strArray[1];
                BiTree leaf = newLeaf(digit);
                node.setAlgorithm("null");
                leaf.setFather(node);
                node.setLchild(leaf);
            } else {
                String left = strArray[2];
                if (left.equals("x") || Character.isDigit(left.charAt(0)) || left.charAt(0) == '+' || left.charAt(0) == '-') {
                    BiTree leaf = newLeaf(left);
                    leaf.setFather(node);
                    node.setLchild(leaf);
                } else {
                    if (nodes.containsKey(left)) {
                        BiTree newLchild = nodes.get(left); //取出名字相同的子节点
                        newLchild.setFather(node); //将子节点的父节点设置为node
                        node.setLchild(newLchild); //将node的左子节点设置成newLchild
                        nodes.put(left,newLchild); //再将更新的结点放回HashMap中
                    }
                }
                if (strArray.length == 3) { //如果没有右子结点
                    node.setRchild(null); //将右子节点设置为null
                } else {
                    String right = strArray[3];
                    if (right.equals("x") || Character.isDigit(right.charAt(0)) || right.charAt(0) == '+' || right.charAt(0) == '-') {
                        BiTree leaf = newLeaf(right);
                        leaf.setFather(node);
                        node.setRchild(leaf);
                    } else {
                        if (nodes.containsKey(right)) {
                            BiTree newRchild = nodes.get(right); //取出名字相同的子节点
                            newRchild.setFather(node); //将子节点的父节点设置为node
                            node.setRchild(newRchild); //将node的左子节点设置成newLchild
                            nodes.put(right,newRchild); //再将更新的结点放回HashMap中
                        }
                    }
                }
            }
            node.setPoly(calculate(node));
            nodes.put(name,node); //将node和对应的名字放入HashMap中
        }
        BiTree root = new BiTree();
        for (BiTree tmp : nodes.values()) {
            root = tmp;
            break; }
        while (true) {
            assert root != null;
            if (root.getFather() == null) { break; }
            root = root.getFather(); }
        return root;
    }

    public static Poly calculate(BiTree node) {  //找到树的左子树和右子树的值并进行相关的运算
        String op = node.getAlgorithm();
        Poly newPoly = new Poly();
        Poly left = node.getLchild().getPoly();
        Poly right = node.getRchild() == null ? new Poly() : node.getRchild().getPoly();

        if (op.equals("add")) { newPoly = left.addPoly(right); }
        else if (op.equals("pos")) { newPoly = left; }
        else if (op.equals("sub")) { newPoly = left.addPoly(right.getOpposite()); }
        else if (op.equals("neg")) { newPoly = left.getOpposite(); }
        else if (op.equals("mul")) { newPoly = left.multiPoly(right); }
        else if (op.equals("pow")) {
            int index = 0;
            assert right != null;
            for (Mono tmp : right.getMonos()) {
                index = Integer.parseInt(tmp.getCoef().toString());
                break;
            }
            newPoly = left.powPoly(index);
        }
        else if (op.equals("sin")) {
            BigInteger innerCoef = new BigInteger("0");
            int innerIndex = 0;
            for (Mono tmp : left.getMonos()) {
                innerCoef = tmp.getCoef();
                innerIndex = tmp.getIndex();
            }
            TrigoFunc newSin = new TrigoFunc("sin",innerCoef,innerIndex,1);
            ArrayList<TrigoFunc> t = new ArrayList<>();
            t.add(newSin);
            Mono newMono = new Mono(BigInteger.valueOf(1),0,t,new ArrayList<>());
            newPoly = newMono.toPoly();
        }
        else if (op.equals("cos")) {
            BigInteger innerCoef = new BigInteger("0");
            int innerIndex = 0;
            for (Mono tmp : left.getMonos()) {
                innerCoef = tmp.getCoef();
                innerIndex = tmp.getIndex();
            }
            TrigoFunc newCos = new TrigoFunc("cos",innerCoef,innerIndex,1);
            ArrayList<TrigoFunc> t = new ArrayList<>();
            t.add(newCos);
            Mono newMono = new Mono(BigInteger.valueOf(1),0,new ArrayList<>(),t);
            newPoly = newMono.toPoly();
        }
        else if (op.equals("null")) { newPoly = left; }
        return newPoly;
    }

    public static BiTree newLeaf(String content) { ////建立叶子结点的函数
        BiTree leaf = new BiTree();
        leaf.setLchild(null);
        leaf.setRchild(null);
        leaf.setFather(null);
        if (content.equals("x")) {
            leaf.setName("leaf"); //表明是叶子结点
            leaf.setAlgorithm("");
            leaf.setName("x");
            Mono tmp = new Mono(BigInteger.valueOf(1),1,new ArrayList<>(),new ArrayList<>());
            Poly res = tmp.toPoly();
            leaf.setPoly(res);
        } else {
            leaf.setName("leaf");
            leaf.setAlgorithm("");
            leaf.setName(content);
            Mono tmp = new Mono(new BigInteger(content),0,new ArrayList<>(),new ArrayList<>());
            Poly res = tmp.toPoly();
            leaf.setPoly(res);
        }
        return leaf;
    }
}
