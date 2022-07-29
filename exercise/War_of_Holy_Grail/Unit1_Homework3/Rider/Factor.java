import java.util.ArrayList;

public interface Factor {
    TriPoly toTriPoly();
    
    Factor clone();
    
    boolean isPre();
    
    Factor replace(ArrayList<Factor> formPars, ArrayList<Factor> parameters);
}


