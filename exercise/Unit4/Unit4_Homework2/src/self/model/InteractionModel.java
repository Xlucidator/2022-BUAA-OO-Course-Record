package self.model;

import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.HashMap;
import java.util.HashSet;

public class InteractionModel {
    private final String name;
    private final String id;
    /* ======= Lifeline ======= */
    private final HashMap<String, UmlLifeline> name2Lifeline = new HashMap<>();
    private final HashMap<String, UmlLifeline> id2Lifeline = new HashMap<>();
    private final HashSet<String> dupNameLifeline = new HashSet<>();
    /* ======= (about) EndPoint ======= */
    private final HashMap<String, UmlEndpoint> id2Endpoint = new HashMap<>();
    private final HashMap<String, Integer> id2FoundCount = new HashMap<>();
    private final HashMap<String, Integer> id2LostCount = new HashMap<>();
    /* ======= (about) Message ======= */
    private final HashMap<String, UmlMessage> lid4CreateMessage = new HashMap<>();
    private final HashSet<String> lifelineNamesOfOverCreated = new HashSet<>();

    public InteractionModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getLifelineCount() {
        return id2Lifeline.size();
    }

    public UmlLifeline getLifelineCreator(String lifelineName) throws
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        if (!name2Lifeline.containsKey(lifelineName)) {
            throw new LifelineNotFoundException(name, lifelineName);
        } else if (dupNameLifeline.contains(lifelineName)) {
            throw new LifelineDuplicatedException(name, lifelineName);
        }

        String lid = name2Lifeline.get(lifelineName).getId();
        if (!lid4CreateMessage.containsKey(lid)) {
            throw new LifelineNeverCreatedException(name, lifelineName);
        } else if (lifelineNamesOfOverCreated.contains(lifelineName)) {
            throw new LifelineCreatedRepeatedlyException(name, lifelineName);
        }
        return id2Lifeline.get(lid4CreateMessage.get(lid).getSource());
    }

    public Pair<Integer, Integer> getLifelineLostAndFound(String lifelineName) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2Lifeline.containsKey(lifelineName)) {
            throw new LifelineNotFoundException(name, lifelineName);
        } else if (dupNameLifeline.contains(lifelineName)) {
            throw new LifelineDuplicatedException(name, lifelineName);
        }

        String lid = name2Lifeline.get(lifelineName).getId();
        return new Pair<>(id2FoundCount.get(lid), id2LostCount.get(lid));
    }

    public void addLifeline(UmlLifeline umlLifeline) {
        String lid = umlLifeline.getId();
        String name = umlLifeline.getName();

        id2Lifeline.put(lid, umlLifeline);
        if (name2Lifeline.containsKey(name)) {
            dupNameLifeline.add(name);
        } else {
            name2Lifeline.put(name, umlLifeline);
        }

        id2FoundCount.put(lid, 0);
        id2LostCount.put(lid, 0);
    }

    public void addEndPoint(UmlEndpoint umlEndpoint) {
        String id = umlEndpoint.getId();
        id2Endpoint.put(id, umlEndpoint);
        // in case when there's message "endpoint --> endpoint"
        id2FoundCount.put(id, 0);
        id2LostCount.put(id, 0);
    }

    public void addMessage(UmlMessage umlMessage) {
        String srcId = umlMessage.getSource();
        String tarId = umlMessage.getTarget();
        if (umlMessage.getMessageSort() == MessageSort.CREATE_MESSAGE) {
            if (lid4CreateMessage.containsKey(tarId)) {
                lifelineNamesOfOverCreated.add(
                        id2Lifeline.get(tarId).getName()
                );
            } else {
                lid4CreateMessage.put(tarId, umlMessage);
            }
        }
        if (id2Endpoint.containsKey(srcId)) { // FoundMessage: endpoint --> target
            id2FoundCount.put(tarId, id2FoundCount.get(tarId) + 1);
        }
        if (id2Endpoint.containsKey(tarId)) { // LostMessage: source --> endpoint
            id2LostCount.put(srcId, id2LostCount.get(srcId) + 1);
        }
    }
}
