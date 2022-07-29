public class Leaf {
    private int own;
    private int father;
    private int rank = 1;
    
    public int getFather() {
        return father;
    }
    
    public int getOwn() {
        return own;
    }
    
    public void setFather(int father) {
        this.father = father;
    }
    
    public void setOwn(int own) {
        this.own = own;
    }
    
    public void addRank(int i) {
        rank = rank + i;
    }
    
    public int getRank() {
        return rank;
    }
}
