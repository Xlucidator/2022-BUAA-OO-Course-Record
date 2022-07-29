import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import self.model.ClassModel;
import self.model.InteractionModel;
import self.model.StateMachineModel;

import java.util.HashMap;
import java.util.HashSet;

public class NameExceptionThrower {
    public static void detectClassNameException(String className,
                                                HashMap<String, ClassModel> name2Class,
                                                HashSet<String> dupNameClasses)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2Class.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (dupNameClasses.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
    }

    public static void detectInteractNameException(String interactionName,
                                                   HashMap<String, InteractionModel> name2Interact,
                                                   HashSet<String> dupNameInteracts)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2Interact.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (dupNameInteracts.contains(interactionName)) {
            throw new InteractionDuplicatedException(interactionName);
        }
    }

    public static void detectMachineNameException(String stateMachineName,
                                                  HashMap<String, StateMachineModel> name2Machine,
                                                  HashSet<String> dupNameMachines)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!name2Machine.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (dupNameMachines.contains(stateMachineName)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
    }
}
