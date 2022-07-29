import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class MainFun {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String strOrigin = scanner.readLine();
        String strReBlank = new RemoveBlank(strOrigin).getStr();
        String strRePlus = new RemovePlusSymbol(new RemovePlusSymbol(strReBlank).getStr()).getStr();
        String strMid = new String();
        if (strRePlus.startsWith("+") || strRePlus.startsWith("-")) {
            strMid = strRePlus;
        } else {
            strMid = "+" + strRePlus;
        }
        Expr expr = new Expr(strMid);
        String strFin = expr.calculate();
        String strFinRe = new RemovePlusSymbol(new RemovePlusSymbol(strFin).getStr()).getStr();
        String res = Algebra.merge(strFinRe);
        System.out.println(new RemovePlusSymbol(res).getStr());
    }
}
