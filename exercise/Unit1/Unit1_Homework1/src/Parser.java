import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expression parseExpression() {
        Expression expression = new Expression();
        boolean signed;
        signed = !lexer.peek().equals("-");
        if (lexer.peek().matches("[+-]")) {
            lexer.next();
        }
        expression.addTerm(parseTerm(signed));
        while (lexer.peek().matches("[+-]")) {
            signed = !lexer.peek().equals("-");
            lexer.next();
            expression.addTerm(parseTerm(signed));
        }
        expression.uniteLikeTerm();
        return expression;
    }

    public Term parseTerm(boolean signed) {
        Term term = new Term();
        int degree = 0;
        BigInteger coefficient = new BigInteger(signed ? "1" : "-1");
        boolean isFirstTime = true;

        do {
            if (!isFirstTime) {
                lexer.next();   // do while helpless in
            }

            if (lexer.peek().equals("x")) {
                degree++;
            } else if (lexer.peek().matches("\\d+")) {
                coefficient = coefficient.multiply(new BigInteger(lexer.peek()));
            } else if (lexer.peek().matches("[+-]")) {
                String sign = lexer.peek();
                lexer.next();
                BigInteger value = new BigInteger(sign + lexer.peek());
                coefficient = coefficient.multiply(value);
            } else if (lexer.peek().equals("(")) {
                lexer.next();
                term.addExpression(parseExpression());
            }

            lexer.next();
            isFirstTime = false;
        } while (lexer.peek().equals("*"));

        term.setCoefficient(coefficient);
        term.setDegree(degree);
        return term;
    }

}
