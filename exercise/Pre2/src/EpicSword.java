public class EpicSword extends Sword {
    private final double evolveRatio;

    public EpicSword() {
        evolveRatio = 0.0;
    }

    public EpicSword(int id, String name, long price, double sharpness, double evolveRatio) {
        super(id, name, price, sharpness);
        this.evolveRatio = evolveRatio;
    }

    public double getEvolveRatio() {
        return evolveRatio;
    }

    public String toString() {
        return super.toString() + String.format(", evolveRatio is %f", evolveRatio);
    }

    @Override
    public void valueUsedBy(Adventurer user) {
        super.valueUsedBy(user);
        setSharpness(getSharpness() * getEvolveRatio());
        System.out.println(getName() + "'s sharpness became " + getSharpness() + ".");
    }
}
