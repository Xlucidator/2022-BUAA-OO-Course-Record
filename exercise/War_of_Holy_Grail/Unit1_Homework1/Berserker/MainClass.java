import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;
import polynomial.Polynomial;

//import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        //单次
        String text = scanner.readLine();
        Lexer lexer = new Lexer(text);
        Parser parser = new Parser(lexer);
        Polynomial poly = parser.parsePoly();
        System.out.println(poly);

        //                //测试
        //                Scanner scanner = new Scanner(System.in);
        //                while (scanner.hasNextLine()) {
        //                    String text = scanner.nextLine();
        //                    Lexer lexer = new Lexer(text);
        //                    Parser parser = new Parser(lexer);
        //                    Polynomial poly = parser.parsePoly();
        //                    System.out.println(poly);
        //                }
    }
}
