package func;

import java.util.ArrayList;

public class SelfFunc {
    private ArrayList<Func> funcs;

    public SelfFunc() {
        this.funcs = new ArrayList<>();
    }

    public void addFunc(Func func) {
        this.funcs.add(func);
    }

    public ArrayList<Func> getFuncs() {
        return funcs;
    }
}
