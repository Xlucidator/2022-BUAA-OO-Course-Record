package self.model;

import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class InterfaceModel implements UmlClassOrInterface {
    private final String name;
    private final String id;
    private final ArrayList<InterfaceModel> fatherInterfaces = new ArrayList<>();
    private HashSet<String> totalInterNameSet = null;// interface name is unique

    public InterfaceModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    public ArrayList<String> getFatherInterfaceIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (InterfaceModel fatherInterface : fatherInterfaces) {
            ids.add(fatherInterface.getId());
        }
        return ids;
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

    public boolean checkDupGeneralize(HashSet<String> idSet) {
        for (InterfaceModel interfaceModel: fatherInterfaces) {
            if (idSet.contains(interfaceModel.getId())) {
                return true; // has dupGeneralization
            }
            idSet.add(interfaceModel.getId());
            if (interfaceModel.checkDupGeneralize(idSet)) {
                return true;
            }
        }
        return false;
    }

    public void addFatherInterface(InterfaceModel interfaceModel) {
        this.fatherInterfaces.add(interfaceModel);
    }

    @Override
    public ElementType getElementType() {
        return ElementType.UML_INTERFACE;
    }

    @Override
    public String getParentId() {
        return null;
    }

    @Override
    public Visibility getVisibility() {
        return null;
    }

    @Override
    public Map<String, Object> toJson() {
        return null;
    }
}
