package self.model;

import com.oocourse.uml2.models.elements.UmlEvent;

import java.util.ArrayList;

public class TransitionModel {
    private final String id;
    private final String srcId;
    private final String tarId;
    private final ArrayList<String> eventNames = new ArrayList<>();

    public TransitionModel(String id, String srcId, String dstId) {
        this.id = id;
        this.srcId = srcId;
        this.tarId = dstId;
    }

    public void addEvent(UmlEvent umlEvent) {
        this.eventNames.add(umlEvent.getName());
    }

    public String getId() {
        return id;
    }

    public String getSrcId() {
        return srcId;
    }

    public String getTarId() {
        return tarId;
    }

    public ArrayList<String> getEventNames() {
        return eventNames;
    }
}
