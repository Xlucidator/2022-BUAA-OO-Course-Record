//import java.util.Scanner;
// 需要先将官方包中用到的工具类import进来

import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        
        // 获取自定义函数个数
        int cnt = scanner.getCount();
        
        // 读入自定义函数
        Function f = null;
        Function g = null;
        Function h = null;
        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine();
            // 存储或者解析逻辑
            ArrayList<Factor> formPars = new ArrayList<>();
            func = func.replaceAll("[\\s]*", "");
            func = func.replaceAll("sin", "s");
            func = func.replaceAll("cos", "c");
            Pattern p = Pattern.compile("([fgh])\\((.*)\\)=");
            Matcher m = p.matcher(func);
            String[] form;
            if (m.find()) {
                form = m.group(2).split(",");//形参字符串数组
                for (String s : form) {
                    Lexer lexer = new Lexer(s);
                    Parser parser = new Parser(lexer);
                    Factor formPar = parser.parseFactor(null, f, g, h);//
                    formPars.add(formPar);
                }
            }
            Lexer lexer = new Lexer(func.split("=")[1]);
            Parser parser = new Parser(lexer);
            Expr expr = parser.parseExpr(f, g, h);//函数表达式
            if (func.charAt(0) == 'f') {
                f = new Function(expr, formPars);
            } else if (func.charAt(0) == 'g') {
                g = new Function(expr, formPars);
            } else if (func.charAt(0) == 'h') {
                h = new Function(expr, formPars);
            }
        }
        
        // 读入最后一行表达式
        String input = scanner.readLine();
        input = input.replaceAll("[\\s]", "");
        input = input.replaceAll("sin", "s");//避免处理错i
        input = input.replaceAll("cos", "c");
        input = preprocess2(input);
        
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        
        Expr expr = parser.parseExpr(f, g, h);
        TriPoly p = expr.toTriPoly();
        System.out.println(p);
    }
    
    public static String preprocess2(String input) {
        String sb = input;
        Pattern p = Pattern.compile("sum\\(");
        Matcher m = p.matcher(input);
        while (m.find()) {
            int left = 0;
            int right = 0;
            int index;
            for (index = m.end(); index < input.length(); index++) {
                if (input.charAt(index) == '(') {
                    left++;
                } else if (input.charAt(index) == ')') {
                    right++;
                }
                if (right > left) {
                    break;
                }
            }
            String sub = input.substring(m.end(), index);
            String[] parameters = sub.split(",");
            Sum sum = new Sum(parameters[3], new BigInteger(parameters[1])
                    , new BigInteger(parameters[2]));
            sb = sb.replace(m.group(0) + sub + ')', '(' + sum.toString() + ')');
        }
        return sb;
    }
}