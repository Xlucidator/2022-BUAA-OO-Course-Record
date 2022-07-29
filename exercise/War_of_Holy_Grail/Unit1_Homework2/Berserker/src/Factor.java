import java.math.BigInteger;

public interface Factor {
    // include sin(), cos(), x
    public int getPos();

    public BigInteger getCoefficient();

    public BigInteger getPower();

    public int compareTo(Factor other);
}
