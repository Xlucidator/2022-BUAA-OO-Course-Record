//import com.oocourse.spec2.ExprInput;
//import com.oocourse.spec2.ExprInputMode;
import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;
import deconstruct.Lexer;
import deconstruct.Parser;
import expression.Expr;
import expression.Function;

import java.util.ArrayList;
//import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        ExprInput in = new ExprInput(ExprInputMode.NormalMode);
        int n = in.getCount();
        
        //Scanner in = new Scanner(System.in);
        //int n = in.nextInt();
        //in.nextLine();
        ArrayList<Function> functions = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            //String inputfun = in.nextLine();
            String inputfun = in.readLine();
            inputfun = prePossessing(inputfun);
            functions.add(new Function(inputfun));
        }
        //String input = in.nextLine();
        String input = in.readLine();
        input = prePossessing(input);
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        parser.setFunctions(functions);
        Expr expr = parser.parseExpr();
        String output = expr.toString();
        //System.out.println(output);
        lexer = new Lexer(output);
        parser = new Parser(lexer);
        expr = parser.parseExpr();
        output = expr.toString();
        //output = output.replaceAll("sin\\(x\\*x\\)", "sin(x**2)");
        //output = output.replaceAll("cos\\(x\\*x\\)", "cos(x**2)");
        //System.out.println(output);
        Boolean again = expr.simplify();
        while (again) {
            //System.out.println(expr.toString());
            output = expr.toString();
            //System.out.println(output);
            again = expr.simplify();
        }
        output = output.replaceAll("sin\\(x\\*x\\)", "sin(x**2)");
        output = output.replaceAll("cos\\(x\\*x\\)", "cos(x**2)");
        System.out.println(output);
    }

    public static String prePossessing(String input) {
        String output = input.replaceAll("\\s", "");
        
        output = output.replaceAll("\\-\\+\\+|\\+\\-\\+|\\+\\+\\-", "-");
        output = output.replaceAll("\\+\\-\\-|\\-\\+\\-|\\-\\-\\+|\\+\\+\\+", "+");
        output = output.replaceAll("\\+\\+|\\-\\-", "+");
        output = output.replaceAll("\\+\\-|\\-\\+", "-");
        return output;
    }
}
