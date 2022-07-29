package self.model;

import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;

import java.util.ArrayList;
import java.util.HashSet;

public class CollaborationModel {
    private final String id;
    private final HashSet<String> attributeIdSet = new HashSet<>();
    private final ArrayList<InteractionModel> interactions = new ArrayList<>();

    public CollaborationModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addAttributeId(String aid) {
        attributeIdSet.add(aid);
    }

    public void addInteraction(InteractionModel interactionModel) {
        interactions.add(interactionModel);
    }

    public void checkAttributeRepresent() throws UmlRule006Exception {
        for (InteractionModel myInteract: interactions) {
            myInteract.checkLifelineRepresent(attributeIdSet);
        }
    }
}
