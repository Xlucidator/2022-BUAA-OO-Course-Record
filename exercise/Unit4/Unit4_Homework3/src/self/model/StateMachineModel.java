package self.model;

import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class StateMachineModel {
    private final String name;
    private final String id;
    /* ======= State ======= */
    private String startId = null;
    private final HashSet<String> endIds = new HashSet<>();
    private final HashMap<String, UmlState> name2State = new HashMap<>();
    private final HashMap<String, UmlState> id2State = new HashMap<>();
    private final HashSet<String> dupNameStates = new HashSet<>();
    /* ======= Transition (include critical point judge) ======= */
    private final HashMap<String, ArrayList<TransitionModel>> srcId2Trans = new HashMap<>();
    private final HashMap<String, Boolean> name2Critical = new HashMap<>(); // <stateName, bool>
    private boolean globalCritical;
    private HashSet<String> hitEndMark;

    public StateMachineModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void checkTransFromEnd() throws UmlRule008Exception {
        for (String endId: endIds) {
            if (srcId2Trans.containsKey(endId)) {
                throw new UmlRule008Exception();
            }
        }
    }

    public void checkStateSameTransTrig() throws UmlRule009Exception {
        ArrayList<String> allStateIds = new ArrayList<>(id2State.keySet());
        allStateIds.add(startId);
        for (String srcId: allStateIds) {
            if (!srcId2Trans.containsKey(srcId)) { // some state may not have transition in/out
                continue;
            }
            HashMap<String, ArrayList<TransitionModel>> trigName2Trans = new HashMap<>();
            for (TransitionModel trans: srcId2Trans.get(srcId)) {
                for (UmlEvent umlEvent: trans.getEvents()) {
                    String eventName = umlEvent.getName();
                    if (!trigName2Trans.containsKey(umlEvent.getName())) {
                        ArrayList<TransitionModel> transList = new ArrayList<>();
                        transList.add(trans);
                        trigName2Trans.put(eventName, transList);
                    } else {
                        for (TransitionModel tarTrans: trigName2Trans.get(eventName)) {
                            if (trans.guardOverlaps(tarTrans)) {
                                throw new UmlRule009Exception();
                            }
                        }
                        trigName2Trans.get(eventName).add(trans);
                    }
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public int getStateCount() {
        return endIds.size() + id2State.size() + 1;
    }

    public void setGlobalCritical() {
        hitEndMark = new HashSet<>();
        HashSet<String> stateMark = new HashSet<>();
        traverseDfs(stateMark, startId);
        globalCritical = (hitEndMark.size() > 0);
    }

    public boolean isStateCritical(String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        if (!name2State.containsKey(stateName)) {
            throw new StateNotFoundException(name, stateName);
        } else if (dupNameStates.contains(stateName)) {
            throw new StateDuplicatedException(name, stateName);
        }

        if (name2Critical.containsKey(stateName)) {
            return name2Critical.get(stateName);
        }

        if (!globalCritical) {  // can never be critical
            name2Critical.put(stateName, false);
            return false;
        }
        hitEndMark = new HashSet<>();
        HashSet<String> stateMark = new HashSet<>(); // track, state can't be traversed twice
        stateMark.add(name2State.get(stateName).getId());
        /* dfs begin */ // System.out.println("dfs for " + stateName + " begin");
        traverseDfs(stateMark, startId);
        boolean isCritical = (hitEndMark.size() == 0);
        name2Critical.put(stateName, isCritical);
        return isCritical;
    }

    private void traverseDfs(HashSet<String> stateMark, String srcId) {
        // System.out.print("-> " + srcId + " ");
        if (endIds.contains(srcId)) {
            // System.out.println("[hit!]");
            hitEndMark.add(srcId);
            return;
        }
        stateMark.add(srcId);
        if (srcId2Trans.containsKey(srcId)) {
            for (TransitionModel trans: srcId2Trans.get(srcId)) {
                String tarId = trans.getTarId();
                if (!stateMark.contains(tarId)) {
                    traverseDfs(stateMark, tarId);
                }
            }
        }
        stateMark.remove(srcId);
        // System.out.print("<- " + srcId + "\n");
    }

    public ArrayList<String> getTransTrigger(String srcName, String tarName)
            throws StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        if (!name2State.containsKey(srcName)) {
            throw new StateNotFoundException(name, srcName);
        } else if (dupNameStates.contains(srcName)) {
            throw new StateDuplicatedException(name, srcName);
        }
        if (!name2State.containsKey(tarName)) {
            throw new StateNotFoundException(name, tarName);
        } else if (dupNameStates.contains(tarName)) {
            throw new StateDuplicatedException(name, tarName);
        }

        ArrayList<String> res = new ArrayList<>();
        String srcId = name2State.get(srcName).getId();
        String tarId = name2State.get(tarName).getId();

        if (!srcId2Trans.containsKey(srcId)) {
            throw new TransitionNotFoundException(name, srcName, tarName);
        }
        boolean hitFlag = false;
        for (TransitionModel trans: srcId2Trans.get(srcId)) {
            if (trans.getTarId().equals(tarId)) {
                res.addAll(trans.getEventNames());
                hitFlag = true;
            }
        }
        if (!hitFlag) {
            throw new TransitionNotFoundException(name, srcName, tarName);
        }
        return res;
    }

    public void setStart(UmlPseudostate start) {
        this.startId = start.getId();
    }

    public void addEnd(UmlFinalState end) {
        this.endIds.add(end.getId());
    }

    public void addState(UmlState umlState) {
        id2State.put(umlState.getId(), umlState);
        String name = umlState.getName();
        if (name2State.containsKey(name)) {
            dupNameStates.add(name);
        } else {
            name2State.put(name, umlState);
        }
    }

    public void addTransition(TransitionModel myTrans) {
        String srcId = myTrans.getSrcId();
        if (!srcId2Trans.containsKey(srcId)) {
            srcId2Trans.put(srcId, new ArrayList<>());
        }
        srcId2Trans.get(srcId).add(myTrans);
    }

}
