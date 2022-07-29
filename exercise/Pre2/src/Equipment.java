import java.math.BigInteger;

public class Equipment implements Comparable<ValueBody>, ValueBody {
    private final int id;
    private final String name;
    private long price;

    public Equipment() {
        id = 0;
        name = "";
        price = 0;
    }

    public Equipment(int id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public BigInteger getValue() {
        return BigInteger.valueOf(getPrice());
    }

    @Override
    public String toString() {
        String cur = this.getClass().getSimpleName();
        String modifiedClassName = cur.substring(0, 1).toLowerCase() + cur.substring(1);
        return String.format("The %s's" +
                " id is %d" +
                ", name is %s", modifiedClassName, id, name);
    }

    @Override
    public int compareTo(ValueBody other) {
        int cmp = this.getValue().compareTo(other.getValue());
        if (cmp < 0) {
            return -1;
        } else if (cmp > 0) {
            return 1;
        }
        if (this.getId() != other.getId()) {
            return this.getId() - other.getId();
        }
        return 0;
    }

    public void valueUsedBy(Adventurer user) throws Exception {
        System.out.printf(name, "has been used%n");
    }
}