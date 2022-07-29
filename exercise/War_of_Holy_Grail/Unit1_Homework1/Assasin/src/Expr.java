import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expr {
    private ArrayList<Item> itemArray = new ArrayList<>();

    public Expr() {
    }

    public Expr(String string) {
        String sample = "[\\+-]([^\\(\\+-]*(\\(.*?\\))?[^\\(\\+-]*)*";
        Pattern p = Pattern.compile(sample);
        Matcher matcher = p.matcher(string);
        while (matcher.find()) {
            itemArray.add(new Item(matcher.group().replaceAll("#", "/*-")));
        }

    }

    public ArrayList<Item> getItemArray() {
        return itemArray;
    }

    public String calculate() {
        String s = "";
        for (Item i : itemArray) {
            s += i.calculate();
        }
        return s;
    }
}
