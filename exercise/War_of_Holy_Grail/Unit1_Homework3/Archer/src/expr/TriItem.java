package expr;

import java.math.BigInteger;

public class TriItem {
    private BigInteger power;
    private String type;
    private Poly content;

    public boolean equal(TriItem other) {
        return (this.getPower().compareTo(other.getPower()) == 0 &&
                this.getType().equals(other.getType()) &&
                this.getContent().equal(other.getContent()));
    }

    public TriItem() {
        this.power = BigInteger.ZERO;
        this.type = "none";
        this.content = new Poly();
    }

    public TriItem(BigInteger power, String type, Poly content) {
        this.power = power;
        this.type = type;
        this.content = new Poly();
        this.content.getAllItems().addAll(content.getAllItems());
    }

    public void setPower(BigInteger power) { this.power = power; }

    public BigInteger getPower() { return power; }

    public void setType(String type) { this.type = type; }

    public String getType() {
        if (power.compareTo(BigInteger.ZERO) == 0) {
            return "none";
        } else {
            return type;
        }
    }

    public void setContent(Poly content) {
        this.content = new Poly();
        this.content.getAllItems().addAll(content.getAllItems());
    }

    public Poly getContent() {
        if (getType().equals("none")) {
            System.out.println("Error!No SinContent!");
            return null;
        } else {
            return content;
        }
    }

}
