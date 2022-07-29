public class ExpBottle extends Bottle {
    private final double expRatio;

    public ExpBottle() {
        expRatio = 0.0;
    }

    public ExpBottle(int id, String name, long price, double capacity, double expRatio) {
        super(id, name, price, capacity);
        this.expRatio = expRatio;
    }

    public double getExpRatio() {
        return expRatio;
    }

    public String toString() {
        return super.toString() + String.format(", expRatio is %f", expRatio);
    }

    @Override
    public void valueUsedBy(Adventurer user) throws Exception {
        super.valueUsedBy(user);
        user.setExp(user.getExp() * getExpRatio());
        System.out.println(user.getName() + "'s exp became " + user.getExp() + ".");
    }
}