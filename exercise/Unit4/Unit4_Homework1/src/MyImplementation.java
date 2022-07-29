import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UserApi;

import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class MyImplementation implements UserApi {
    private final HashMap<String, ClassModel> name2Classes = new HashMap<>();//<name, class>
    private final HashMap<String, ClassModel> id2Classes = new HashMap<>(); //<id, class>
    private final HashSet<String> badClasses = new HashSet<>();             //<name, cd>
    private final HashMap<String, Integer> name2ClassAttCouplingDegree = new HashMap<>();

    private final HashMap<String, InterfaceModel> id2Interfaces = new HashMap<>();//<id, interface>
    private final HashMap<String, ArrayList<String>> className2InterfaceList = new HashMap<>();

    private final HashMap<String, OperationModel> id2Operations = new HashMap<>();//<id, operation>

    public MyImplementation(UmlElement... elements) {
        formClassOperation(elements);
        completeOperation(elements);    // after: UMLOperation has its complete information
        storeOthers(elements);
    }

    private void formClassOperation(UmlElement... elements) {
        String umlName;
        String umlId;
        for (UmlElement umlElement: elements) {
            umlName = umlElement.getName();
            umlId = umlElement.getId();
            switch (umlElement.getElementType()) {
                case UML_CLASS:
                    ClassModel myClass = new ClassModel(umlName, umlId);
                    id2Classes.put(umlId, myClass); // (umlId is unique)
                    if (name2Classes.containsKey(umlName)) {
                        badClasses.add(umlName);
                    } else {
                        name2Classes.put(umlName, myClass);
                    } // case: same umlName & different umlId
                    break;
                case UML_INTERFACE:
                    id2Interfaces.put(umlId, new InterfaceModel(umlName));
                    break;
                case UML_OPERATION:
                    Visibility visibility = ((UmlOperation) umlElement).getVisibility();
                    id2Operations.put(umlId, new OperationModel(umlName, visibility,
                            umlElement.getParentId()));
                    break;
                default:;
            }
        }
    }

    private void completeOperation(UmlElement... elements) {
        for (UmlElement umlElement: elements) {
            if (umlElement.getElementType() == ElementType.UML_PARAMETER) {
                id2Operations.get(umlElement.getParentId())
                        .addPara((UmlParameter) umlElement);
            }
        }
    }

    private void storeOthers(UmlElement... elements) {
        ElementType umlType;
        String umlId;
        String umlParentId;
        for (UmlElement umlElement: elements) {
            umlType = umlElement.getElementType();
            umlId = umlElement.getId();
            umlParentId = umlElement.getParentId();
            switch (umlType) {
                case UML_ATTRIBUTE:
                    id2Classes.get(umlParentId).addAttribute((UmlAttribute) umlElement);
                    break;
                case UML_OPERATION:
                    id2Classes.get(umlParentId).addOperation(id2Operations.get(umlId));
                    break;
                case UML_GENERALIZATION:
                    String src = ((UmlGeneralization) umlElement).getSource(); // son
                    String tar = ((UmlGeneralization) umlElement).getTarget(); // father
                    if (id2Classes.containsKey(src)) {  // Class extends Class
                        id2Classes.get(src).setFatherClass(id2Classes.get(tar));
                        id2Classes.get(tar).addSonClass(id2Classes.get(src));
                    } else { // Interface extends Interface
                        id2Interfaces.get(src).addFatherInterface(id2Interfaces.get(tar));
                    }
                    break;
                case UML_INTERFACE_REALIZATION:
                    String base = ((UmlInterfaceRealization) umlElement).getSource();  // son
                    String inter = ((UmlInterfaceRealization) umlElement).getTarget(); // father
                    id2Classes.get(base).addInterface(id2Interfaces.get(inter));
                    break;
                case UML_ASSOCIATION:
                case UML_ASSOCIATION_END:
                default:;
            }
        }
    }

    @Override
    public int getClassCount() {
        return id2Classes.size();
    }

    @Override
    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (badClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).getSonCount();
    }

    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (badClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).getOpCount();
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (badClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).getOpVis(methodName);
    }

    @Override
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (badClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        return name2Classes.get(className).getOpCouplingDegree(methodName);
    }

    @Override
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (badClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }

        if (name2ClassAttCouplingDegree.containsKey(className)) {
            return name2ClassAttCouplingDegree.get(className);
        }
        ClassModel self = name2Classes.get(className);
        HashSet<String> referenceTypeSet = new HashSet<>(self.getAttributeRefTypeSet());
        referenceTypeSet.add(self.getId());
        while (self.hasFatherClass()) {
            self = self.getFatherClass();
            referenceTypeSet.addAll(self.getAttributeRefTypeSet());
        }
        name2ClassAttCouplingDegree.put(className, referenceTypeSet.size() - 1);
        return referenceTypeSet.size() - 1;
    }

    @Override
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (badClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }

        if (className2InterfaceList.containsKey(className)) {
            return className2InterfaceList.get(className);
        }
        ClassModel self = name2Classes.get(className);
        HashSet<String> interfaceNameSet = new HashSet<>(self.getClassTotalInterfaceNameSet());
        while (self.hasFatherClass()) {
            self = self.getFatherClass();
            interfaceNameSet.addAll(self.getClassTotalInterfaceNameSet());
        }
        ArrayList<String> interfaceNameList = new ArrayList<>(interfaceNameSet);
        className2InterfaceList.put(className, interfaceNameList);
        return interfaceNameList;
    }

    @Override
    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (badClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        ClassModel self = name2Classes.get(className);
        int depth = 0;
        while (self.hasFatherClass()) {
            depth++;
            self = self.getFatherClass();
        }
        return depth;
    }
}
