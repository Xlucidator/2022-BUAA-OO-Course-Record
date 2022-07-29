public class Parser {
    private Lexer lexer;

    public Parser(Lexer e) {
        this.lexer = e;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm());
        while (lexer.getchar().equals("+") || lexer.getchar().equals("-")) {
            expr.addTerm(parseTerm());
        }
        lexer.goNext();
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        while ("-+".indexOf(lexer.getchar()) != -1) {
            if (lexer.getchar().equals("-")) {
                term.reverse();
            }
            lexer.goNext();
        }
        term.addFactor(parseFactor());
        while (lexer.getchar().equals("*")) {
            lexer.goNext();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        Factor factor;
        if (lexer.getchar().equals("x")) {
            X x = new X();
            lexer.goNext();
            if (lexer.nextIsExp()) {
                lexer.goNext();
                lexer.goNext();
                x.setIndex(lexer.nextNum());
            }
            factor = x;
        } else if (lexer.getchar().equals("(")) {
            lexer.goNext();
            Expr tmpExpr = parseExpr();
            if (lexer.nextIsExp()) {
                lexer.goNext();
                lexer.goNext();
                tmpExpr.setIndex(lexer.nextNum());
            }
            factor = tmpExpr;
        }
        else {
            factor = new Number(lexer.nextNum());
        } //factor.anounce();
        return factor;
    }
}
