import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

public class Term implements Factor {
    private final HashSet<Factor> factors;
    private boolean negative;

    public Term() {
        this.factors = new HashSet<>();
        this.negative = false;
    }

    public void setNe() {
        this.negative = true;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public ArrayList<Variable> cal() {
        ArrayList<Variable> result = new ArrayList<>();
        Variable initial = new Variable().setVariable(new BigInteger("1"),new BigInteger("0"),
                new BigInteger("0"),"");
        result.add(initial);
        Variable temp = new Variable();
        for (Factor  item : this.factors) {
            result = temp.multMany(result,item.cal());
        }
        if (this.negative == true) {
            result = temp.steNeg(result);
        }
        return result;
    }

}
