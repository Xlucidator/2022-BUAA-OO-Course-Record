import java.util.Comparator;

class CosComparator implements Comparator<Cos> {
    @Override
    public int compare(Cos o1, Cos o2) {
        Factor cos1 = o1.getFac();
        Factor cos2 = o2.getFac();
        if (cos1.getExp().compareTo(cos2.getExp()) > 0) {
            return 1;
        }
        else if (cos1.getExp().compareTo(cos2.getExp()) < 0) {
            return -1;
        }
        else if (cos1.getCoef().compareTo(cos2.getCoef()) > 0) {
            return 1;
        }
        else if (cos1.getCoef().compareTo(cos2.getCoef()) < 0) {
            return -1;
        }
        return 0;
    }
}