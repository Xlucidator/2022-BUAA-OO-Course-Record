package expr;

import java.util.ArrayList;

public class Func extends Expr {
    private ArrayList<Param> params;

    public Func() {
        super();
        params = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            params.add(new Param());
        }
    }

    public Param getParam(int i) {
        return params.get(i);
    }

    public void setParam(int i, Factor factor) {
        params.get(i).setFactor(factor);
    }

}
