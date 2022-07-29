package parser;

import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;
import expr.Constant;
import expr.Cos;
import expr.Expr;
import expr.Factor;
import expr.Function;
import expr.Sin;
import expr.Term;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class MainClass {
    public static final HashMap<String, Function> FUNCTIONS = new HashMap<>();
    
    public static Factor searchSin(HashMap<HashMap<Factor, BigInteger>, BigInteger> poly) {
        boolean flag = false;
        Factor ans = null;
        for (HashMap<Factor, BigInteger> key : poly.keySet()) {
            for (Factor factor : key.keySet()) {
                if (factor instanceof Sin) {
                    if (key.get(factor).equals(BigInteger.ONE.add(BigInteger.ONE))) {
                        flag = true;
                        ans = factor;
                        break;
                    }
                }
            }
            if (flag) {
                break;
            }
        }
        return ans;
    }
    
    public static Factor searchCos(HashMap<HashMap<Factor, BigInteger>, BigInteger> poly) {
        boolean flag = false;
        Factor ans = null;
        for (HashMap<Factor, BigInteger> key : poly.keySet()) {
            for (Factor factor : key.keySet()) {
                if (factor instanceof Cos) {
                    if (key.get(factor).equals(BigInteger.ONE.add(BigInteger.ONE))) {
                        flag = true;
                        ans = factor;
                        break;
                    }
                }
            }
            if (flag) {
                break;
            }
        }
        return ans;
    }
    
    public static HashMap<HashMap<Factor, BigInteger>, BigInteger> optimizeSin2(
            HashMap<HashMap<Factor, BigInteger>, BigInteger> poly
    ) {
        HashMap<HashMap<Factor, BigInteger>, BigInteger> tmp = new HashMap<>();
        Factor sin;
        if ((sin = searchSin(poly)) != null) {
            for (HashMap<Factor, BigInteger> key : poly.keySet()) {
                if (key.containsKey(sin)) {
                    HashMap<Factor, BigInteger> newKey = new HashMap<>();
                    for (Factor factor : key.keySet()) {
                        if (factor.equals(sin) && key.get(factor).equals(new BigInteger("2"))) {
                            Cos cos = ((Sin) sin).changeToCos();
                            Term term1 = new Term();
                            term1.addFactor(new Constant("1"));
                            Term term2 = new Term();
                            term2.addFactor(cos);
                            term2.addFactor(cos);
                            term2.changePos();
                            Expr expr = new Expr();
                            expr.addTerm(term1);
                            expr.addTerm(term2);
                            newKey.put(expr, BigInteger.ONE);
                        } else {
                            newKey.put(factor, key.get(factor));
                        }
                    }
                    tmp.put(newKey, poly.get(key));
                } else {
                    tmp.put(key, poly.get(key));
                }
            }
            Expr expr = new Expr();
            expr.setPoly(tmp);
            Lexer lexer = new Lexer(expr.display());
            Parser parser = new Parser(lexer);
            Expr ans = parser.consumeExpr();
            ans.update();
            return ans.getPoly();
        } else {
            return null;
        }
    }
    
    public static HashMap<HashMap<Factor, BigInteger>, BigInteger> optimizeCos2(
            HashMap<HashMap<Factor, BigInteger>, BigInteger> poly
    ) {
        HashMap<HashMap<Factor, BigInteger>, BigInteger> tmp = new HashMap<>();
        Factor cos;
        if ((cos = searchCos(poly)) != null) {
            for (HashMap<Factor, BigInteger> key : poly.keySet()) {
                if (key.containsKey(cos)) {
                    HashMap<Factor, BigInteger> newKey = new HashMap<>();
                    for (Factor factor : key.keySet()) {
                        if (factor.equals(cos) && key.get(factor).equals(new BigInteger("2"))) {
                            Sin sin = ((Cos) cos).changeToSin();
                            Term term1 = new Term();
                            term1.addFactor(new Constant("1"));
                            Term term2 = new Term();
                            term2.addFactor(sin);
                            term2.addFactor(sin);
                            term2.changePos();
                            Expr expr = new Expr();
                            expr.addTerm(term1);
                            expr.addTerm(term2);
                            newKey.put(expr, BigInteger.ONE);
                        } else {
                            newKey.put(factor, key.get(factor));
                        }
                    }
                    tmp.put(newKey, poly.get(key));
                } else {
                    tmp.put(key, poly.get(key));
                }
            }
            Expr expr = new Expr();
            expr.setPoly(tmp);
            Lexer lexer = new Lexer(expr.display());
            Parser parser = new Parser(lexer);
            Expr ans = parser.consumeExpr();
            ans.update();
            return ans.getPoly();
        } else {
            return null;
        }
    }
    
    public static ArrayList<String> optimizeSin(
            HashMap<HashMap<Factor, BigInteger>, BigInteger> poly
    ) {
        ArrayList<String> strings = new ArrayList<>();
        HashMap<HashMap<Factor, BigInteger>, BigInteger> tmp;
        HashMap<HashMap<Factor, BigInteger>, BigInteger> resource = new HashMap<>(poly);
        while ((tmp = optimizeSin2(resource)) != null) {
            resource.clear();
            resource.putAll(tmp);
            Expr expr = new Expr();
            expr.setPoly(tmp);
            strings.add(expr.display());
        }
        return strings;
    }
    
    public static ArrayList<String> optimizeCos(
            HashMap<HashMap<Factor, BigInteger>, BigInteger> poly
    ) {
        ArrayList<String> strings = new ArrayList<>();
        HashMap<HashMap<Factor, BigInteger>, BigInteger> tmp;
        HashMap<HashMap<Factor, BigInteger>, BigInteger> resource = new HashMap<>(poly);
        while ((tmp = optimizeCos2(resource)) != null) {
            resource.clear();
            resource.putAll(tmp);
            Expr expr = new Expr();
            expr.setPoly(tmp);
            strings.add(expr.display());
        }
        return strings;
    }
    
    public static void main(String[] args) {
        //读入
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        //读入n
        int numOfCustomizeFunction = scanner.getCount();
        //读入自定义函数
        for (int i = 0; i < numOfCustomizeFunction; i++) {
            String function = scanner.readLine();
            Lexer l = new Lexer(function);
            Parser p = new Parser(l);
            Function ref = p.consumeCustomizeFunction();
            FUNCTIONS.put(ref.getName(), ref);
        }
        String input = scanner.readLine();
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        //读入一个表达式
        Expr expr = parser.consumeExpr();
        //更新计算HashMap
        expr.update();
        //输出
        ArrayList<String> strings = new ArrayList<>();
        strings.add(expr.display());
        strings.addAll(optimizeSin(expr.getPoly()));
        strings.addAll(optimizeCos(expr.getPoly()));
        String minString = strings.get(0);
        for (String string : strings) {
            //System.out.println(string);
            if (string.length() < minString.length()) {
                minString = string;
            }
        }
        System.out.println(minString);
    }
}