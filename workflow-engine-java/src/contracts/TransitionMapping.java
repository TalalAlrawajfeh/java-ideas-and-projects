package contracts;

import commons.Pair;

public class TransitionMapping {
    private Pair<State, StateModifier> priorStateToModifier;
    private State consequentState;

    public TransitionMapping() {
    }

    public TransitionMapping(Pair<State, StateModifier> priorStateToModifier, State consequentState) {
        this.priorStateToModifier = priorStateToModifier;
        this.consequentState = consequentState;
    }

    public Pair<State, StateModifier> getPriorStateToModifier() {
        return priorStateToModifier;
    }

    public void setPriorStateToModifier(Pair<State, StateModifier> priorStateToModifier) {
        this.priorStateToModifier = priorStateToModifier;
    }

    public State getConsequentState() {
        return consequentState;
    }

    public void setConsequentState(State consequentState) {
        this.consequentState = consequentState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransitionMapping that = (TransitionMapping) o;

        if (priorStateToModifier != null ? !priorStateToModifier.equals(that.priorStateToModifier) : that.priorStateToModifier != null)
            return false;
        return consequentState != null ? consequentState.equals(that.consequentState) : that.consequentState == null;
    }

    @Override
    public int hashCode() {
        int result = priorStateToModifier != null ? priorStateToModifier.hashCode() : 0;
        result = 31 * result + (consequentState != null ? consequentState.hashCode() : 0);
        return result;
    }
}
