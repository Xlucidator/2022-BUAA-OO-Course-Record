package expression;

import polynomial.Polynomial;

public interface Factor {
    Polynomial factorToPoly();

    void setAllPow(int allPow);

    int getAllPow();

    Factor copy();
}
