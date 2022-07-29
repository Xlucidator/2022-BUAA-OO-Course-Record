package self.model;

import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.models.common.ReferenceType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ClassModel {
    private final String name;
    private final String id;
    /* ======= Class ======= */
    private ClassModel fatherClass = null;
    private final ArrayList<ClassModel> sonClasses = new ArrayList<>();
    /* ======= Interface ======= */
    private final ArrayList<InterfaceModel> interfaces = new ArrayList<>();
    private HashSet<String> totalInterfaceSet = null;   // <InterfaceName>
    /* ======= Attribute ======= */
    private final HashSet<String> attributeRefTypeSet = new HashSet<>(); // <referenceTypeId>
    /* ======= Operation(Method) ======= */
    private final ArrayList<OperationModel> operations = new ArrayList<>();
    private final HashMap<String, HashMap<Visibility, Integer>> opVis = new HashMap<>();
    private final HashMap<String, Boolean> opDuplicate = new HashMap<>();
    private final HashSet<String> opTypeError = new HashSet<>();

    public ClassModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public HashSet<String> getAttributeRefTypeSet() {
        return attributeRefTypeSet;
    }

    public int getSonCount() {
        return sonClasses.size();
    }

    public int getOpCount() {
        return  operations.size();
    }

    public Map<Visibility, Integer> getOpVis(String methodName) {
        if (!opVis.containsKey(methodName)) {
            return new HashMap<Visibility, Integer>() {
                {
                    put(Visibility.PUBLIC, 0);
                    put(Visibility.PROTECTED, 0);
                    put(Visibility.PRIVATE, 0);
                    put(Visibility.PACKAGE, 0);
                }
            };
        }
        return opVis.get(methodName);
    }

    public List<Integer> getOpCouplingDegree(String methodName)
            throws MethodWrongTypeException, MethodDuplicatedException {
        if (opTypeError.contains(methodName)) {
            throw new MethodWrongTypeException(name, methodName);
        }
        if (!opDuplicate.containsKey(methodName)) {
            return new ArrayList<>();
        }
        if (opDuplicate.get(methodName)) {
            throw new MethodDuplicatedException(name, methodName);
        }

        ArrayList<Integer> couplingDegree = new ArrayList<>();
        for (OperationModel op: operations) {
            if (methodName.equals(op.getName())) {
                couplingDegree.add(op.getCouplingDegree());
            }
        }
        return couplingDegree;
    }

    public HashSet<String> getClassTotalInterfaceNameSet() {
        if (totalInterfaceSet != null) {
            return totalInterfaceSet;
        }
        HashSet<String> interfaceNameSet = new HashSet<>();
        for (InterfaceModel interfaceModel: interfaces) {
            interfaceNameSet.addAll(interfaceModel.getInterTotalNameSet());
        }
        totalInterfaceSet = interfaceNameSet;
        return interfaceNameSet;
    }

    public boolean hasFatherClass() {
        return fatherClass != null;
    }

    public ClassModel getFatherClass() {
        return fatherClass;
    }

    public void setFatherClass(ClassModel fatherClass) {
        this.fatherClass = fatherClass;
    }

    public void addSonClass(ClassModel sonClass) {
        this.sonClasses.add(sonClass);
    }

    public void addInterface(InterfaceModel umlInterface) {
        this.interfaces.add(umlInterface);
    }

    public void addAttribute(UmlAttribute attribute) {
        if (attribute.getType() instanceof ReferenceType) {
            attributeRefTypeSet.add(((ReferenceType) attribute.getType()).getReferenceId());
        }
    }

    public void addOperation(OperationModel operation) {
        String opName = operation.getName();
        Visibility opVisibility = operation.getVisibility();

        if (!opDuplicate.containsKey(opName)) {   // "opName" method not exist
            opDuplicate.put(opName, false);
        } else if (!opDuplicate.get(opName)) {    // "opName" method don't have dupOperation
            for (OperationModel op: operations) {
                if (operation.isOperationEqual(op)) {
                    opDuplicate.put(opName, true);
                    break;
                }
            }
        }

        if (!opVis.containsKey(opName)) {
            opVis.put(opName, new HashMap<Visibility, Integer>() {
                {
                    put(Visibility.PUBLIC, 0);
                    put(Visibility.PROTECTED, 0);
                    put(Visibility.PRIVATE, 0);
                    put(Visibility.PACKAGE, 0);
                }
            });
        }
        opVis.get(opName).put(opVisibility, opVis.get(opName).get(opVisibility) + 1);

        if (operation.isNamedTypeError()) {
            opTypeError.add(opName);
        }

        operations.add(operation);
    }
}
