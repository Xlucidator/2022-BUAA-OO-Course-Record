package expression;

public class Variable implements Base {
    private final String name = "x";

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }
}
