package workflow;

import commons.Pair;
import contracts.State;
import contracts.StateModifier;
import contracts.TransitionMapping;

public class WorkflowTransitionMappingBuilder {
    private State priorState;
    private StateModifier stateModifier;
    private State consequentState;

    public WorkflowTransitionMappingBuilder() {
        /* default constructor */
    }

    public WorkflowTransitionMappingBuilder setPriorState(WorkflowState priorState) {
        this.priorState = priorState;
        return this;
    }

    public WorkflowTransitionMappingBuilder setStateModifier(StateModifier stateModifier) {
        this.stateModifier = stateModifier;
        return this;
    }

    public WorkflowTransitionMappingBuilder setConsequentState(WorkflowState consequentState) {
        this.consequentState = consequentState;
        return this;
    }

    public TransitionMapping build() {
        return new TransitionMapping(new Pair<>(priorState, stateModifier), consequentState);
    }
}
