import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.interact.format.UserApi;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlTransition;
import self.model.ClassModel;
import self.model.CollaborationModel;
import self.model.InteractionModel;
import self.model.InterfaceModel;
import self.model.OperationModel;
import self.model.StateMachineModel;
import self.model.TransitionModel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class MyImplementation implements UserApi {
    /* ============================ ClassDiagram ============================ */
    private final HashMap<String, ClassModel> name2Class = new HashMap<>();
    private final HashMap<String, ClassModel> id2Class = new HashMap<>();
    private final HashSet<String> dupNameClasses = new HashSet<>(); //<name>
    private final HashMap<String, Integer> name2ClassAttCouplingDegree = new HashMap<>();
    private final HashMap<String, InterfaceModel> id2Interface = new HashMap<>();
    private final HashMap<String, ArrayList<String>> className2InterfaceList = new HashMap<>();
    private final HashMap<String, OperationModel> id2Operation = new HashMap<>();
    private final HashMap<String, UmlAssociationEnd> id2AsEnd = new HashMap<>();
    /* ============================ StateMachine ============================ */
    private final HashMap<String, StateMachineModel> name2Machine = new HashMap<>();
    private final HashMap<String, StateMachineModel> id2Machine = new HashMap<>();
    private final HashSet<String> dupNameMachines = new HashSet<>();
    private final HashMap<String, StateMachineModel> rid2Machine = new HashMap<>();
    private final HashMap<String, TransitionModel> id2Trans = new HashMap<>();
    /* =========================== SequenceDiagram ========================== */
    private final HashMap<String, CollaborationModel> id2Collaboration = new HashMap<>();
    private final HashMap<String, InteractionModel> name2Interact = new HashMap<>();
    private final HashMap<String, InteractionModel> id2Interact = new HashMap<>();
    private final HashSet<String> dupNameInteracts = new HashSet<>();
    /* =========================== GrammarChecker ========================== */
    private boolean umlR001 = false;
    private final HashSet<UmlClassOrInterface> umlR003 = new HashSet<>();
    private boolean umlR005 = false;

    public MyImplementation(UmlElement... elements) {
        storeArterialEntity(elements);
        completeEntity(elements);    // after: UMLOperation has its complete information
        storeRelations(elements);
        name2Machine.values().forEach(StateMachineModel::setGlobalCritical);
    }

    private void checkNameNull(String name) {
        umlR001 = umlR001 || (name == null || name.matches("^[ \t]*$"));
    }

    private void storeArterialEntity(UmlElement... elements) {
        String umlName;
        String umlId;
        for (UmlElement umlElement: elements) {
            umlName = umlElement.getName();
            umlId = umlElement.getId();
            switch (umlElement.getElementType()) {
                case UML_CLASS:
                    checkNameNull(umlName);
                    ClassModel myClass = new ClassModel(umlName, umlId);
                    id2Class.put(umlId, myClass); // (umlId is unique)
                    if (name2Class.containsKey(umlName)) {
                        dupNameClasses.add(umlName);
                    } else {
                        name2Class.put(umlName, myClass);
                    } // case: same umlName & different umlId
                    break;
                case UML_INTERFACE:
                    checkNameNull(umlName);
                    id2Interface.put(umlId, new InterfaceModel(umlName, umlId));
                    break;
                case UML_OPERATION:
                    checkNameNull(umlName);
                    id2Operation.put(umlId, new OperationModel((UmlOperation) umlElement));
                    break;
                case UML_ASSOCIATION_END:
                    id2AsEnd.put(umlId, (UmlAssociationEnd) umlElement);
                    break;
                /* ================================================================ */
                case UML_STATE_MACHINE:
                    StateMachineModel myMachine = new StateMachineModel(umlName, umlId);
                    id2Machine.put(umlId, myMachine);
                    if (name2Machine.containsKey(umlName)) {
                        dupNameMachines.add(umlName);
                    } else {
                        name2Machine.put(umlName, myMachine);
                    } // same umlName & different umlId: we don't care
                    break;
                /* ================================================================ */
                case UML_COLLABORATION:
                    CollaborationModel myCollaboration = new CollaborationModel(umlId);
                    id2Collaboration.put(umlId, myCollaboration);
                    break;
                case UML_INTERACTION:
                    InteractionModel myInteracts = new InteractionModel(umlName, umlId);
                    id2Interact.put(umlId, myInteracts);
                    if (name2Interact.containsKey(umlName)) {
                        dupNameInteracts.add(umlName);
                    } else {
                        name2Interact.put(umlName, myInteracts);
                    }
                    break;
                default:;
            }
        }
    }

    private void completeEntity(UmlElement... elements) {
        ElementType umlType;
        for (UmlElement umlElement: elements) {
            umlType = umlElement.getElementType();
            if (umlType == ElementType.UML_PARAMETER) {
                if (((UmlParameter) umlElement).getDirection() != Direction.RETURN) {
                    checkNameNull(umlElement.getName());
                }
                id2Operation.get(umlElement.getParentId()).addPara((UmlParameter) umlElement);
            } else if (umlType == ElementType.UML_REGION) {
                rid2Machine.put(umlElement.getId(), id2Machine.get(umlElement.getParentId()));
            }
        }
        String umlParentId;
        for (UmlElement umlElement: elements) {
            umlType = umlElement.getElementType();
            umlParentId = umlElement.getParentId();
            switch (umlType) {
                case UML_ASSOCIATION:
                    UmlAssociationEnd end1 = id2AsEnd.get(((UmlAssociation) umlElement).getEnd1());
                    UmlAssociationEnd end2 = id2AsEnd.get(((UmlAssociation) umlElement).getEnd2());
                    if (id2Class.containsKey(end1.getReference())) {
                        id2Class.get(end1.getReference()).addAssociationEndName(end2.getName());
                    }
                    if (id2Class.containsKey(end2.getReference())) {
                        id2Class.get(end2.getReference()).addAssociationEndName(end1.getName());
                    }
                    break;
                /* ================================================================ */
                case UML_PSEUDOSTATE:
                    rid2Machine.get(umlParentId).setStart((UmlPseudostate) umlElement);
                    break;
                case UML_FINAL_STATE:
                    rid2Machine.get(umlParentId).addEnd((UmlFinalState) umlElement);
                    break;
                case UML_STATE:
                    rid2Machine.get(umlParentId).addState((UmlState) umlElement);
                    break;
                case UML_TRANSITION:
                    TransitionModel myTrans = new TransitionModel((UmlTransition) umlElement);
                    id2Trans.put(myTrans.getId(), myTrans);
                    rid2Machine.get(umlParentId).addTransition(myTrans);
                    break;
                /* ================================================================ */
                case UML_INTERACTION:
                    id2Collaboration.get(umlParentId).addInteraction(
                            id2Interact.get(umlElement.getId()));
                    break;
                case UML_LIFELINE:
                    id2Interact.get(umlParentId).addLifeline((UmlLifeline) umlElement);
                    break;
                case UML_ENDPOINT:
                    id2Interact.get(umlParentId).addEndPoint((UmlEndpoint) umlElement);
                    break;
                default:;
            }
        }
    }

    private void checkVisibilityNotPublic(UmlAttribute umlAttribute) {
        umlR005 = umlR005 | (umlAttribute.getVisibility() != Visibility.PUBLIC);
    }

    private void storeRelations(UmlElement... elements) {
        ElementType umlType;
        String umlId;
        String umlParentId;
        for (UmlElement umlElement: elements) {
            umlType = umlElement.getElementType();
            umlId = umlElement.getId();
            umlParentId = umlElement.getParentId();
            switch (umlType) {
                case UML_ATTRIBUTE:
                    if (id2Class.containsKey(umlParentId)) {
                        checkNameNull(umlElement.getName());
                        id2Class.get(umlParentId).addAttribute((UmlAttribute) umlElement);
                    } else if (id2Interface.containsKey(umlParentId)) {
                        checkVisibilityNotPublic((UmlAttribute) umlElement);
                    } else if (id2Collaboration.containsKey(umlParentId)) {
                        id2Collaboration.get(umlParentId).addAttributeId(umlId);
                    }
                    break;
                case UML_OPERATION:
                    id2Class.get(umlParentId).addOperation(id2Operation.get(umlId));
                    break;
                case UML_GENERALIZATION:
                    String src = ((UmlGeneralization) umlElement).getSource(); // son
                    String tar = ((UmlGeneralization) umlElement).getTarget(); // father
                    if (id2Class.containsKey(src)) {  // Class extends Class
                        id2Class.get(src).setFatherClass(id2Class.get(tar));
                        id2Class.get(tar).addSonClass(id2Class.get(src));
                    } else { // Interface extends Interface
                        id2Interface.get(src).addFatherInterface(id2Interface.get(tar));
                    }
                    break;
                case UML_INTERFACE_REALIZATION:
                    String base = ((UmlInterfaceRealization) umlElement).getSource();  // son
                    String inter = ((UmlInterfaceRealization) umlElement).getTarget(); // father
                    id2Class.get(base).addInterface(id2Interface.get(inter));
                    break;
                /* ================================================================ */
                case UML_EVENT:
                    id2Trans.get(umlParentId).addEvent((UmlEvent) umlElement);
                    break;
                /* ================================================================ */
                case UML_MESSAGE:
                    id2Interact.get(umlParentId).addMessage((UmlMessage) umlElement);
                    break;
                default:;
            }
        }
    }

    public int getClassCount() {
        return id2Class.size();
    }

    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        NameExceptionThrower.detectClassNameException(className, name2Class, dupNameClasses);
        return name2Class.get(className).getSonCount();
    }

    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        NameExceptionThrower.detectClassNameException(className, name2Class, dupNameClasses);
        return name2Class.get(className).getOpCount();
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        NameExceptionThrower.detectClassNameException(className, name2Class, dupNameClasses);
        return name2Class.get(className).getOpVis(methodName);
    }

    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        NameExceptionThrower.detectClassNameException(className, name2Class, dupNameClasses);
        return name2Class.get(className).getOpCouplingDegree(methodName);
    }

    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        NameExceptionThrower.detectClassNameException(className, name2Class, dupNameClasses);
        if (name2ClassAttCouplingDegree.containsKey(className)) {
            return name2ClassAttCouplingDegree.get(className);
        }
        ClassModel self = name2Class.get(className);
        HashSet<String> referenceTypeSet = new HashSet<>(self.getAttributeRefTypeSet());
        referenceTypeSet.add(self.getId());
        while (self.hasFatherClass()) {
            self = self.getFatherClass();
            referenceTypeSet.addAll(self.getAttributeRefTypeSet());
        }
        name2ClassAttCouplingDegree.put(className, referenceTypeSet.size() - 1);
        return referenceTypeSet.size() - 1;
    }

    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        NameExceptionThrower.detectClassNameException(className, name2Class, dupNameClasses);
        if (className2InterfaceList.containsKey(className)) {
            return className2InterfaceList.get(className);
        }
        ClassModel self = name2Class.get(className);
        HashSet<String> interfaceNameSet = new HashSet<>(self.getClassTotalInterfaceNameSet());
        while (self.hasFatherClass()) {
            self = self.getFatherClass();
            interfaceNameSet.addAll(self.getClassTotalInterfaceNameSet());
        }
        ArrayList<String> interfaceNameList = new ArrayList<>(interfaceNameSet);
        className2InterfaceList.put(className, interfaceNameList);
        return interfaceNameList;
    }

    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        NameExceptionThrower.detectClassNameException(className, name2Class, dupNameClasses);
        ClassModel self = name2Class.get(className);
        int depth = 0;
        while (self.hasFatherClass()) {
            depth++;
            self = self.getFatherClass();
        }
        return depth;
    }

    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        NameExceptionThrower.detectInteractNameException(interactionName,
                name2Interact, dupNameInteracts);
        return name2Interact.get(interactionName).getLifelineCount();
    }

    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        NameExceptionThrower.detectInteractNameException(interactionName,
                name2Interact, dupNameInteracts);
        return name2Interact.get(interactionName).getLifelineCreator(lifelineName);
    }

    public Pair<Integer, Integer>
            getParticipantLostAndFound(String interactionName, String lifelineName) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        NameExceptionThrower.detectInteractNameException(interactionName,
                name2Interact, dupNameInteracts);
        return name2Interact.get(interactionName).getLifelineLostAndFound(lifelineName);
    }

    public int getStateCount(String machineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        NameExceptionThrower.detectMachineNameException(machineName, name2Machine, dupNameMachines);
        return name2Machine.get(machineName).getStateCount();
    }

    public boolean getStateIsCriticalPoint(String machineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        NameExceptionThrower.detectMachineNameException(machineName, name2Machine, dupNameMachines);
        return name2Machine.get(machineName).isStateCritical(stateName);
    }

    public List<String> getTransitionTrigger(String machineName,
                                             String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        NameExceptionThrower.detectMachineNameException(machineName, name2Machine, dupNameMachines);
        return name2Machine.get(machineName).getTransTrigger(sourceStateName, targetStateName);
    }

    public void checkForUml001() throws UmlRule001Exception {
        if (umlR001) {
            throw new UmlRule001Exception();
        }
    }

    public void checkForUml002() throws UmlRule002Exception {
        HashSet<AttributeClassInformation> attPairs = new HashSet<>();
        for (ClassModel myClass : id2Class.values()) {
            attPairs.addAll(myClass.getDupClassAttributePairs());
        }
        if (attPairs.size() != 0) {
            throw new UmlRule002Exception(attPairs);
        }
    }

    public void checkForUml003() throws UmlRule003Exception {
        for (String startId: id2Class.keySet()) {
            ArrayList<String> path = new ArrayList<>();
            dfs4Class(startId, path);
        }
        for (String startId: id2Interface.keySet()) {
            ArrayList<String> path = new ArrayList<>();
            dfs4Interface(startId, path);
        }
        if (umlR003.size() > 0) {
            throw new UmlRule003Exception(umlR003);
        }
    }

    private void dfs4Class(String stepIn, ArrayList<String> path) {
        int index = path.indexOf(stepIn);
        if (index >= 0) {       // form a loop
            for (int i = index; i < path.size(); ++i) {
                umlR003.add(id2Class.get(path.get(i)));
            }
            return;
        }
        path.add(stepIn);
        String nextId = id2Class.get(stepIn).getFatherClassId();
        if (nextId != null) {
            dfs4Class(nextId, path);
        }
        path.remove(stepIn);
    }

    private void dfs4Interface(String stepIn, ArrayList<String> path) {
        int index = path.indexOf(stepIn);
        if (index >= 0) {
            for (int i = index; i < path.size(); ++i) {
                umlR003.add(id2Interface.get(path.get(i)));
            }
            return;
        }
        path.add(stepIn);
        for (String nextId: id2Interface.get(stepIn).getFatherInterfaceIds()) {
            dfs4Interface(nextId, path);
        }
        path.remove(stepIn);
    }

    public void checkForUml004() throws UmlRule004Exception {
        HashSet<UmlClassOrInterface> dupSet = new HashSet<>();
        for (InterfaceModel myInterface: id2Interface.values()) {
            HashSet<String> idSet = new HashSet<>();
            if (myInterface.checkDupGeneralize(idSet)) {
                dupSet.add(myInterface);
            }
        }
        if (dupSet.size() > 0) {
            throw new UmlRule004Exception(dupSet);
        }
    }

    public void checkForUml005() throws UmlRule005Exception {
        if (umlR005) {
            throw new UmlRule005Exception();
        }
    }

    public void checkForUml006() throws UmlRule006Exception {
        for (CollaborationModel myCollaboration: id2Collaboration.values()) {
            myCollaboration.checkAttributeRepresent();
        }
    }

    public void checkForUml007() throws UmlRule007Exception {
        for (InteractionModel myInteraction: id2Interact.values()) {
            myInteraction.checkReceiveAfterDestroyed();
        }
    }

    public void checkForUml008() throws UmlRule008Exception {
        for (StateMachineModel myStateMachine: rid2Machine.values()) {
            myStateMachine.checkTransFromEnd();
        }
    }

    public void checkForUml009() throws UmlRule009Exception {
        for (StateMachineModel myStateMachine: rid2Machine.values()) {
            myStateMachine.checkStateSameTransTrig();
        }
    }
}
