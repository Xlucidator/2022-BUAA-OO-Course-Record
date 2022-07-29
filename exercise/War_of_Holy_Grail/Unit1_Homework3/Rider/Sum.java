import java.math.BigInteger;

public class Sum {
    private final String expr;
    private final BigInteger start;
    private final BigInteger end;
    
    public Sum(String expr, BigInteger start, BigInteger end) {
        this.expr = expr;
        this.start = start;
        this.end = end;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BigInteger i = start; i.compareTo(end) <= 0; ) {
            sb.append(expr.replaceAll("i", '(' + i.toString() + ')'));
            if (i.compareTo(end) < 0) {
                sb.append("+");
            }
            i = i.add(BigInteger.ONE);
        }
        if (sb.toString().isEmpty()) {
            sb.append("0");
        }
        return sb.toString();
    }
}
