import java.math.BigInteger;

public class MiniList {
    public BigInteger getVariables(int i) {
        return variables[i];
    }

    public MiniList() {
        this.variables[0] = new BigInteger("0");
    }

    public void setVariables(int i, BigInteger bigInteger) {
        this.variables[i] = bigInteger;
    }

    private BigInteger[] variables = new BigInteger[20];

    public void put(int power, BigInteger coef) {
        this.variables[power] = coef;
    }

    public void print() {
        BigInteger zero = new BigInteger("0");
        BigInteger one = new BigInteger("1");
        BigInteger negone = new BigInteger("-1");
        int z = 1;
        for (int i = 1; i < 20; i++) {
            if (!this.variables[0].toString().equals("0")) {
                break;
            }
            if (this.variables[i] != null && !this.variables[i].toString().equals("0")) {
                z = 0;
                break;
            }
        }
        int index = 1;
        for (int i = 0; i < 20; i++) {
            if (this.variables[i] != null) {
                if (index == 0 && this.variables[i].compareTo(zero) > 0) {
                    System.out.print("+");
                }
                if (this.variables[i] != null) {
                    if (i == 0 && z != 0) {
                        System.out.print(this.variables[i]);
                        index = 0;
                    } else if (this.variables[i].compareTo(negone) == 0) {
                        System.out.print("-x");
                        if (i == 2) {
                            System.out.print("*x");
                        } else if (i != 1) {
                            System.out.print("**" + i);
                        }
                        index = 0;
                    } else if (this.variables[i].compareTo(one) == 0) {
                        if (i == 2) {
                            System.out.print("x*x");
                        } else {
                            System.out.print("x");
                            if (i > 1) {
                                System.out.print("**" + i);
                            }
                        }
                        index = 0;
                    }
                    else if (this.variables[i].compareTo(zero) != 0) {
                        if (i == 2) {
                            System.out.print(this.variables[i] + "*x*x");
                        } else {
                            System.out.print(this.variables[i] + "*x");
                            if (i > 1) {
                                System.out.print("**" + i);
                            }
                        }
                        index = 0;
                    }
                }
            }
        }
    }
}
