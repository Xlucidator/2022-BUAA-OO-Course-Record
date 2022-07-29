import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private List<String> exprs;
    private int num;

    public Parser(int num,List<String> exprs) {
        this.exprs = exprs;
        this.num = num;
    }

    public ArrayList<Factor> parserStr() {
        HashMap<String, ArrayList<Factor>> functions = new HashMap<>();
        for (int i = 0;i < num;i++) {
            String[] list = exprs.get(i).split(" ");
            if (list.length == 7) {
                return translate(functions,list[6]);
            }
            String label = list[0];
            String oper = null;
            if (list[1].length() == 3) {
                oper = list[1];
            }
            String oper1 = null;
            String oper2 = null;
            if (list.length >= 3) {
                oper1 = list[2];
            }
            if (list.length >= 4) {
                oper2 = list[3];
            }
            //label oper oper1 oper2
            ArrayList<Factor> operation1 = new ArrayList<>();
            ArrayList<Factor> operation2 = new ArrayList<>();
            if (oper1 != null) {
                operation1 = translate(functions, oper1);
            }
            if (oper2 != null) {
                operation2 = translate(functions, oper2);
            }
            ArrayList<Factor> tempFun = new ArrayList<>();
            Factor tempFactor;
            Calculate cal = new Calculate();
            if (oper.equals("sin")) {
                tempFactor = new Factor(true,operation1.get(0));
                tempFun.add(tempFactor);
            } else if (oper.equals("cos")) {
                tempFactor = new Factor(false,operation1.get(0));
                tempFun.add(tempFactor);
            } else if (oper.equals("pos")) {
                tempFun = cal.posFun(operation1);
            } else if (oper.equals("neg")) {
                tempFun = cal.negFun(operation1);
            } else if (oper.equals("add")) {
                tempFun = cal.addFun(operation1,operation2);
            } else if (oper.equals("sub")) {
                tempFun = cal.subFun(operation1,operation2);
            } else if (oper.equals("mul")) {
                tempFun = cal.mulFun(operation1,operation2);
            } else if (oper.equals("pow")) {
                tempFun = cal.powFun(operation1,operation2);
            }
            int k = i + 1;
            functions.put("f" + k,tempFun);
        }
        return functions.get("f" + num);
    }

    public ArrayList<Factor> translate(HashMap<String,
            ArrayList<Factor>> functions,String operation) {
        if (operation.matches("f[1-9][0-9]*")) {
            return functions.get(operation);
        }
        else if (operation.equals("x")) {
            Factor tempFactor = new Factor(1);
            ArrayList<Factor> tempFactors = new ArrayList<>();
            tempFactors.add(tempFactor);
            return tempFactors;
        }
        else {
            Factor tempFactor;
            ArrayList<Factor> tempFactors = new ArrayList<>();
            BigInteger coe = new BigInteger(operation);
            tempFactor = new Factor(coe);
            tempFactors.add(tempFactor);
            return tempFactors;
        }
    }
}
