import java.util.HashMap;

public class ExceptionCounter {
    private static final ExceptionCounter ECOUNTER = new ExceptionCounter();
    private HashMap<String, HashMap<Integer, Integer>> e2Record;

    public void initial() {
        e2Record = new HashMap<>();
        e2Record.put("pinf", new HashMap<>());
        e2Record.put("epi", new HashMap<>());
        e2Record.put("rnf", new HashMap<>());
        e2Record.put("er", new HashMap<>());
        e2Record.put("ginf", new HashMap<>());
        e2Record.put("egi", new HashMap<>());
        e2Record.put("emi", new HashMap<>());
        e2Record.put("minf", new HashMap<>());
        e2Record.put("einf", new HashMap<>());
        e2Record.put("eei", new HashMap<>());
    }

    public static ExceptionCounter getInstance() {
        return ECOUNTER;
    }

    public void update(String exp, int id) {
        if (e2Record.get(exp).containsKey(id)) {
            e2Record.get(exp).compute(id, (key, cnt) -> ++cnt);
            //System.out.println("Change to " + e2Record.get(exp).get(id));
            return;
        }
        e2Record.get(exp).put(id, 1);
        //System.out.println("New " + e2Record.get(exp).get(id));
    }

    public int getRecord(String exp, int id) {
        return e2Record.get(exp).get(id);
    }
}
