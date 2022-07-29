package factor;

import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;

    public Number(BigInteger number) {
        this.num = number;
    }

    public Number(BigInteger number, BigInteger degree) {
        BigInteger zero = new BigInteger("0");
        if (degree.equals(zero)) {
            num = new BigInteger("1");
        } else {
            BigInteger res = new BigInteger(number.toString());
            BigInteger one = new BigInteger("1");
            BigInteger times = degree.subtract(one);
            while (times.compareTo(zero) > 0) {
                times = times.subtract(one);
                res = res.multiply(number);
            }
            this.num = res;
        }
    }

    public BigInteger getNum() {
        return num;
    }
}
