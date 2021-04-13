package contracts;

public class StateModifier {
    private Long eventId;

    public StateModifier(Long eventId) {
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateModifier stateModifier = (StateModifier) o;

        return eventId != null ? eventId.equals(stateModifier.eventId) : stateModifier.eventId == null;
    }

    @Override
    public int hashCode() {
        return eventId != null ? eventId.hashCode() : 0;
    }
}
