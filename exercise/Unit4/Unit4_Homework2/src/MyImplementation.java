import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UserApi;

import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlTransition;
import self.model.ClassModel;
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
    private final HashMap<String, ClassModel> name2Class = new HashMap<>();//<name, class>
    private final HashMap<String, ClassModel> id2Class = new HashMap<>(); //<id, class>
    private final HashSet<String> dupNameClasses = new HashSet<>(); //<name>
    private final HashMap<String, Integer> name2ClassAttCouplingDegree = new HashMap<>();
    private final HashMap<String, InterfaceModel> id2Interface = new HashMap<>();//<id, interface>
    private final HashMap<String, ArrayList<String>> className2InterfaceList = new HashMap<>();
    private final HashMap<String, OperationModel> id2Operation = new HashMap<>();//<id, operation>

    /* ============================ StateMachine ============================ */
    private final HashMap<String, StateMachineModel> name2Machine = new HashMap<>();
    private final HashMap<String, StateMachineModel> id2Machine = new HashMap<>();
    private final HashSet<String> dupNameMachines = new HashSet<>();
    private final HashMap<String, StateMachineModel> rid2Machine = new HashMap<>();
    private final HashMap<String, TransitionModel> id2Trans = new HashMap<>();

    /* =========================== SequenceDiagram ========================== */
    private final HashMap<String, InteractionModel> name2Interact = new HashMap<>();
    private final HashMap<String, InteractionModel> id2Interact = new HashMap<>();
    private final HashSet<String> dupNameInteracts = new HashSet<>();

    public MyImplementation(UmlElement... elements) {
        storeArterialEntity(elements);
        completeEntity(elements);    // after: UMLOperation has its complete information
        storeRelations(elements);

        name2Machine.values().forEach(StateMachineModel::setGlobalCritical);
    }

    private void storeArterialEntity(UmlElement... elements) {
        String umlName;
        String umlId;
        for (UmlElement umlElement: elements) {
            umlName = umlElement.getName();
            umlId = umlElement.getId();
            switch (umlElement.getElementType()) {
                case UML_CLASS:
                    ClassModel myClass = new ClassModel(umlName, umlId);
                    id2Class.put(umlId, myClass); // (umlId is unique)
                    if (name2Class.containsKey(umlName)) {
                        dupNameClasses.add(umlName);
                    } else {
                        name2Class.put(umlName, myClass);
                    } // case: same umlName & different umlId
                    break;
                case UML_INTERFACE:
                    id2Interface.put(umlId, new InterfaceModel(umlName));
                    break;
                case UML_OPERATION:
                    Visibility visibility = ((UmlOperation) umlElement).getVisibility();
                    id2Operation.put(umlId, new OperationModel(umlName, visibility,
                            umlElement.getParentId()));
                    break;

                case UML_STATE_MACHINE:
                    StateMachineModel myMachine = new StateMachineModel(umlName, umlId);
                    id2Machine.put(umlId, myMachine);
                    if (name2Machine.containsKey(umlName)) {
                        dupNameMachines.add(umlName);
                    } else {
                        name2Machine.put(umlName, myMachine);
                    } // same umlName & different umlId: we don't care
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
                id2Operation.get(umlElement.getParentId())
                        .addPara((UmlParameter) umlElement);
            } else if (umlType == ElementType.UML_REGION) {
                rid2Machine.put(umlElement.getId(),
                        id2Machine.get(umlElement.getParentId()));
            }
        }
        String umlParentId;
        for (UmlElement umlElement: elements) {
            umlType = umlElement.getElementType();
            umlParentId = umlElement.getParentId();
            switch (umlType) {
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
                    TransitionModel myTrans = new TransitionModel(
                            umlElement.getId(),
                            ((UmlTransition) umlElement).getSource(),
                            ((UmlTransition) umlElement).getTarget()
                    );
                    id2Trans.put(myTrans.getId(), myTrans);
                    rid2Machine.get(umlParentId).addTransition(myTrans);
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
                        id2Class.get(umlParentId).addAttribute((UmlAttribute) umlElement);
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

                case UML_EVENT:
                    id2Trans.get(umlParentId).addEvent((UmlEvent) umlElement);
                    break;

                case UML_MESSAGE:
                    id2Interact.get(umlParentId).addMessage((UmlMessage) umlElement);
                    break;

                default:;
            }
        }
    }

    @Override
    public int getClassCount() {
        return id2Class.size();
    }

    @Override
    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Class.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (dupNameClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        return name2Class.get(className).getSonCount();
    }

    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Class.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (dupNameClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        return name2Class.get(className).getOpCount();
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Class.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (dupNameClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        return name2Class.get(className).getOpVis(methodName);
    }

    @Override
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        if (!name2Class.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (dupNameClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        return name2Class.get(className).getOpCouplingDegree(methodName);
    }

    @Override
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Class.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (dupNameClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }

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

    @Override
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Class.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (dupNameClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }

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

    @Override
    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Class.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (dupNameClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        ClassModel self = name2Class.get(className);
        int depth = 0;
        while (self.hasFatherClass()) {
            depth++;
            self = self.getFatherClass();
        }
        return depth;
    }

    @Override
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2Interact.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (dupNameInteracts.contains(interactionName)) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return name2Interact.get(interactionName).getLifelineCount();
    }

    @Override
    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        if (!name2Interact.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (dupNameInteracts.contains(interactionName)) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return name2Interact.get(interactionName).getLifelineCreator(lifelineName);
    }

    @Override
    public Pair<Integer, Integer>
            getParticipantLostAndFound(String interactionName, String lifelineName) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2Interact.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (dupNameInteracts.contains(interactionName)) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return name2Interact.get(interactionName).getLifelineLostAndFound(lifelineName);
    }

    @Override
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!name2Machine.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        if (dupNameMachines.contains(stateMachineName)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return name2Machine.get(stateMachineName).getStateCount();
    }

    @Override
    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        if (!name2Machine.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        if (dupNameMachines.contains(stateMachineName)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return name2Machine.get(stateMachineName).isStateCritical(stateName);
    }

    @Override
    public List<String> getTransitionTrigger(String stateMachineName,
                                             String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        if (!name2Machine.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        if (dupNameMachines.contains(stateMachineName)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return name2Machine.get(stateMachineName)
                .getTransTrigger(sourceStateName, targetStateName);
    }
}
