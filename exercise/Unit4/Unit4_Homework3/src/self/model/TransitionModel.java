package self.model;

import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;

public class TransitionModel {
    private final String id;
    private final String srcId;
    private final String tarId;
    private final String guard;
    private final ArrayList<UmlEvent> events = new ArrayList<>();

    public TransitionModel(UmlTransition umlTransition) {
        this.id = umlTransition.getId();
        this.srcId = umlTransition.getSource();
        this.tarId = umlTransition.getTarget();
        this.guard = umlTransition.getGuard();
    }

    public void addEvent(UmlEvent umlEvent) {
        this.events.add(umlEvent);
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

    public String getGuard() {
        return guard;
    }

    public ArrayList<UmlEvent> getEvents() {
        return events;
    }

    public ArrayList<String> getEventNames() {
        ArrayList<String> eventNames = new ArrayList<>();
        for (UmlEvent umlEvent: events) {
            eventNames.add(umlEvent.getName());
        }
        return eventNames;
    }

    public boolean guardOverlaps(TransitionModel other) {
        if (guard == null || other.getGuard() == null) {
            return true;
        }
        return guard.equals(other.getGuard());
    }
}
