package main;

import factor.function.Custom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class FunctionBase {
    private final ArrayList<Custom> functions = new ArrayList<>();

    public Custom getFunction(String name) throws IOException, ClassNotFoundException {
        for (Custom custom : functions) {
            if (Objects.equals(custom.getName(), name)) {
                return custom;
            }
        }
        return new Custom("");
    }

    public void addFunction(Custom custom) {
        functions.add(custom);
    }
}
