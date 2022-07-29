public class Bottle extends Equipment {
    private final double capacity;
    private boolean filled;

    public Bottle() {
        capacity = 0.0;
        filled = true;
    }

    public Bottle(int id, String name, long price, double capacity) {
        super(id, name, price);
        this.capacity = capacity;
        this.filled = true;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public double getCapacity() {
        return capacity;
    }

    public boolean isFilled() {
        return filled;
    }

    public String toString() {
        return super.toString() + String.format(
                ", capacity is %f" +
                ", filled is %b", capacity, filled);
    }

    @Override
    public void valueUsedBy(Adventurer user) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }

        user.setHealth(user.getHealth() + capacity / 10);
        setFilled(false);
        setPrice(getPrice() / 10);

        System.out.println(user.getName() +
                " drank " + getName() +
                " and recovered " + capacity / 10 +
                ".");
    }
}




