import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;
import expr.Expr;
import expr.Term;

import java.util.HashSet;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String input = scanner.readLine();

        int sign = 0;
        Lexer pre = new Lexer(input);
        int opFirst = pre.getFlag();
        pre.next();
        int opSecond = pre.getFlag();
        String ctSecond = pre.getCurToken();
        if ((opFirst == 11 || opFirst == 10)) {
            if (opFirst == 11) {
                sign = 1;
            } else {
                sign = -1;
            }
        }
        //System.out.println("sign: " + sign);

        Lexer lexer = new Lexer(input);
        if (sign == 1 || sign == -1) {
            lexer.setFlag(3);
            lexer.next();
        }
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr(sign);
        //System.out.println("AfterParser: " + expr.toString());
        HashSet<Expr> set = new HashSet<Expr>();
        for (Term term : expr.getTerms()) {
            //System.out.println("Term: " + term.toString() + "   Sign: " + term.getSign());
            Expr s = term.upTerm();
            //System.out.println("After_upTerm: " + s.toString() + "  Sign: " + s.getSign());
            s.upExpr();
            //System.out.println("After_upExpr: " + s.toString() + "  Sign: " + s.getSign());
            set.add(s);
        }
        Expr ans = new Expr();
        ans.upExpr();
        for (Expr e : set) {
            ans.addExpr(e);
        }
        System.out.println(ans.makeAns());
    }
}
