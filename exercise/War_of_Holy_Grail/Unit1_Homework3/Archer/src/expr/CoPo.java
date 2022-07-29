package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class CoPo {
    private BigInteger coefficient;
    private BigInteger powerX;
    private ArrayList<TriItem> triItems;

    public boolean equal(CoPo other) {
        return (this.getCoefficient().compareTo(other.getCoefficient()) == 0 &&
                this.polymeriza(other));
    }

    public boolean polymeriza(CoPo other) {
        if (this.getTriItems().size() != other.getTriItems().size()) {
            return false;
        } else if (this.getPowerX().compareTo(other.getPowerX()) != 0) {
            return false;
        } else {
            HashMap<TriItem, Integer> thisMap = new HashMap<>();
            HashMap<TriItem, Integer> otherMap = new HashMap<>();
            ArrayList<TriItem> thisArray = this.getTriItems();
            ArrayList<TriItem> otherArray = other.getTriItems();
            for (TriItem t : thisArray) {
                thisMap.put(t, 0);
            }
            for (TriItem t : otherArray) {
                otherMap.put(t, 0);
            }
            out: for (TriItem tt : thisArray) {
                for (TriItem ot : otherArray) {
                    if (thisMap.get(tt).equals(0) && otherMap.get(ot).equals(0) &&
                            tt.equal(ot)) {
                        thisMap.replace(tt, 1);
                        otherMap.replace(ot, 1);
                        continue out;
                    }
                }
                return false;
            }
            return true;
        }
    }

    public CoPo(BigInteger coefficient, BigInteger powerX) {
        this.coefficient = coefficient;
        this.powerX = powerX;
        this.triItems = new ArrayList<>();
    }

    public void setCoefficient(BigInteger coefficient) { this.coefficient = coefficient; }

    public void setPowerX(BigInteger power) { this.powerX = power; }

    public BigInteger getCoefficient() { return coefficient; }

    public BigInteger getPowerX() { return powerX; }

    public void setTriItems(ArrayList<TriItem> triItems) {
        this.triItems.clear();
        this.triItems.addAll(triItems);
    }

    public void setTriItem(TriItem triItem) {
        this.triItems.clear();
        this.triItems.add(triItem);
    }

    public ArrayList<TriItem> getTriItems() { return this.triItems; }

    public TriItem getTriItem(int i) {
        return triItems.get(i);
    }

    public void addTriItem(TriItem triItem) {
        triItems.add(triItem);
    }

    public void addTriItems(ArrayList<TriItem> triItems) { this.triItems.addAll(triItems); }

    public void removeTriItem(int i) { triItems.remove(i); }

    public int sizeTriItems() {
        return triItems.size();
    }

}
