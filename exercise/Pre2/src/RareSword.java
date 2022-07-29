public class RareSword extends Sword {
    private final double extraExpBonus;

    public RareSword() {
        extraExpBonus = 0.0;
    }

    public RareSword(int id, String name, long price, double sharpness, double extraExpBonus) {
        super(id, name, price, sharpness);
        this.extraExpBonus = extraExpBonus;
    }

    public double getExtraExpBonus() {
        return extraExpBonus;
    }

    public String toString() {
        return super.toString() + String.format(", extraExpBonus is %f", extraExpBonus);
    }

    @Override
    public void valueUsedBy(Adventurer user) {
        super.valueUsedBy(user);
        user.setExp(user.getExp() + getExtraExpBonus());
        System.out.println(user.getName() + " got extra exp " + getExtraExpBonus() + ".");
    }
}

