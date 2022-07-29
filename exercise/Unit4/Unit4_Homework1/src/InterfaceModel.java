import java.util.ArrayList;
import java.util.HashSet;

public class InterfaceModel {
    private final String name;
    private final ArrayList<InterfaceModel> fatherInterfaces = new ArrayList<>();
    private HashSet<String> totalInterNameSet = null;// interface name is unique

    public InterfaceModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public HashSet<String> getInterTotalNameSet() {
        if (totalInterNameSet != null) {
            return totalInterNameSet;
        }
        HashSet<String> nameSet = new HashSet<>();
        nameSet.add(name);
        for (InterfaceModel interfaceModel: fatherInterfaces) {
            nameSet.addAll(interfaceModel.getInterTotalNameSet());
        }
        totalInterNameSet = nameSet;
        return nameSet;
    }

    public void addFatherInterface(InterfaceModel interfaceModel) {
        this.fatherInterfaces.add(interfaceModel);
    }
}
