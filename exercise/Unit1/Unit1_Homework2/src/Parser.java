import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expression parseExpression() {
        Expression expression = new Expression();
        boolean exprSigned;
        exprSigned = !lexer.peek().equals("-");
        if (lexer.peek().matches("[+-]")) {
            lexer.next();
        }
        expression.addTerm(parseTerm(exprSigned));

        while (lexer.peek().matches("[+-]")) {
            exprSigned = !lexer.peek().equals("-");
            lexer.next();
            expression.addTerm(parseTerm(exprSigned));
        }
        expression.clearExpressionInTerm();
        expression.uniteLikeTerm();
        return expression;
    }

    public Term parseTerm(boolean exprSigned) {
        boolean termSigned;
        termSigned = !lexer.peek().equals("-") == exprSigned;   // xnor
        if (lexer.peek().matches("[+-]")) {
            lexer.next();
        }

        BigInteger coefficient = new BigInteger(termSigned ? "1" : "-1");
        Term term = new Term(coefficient, 0);

        boolean isFirstTime = true;

        do {
            if (!isFirstTime) {
                lexer.next();   // do while helpless in
            }

            if (lexer.peek().substring(0, 1).matches("[0-9+x-]")) {
                term.addPolyFactor(parsePolyFactor());
            } else if (lexer.peek().equals("(")) {
                lexer.next();
                Expression expr = parseExpression(); // ")"
                int times = 1;
                lexer.next();
                if (lexer.peek().equals("**")) {
                    lexer.next();
                    times = parseNum().intValue();
                }
                for (int i = 0; i < times; i++) {
                    term.addExpression(expr.deepClone());
                }
            } else if (lexer.peek().matches("cos|sin")) {
                term.addTrig(parseTrig());
            } else if (lexer.peek().equals("sum")) {
                term.addExpression(parseSum());
            } else if (lexer.peek().matches("[fgh]")) {
                term.addExpression(parseFunction());
            }

            isFirstTime = false;
        } while (lexer.peek().equals("*"));

        return term;
    }

    public BigInteger parseNum() {
        String sign = "";
        if (lexer.peek().matches("[+-]")) {
            sign = lexer.peek();
            lexer.next();
        }
        BigInteger num = new BigInteger(sign + lexer.peek());
        lexer.next();
        return num;
    }

    public PolyFactor parsePolyFactor() {
        BigInteger coefficient;
        int index;
        if (lexer.peek().equals("x")) {
            coefficient = BigInteger.ONE;
            index = 1;
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                index = parseNum().intValue();
            }
        } else {
            coefficient = parseNum();
            index = 0;
        }
        return new PolyFactor(coefficient, index);
    }

    public Expression parseFunction() {
        String name = lexer.peek();
        SelfFunction func = MainClass.MYFUNCTIONS.get(name);

        lexer.next();   // "("
        lexer.next();

        String funcExpr = func.getExpr();
        String[] arguments = getContentInBrackets().split(",");
        ArrayList<String> parameters = func.getParameters();
        HashMap<String, String> pairs = new HashMap<>();
        for (int i = 0; i < arguments.length; i++) {
            pairs.put(parameters.get(i), arguments[i]);
        }

        if (pairs.containsKey("x")) {
            funcExpr = funcExpr.replaceAll("x",pairs.get("x"));
        }
        if (pairs.containsKey("y")) {
            funcExpr = funcExpr.replaceAll("y",pairs.get("y"));
        }
        if (pairs.containsKey("z")) {
            funcExpr = funcExpr.replaceAll("z",pairs.get("z"));
        }

        return new Parser(new Lexer(funcExpr)).parseExpression();
    }

    public Expression parseSum() {
        lexer.next();   // "("
        lexer.next();   // "i"
        lexer.next();   // ","
        lexer.next();
        BigInteger start = parseNum();
        lexer.next();
        BigInteger end = parseNum();
        lexer.next();
        String sumTerm = getContentInBrackets().replaceAll("sin", "#");
        StringBuilder sb = new StringBuilder();
        while (start.compareTo(end) <= 0) {
            sb.append("+").append(sumTerm.replaceAll("i", start.toString()));
            start = start.add(BigInteger.ONE);
        }
        if (sb.length() == 0) {
            sb.append("0");
        }
        String expr = sb.toString().replaceAll("#", "sin");

        return new Parser(new Lexer(expr)).parseExpression();
    }

    public String getContentInBrackets() {
        int bracketPairs = 0;
        StringBuilder expr = new StringBuilder();
        while (true) {
            if (lexer.peek().equals("(")) {
                bracketPairs++;
            }
            if (lexer.peek().equals(")")) {
                if (bracketPairs == 0) {
                    break;
                } else {
                    bracketPairs--;
                }
            }
            expr.append(lexer.peek());
            lexer.next();
        }   // ")"
        lexer.next();
        return expr.toString();
    }

    public TrigFunction parseTrig() {
        final String name = lexer.peek();
        lexer.next();   // lexer.peek() == '('
        lexer.next();
        PolyFactor varFactor = parsePolyFactor();   // lexer.peek() == ')'
        lexer.next();
        int index = 1;
        if (lexer.peek().equals("**")) {
            lexer.next();
            index = parseNum().intValue();
        }
        return new TrigFunction(name, varFactor, index);
    }

}
