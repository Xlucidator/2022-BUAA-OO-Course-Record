import java.util.ArrayList;

public class Custom {

    private String name;
    private ArrayList<String> param;
    private Expr def;
    private int len;

    public ArrayList<String> getParam() {
        return param;
    }

    public int getLen() {
        return len;
    }

    public String getName() {
        return name;
    }

    public Expr getDef() {
        return def;
    }

    public Custom(String name, ArrayList<String> param, Expr def) {
        this.name = name;
        this.param = param;
        this.def = def;
        this.len = param.size();
    }

    public String toString() {
        return def.toString();
    }
}
