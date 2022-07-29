import java.util.Comparator;

class SinComparator implements Comparator<Sin> {
    @Override
    public int compare(Sin o1, Sin o2) {
        Factor sin1 = o1.getFac();
        Factor sin2 = o2.getFac();
        if (sin1.getExp().compareTo(sin2.getExp()) > 0) {
            return 1;
        }
        else if (sin1.getExp().compareTo(sin2.getExp()) < 0) {
            return -1;
        }
        else if (sin1.getCoef().compareTo(sin2.getCoef()) > 0) {
            return 1;
        }
        else if (sin1.getCoef().compareTo(sin2.getCoef()) < 0) {
            return -1;
        }
        return 0;
    }
}