public class HealingPotion extends Bottle {
    private final double efficiency;

    public HealingPotion() {
        efficiency = 0.0;
    }

    public HealingPotion(int id, String name, long price, double capacity, double efficiency) {
        super(id, name, price, capacity);
        this.efficiency = efficiency;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public String toString() {
        return super.toString() + String.format(", efficiency is %f", efficiency);
    }

    @Override
    public void valueUsedBy(Adventurer user) throws Exception {
        super.valueUsedBy(user);
        double extraRecover = getEfficiency() * getCapacity();
        user.setHealth(user.getHealth() + extraRecover);
        System.out.println(user.getName() + " recovered extra " + extraRecover + ".");
    }
}