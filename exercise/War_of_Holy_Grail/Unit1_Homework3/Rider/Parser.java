import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;
    
    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    
    public Expr parseExpr(Function f, Function g, Function h) {
        Expr expr = new Expr();//表达式首字符
        if (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            expr.addOp(lexer.peek());
            lexer.next();
        } else {
            expr.addOp("+");//缺省默认为正
        }
        expr.addTerm(parseTerm(f, g, h));
        //连接符
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            expr.addOp(lexer.peek());
            lexer.next();
            expr.addTerm(parseTerm(f, g, h));
        }
        return expr;
    }
    
    public Term parseTerm(Function f, Function g, Function h) {
        Term term = new Term();//项首字符
        if (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            term.setOp(lexer.peek());
            lexer.next();
        } else {
            term.setOp("+");
        }
        term.addFactor(parseFactor(term, f, g, h));
        
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor(term, f, g, h));
        }
        return term;
    }
    
    public Factor parseFactor(Term term, Function f, Function g, Function h) {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr(f, g, h);//lexer.peek() == ')';
            lexer.next();//指向右括号后的字符
            if (!lexer.peek().isEmpty() && Character.isDigit(lexer.peek().charAt(0))) {
                int num = Integer.parseInt(lexer.peek());//指数
                lexer.next();//展开成多项
                if (num == 0) {
                    return new Number(BigInteger.ONE);//零次方等于1
                }
                for (int i = 0; i < num - 1; i++) {
                    term.addFactor(expr);
                }
            }
            return expr;
        } else if (lexer.peek().equals("x") ||
                lexer.peek().equals("y") || lexer.peek().equals("z")) {
            return isPower(lexer);
        } else if (lexer.peek().equals("s") || lexer.peek().equals("c")) {
            final int type = lexer.peek().equals("s") ? 1 : 0;
            lexer.next();//左括号
            lexer.next();
            Expr expr = parseExpr(f, g, h);//化简所需
            lexer.next();
            BigInteger num = BigInteger.ONE;//指数
            if (!lexer.peek().isEmpty() && Character.isDigit(lexer.peek().charAt(0))) {
                num = new BigInteger(lexer.peek());
                lexer.next();
            }
            return isTri(term, type, num, expr);
        } else if (lexer.peek().equals("f") || lexer.peek().equals("g") ||
                lexer.peek().equals("h")) {
            //获得实参
            return isFunc(lexer, f, g, h);
        } else if (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            String op = lexer.peek();
            lexer.next();//指向下一个数
            BigInteger num = new BigInteger(op + lexer.peek());//
            lexer.next();//下一个字符
            return new Number(num);
        } else {
            BigInteger num = new BigInteger(lexer.peek());//
            lexer.next();//下一个字符
            return new Number(num);
        }
    }
    
    public Factor isTri(Term term, int type, BigInteger index, Expr expr) {
        if (expr.isPre()) {
            return  new Tri(type, index, expr, "yz");
        }
        String factStr = expr.toTriPoly().toString();
        Expr expr1 = expr;
        if (index.equals(BigInteger.ZERO)) {
            return new Number(BigInteger.ONE);
        } else if (factStr.equals("0")) {
            if (type == 1) {
                //sin
                return new Number(BigInteger.ZERO);
            } else if (type == 0) {
                return new Number(BigInteger.ONE);
            }
        } else if (factStr.charAt(0) == '-') {
            //负数
            BigInteger mod = index.remainder(new BigInteger("2"));
            TriPoly triPoly = expr.toTriPoly().opposite();
            if (type == 1 && (!mod.equals(BigInteger.ZERO))) {
                if (term.getOp().equals("+")) {
                    term.setOp("-");
                } else if (term.getOp().equals("-")) {
                    term.setOp("+");
                }
            }
            factStr = triPoly.toString();
            String sb = factStr.replaceAll("sin", "s");
            sb = sb.replaceAll("cos", "c");
            Lexer subLexer = new Lexer(sb);
            Parser subParser = new Parser(subLexer);
            expr1 = subParser.parseExpr(null, null, null);
        }
        return new Tri(type, index, expr1, factStr);
    }
    
    public Factor isPower(Lexer lexer) {
        int type = (lexer.peek().equals("x")) ? 1
                : (lexer.peek().equals("y")) ? 2
                : (lexer.peek().equals("z")) ? 3 : 1;
        lexer.next();//指向x后的字符
        if (!lexer.peek().isEmpty() && Character.isDigit(lexer.peek().charAt(0))) {
            BigInteger num = new BigInteger(lexer.peek());//指数
            lexer.next();
            if (num.equals(BigInteger.ZERO)) {
                return new Number(BigInteger.ONE);
            }
            return new Power(type, num);
        } else {
            return new Power(type, BigInteger.ONE);//缺省为1
        }
    }
    
    public Factor isFunc(Lexer lexer, Function f, Function g, Function h) {
        ArrayList<Factor> parameters = new ArrayList<>();
        final String name = lexer.peek();
        lexer.next();
        Factor factor;
        Factor func = null;//之后一定指向全新factor
        do {
            lexer.next();
            factor = parseExpr(f, g, h);//可以支持实参是表达式
            parameters.add(factor);
        } while (!lexer.peek().equals(")"));
        lexer.next();//很关键
        switch (name) {
            case "f":
                func = f.toExpr(parameters);
                break;
            case "g":
                func = g.toExpr(parameters);
                break;
            case "h":
                func = h.toExpr(parameters);
                break;
            default:
                break;
        }
        return func;
    }
}

