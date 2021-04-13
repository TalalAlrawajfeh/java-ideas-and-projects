package workflow;

import contracts.State;
import contracts.TransitionMapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WorkflowBuilder {
    private Workflow workflow = new Workflow();
    private Set<State> terminalStates = new HashSet<>();
    private Set<TransitionMapping> transitionMappings = new HashSet<>();

    public WorkflowBuilder() {
        /* default constructor */
    }

    public WorkflowBuilder setInitialState(WorkflowState initialState) {
        workflow.setInitialState(initialState);
        return this;
    }

    public WorkflowBuilder addTerminalStates(Collection<WorkflowState> terminalStates) {
        this.terminalStates.addAll(terminalStates);
        return this;
    }

    public WorkflowBuilder addTerminalState(WorkflowState terminalState) {
        terminalStates.add(terminalState);
        return this;
    }

    public WorkflowBuilder addTransitionMappings(Collection<TransitionMapping> transitionMappings) {
        this.transitionMappings.addAll(transitionMappings);
        return this;
    }

    public WorkflowBuilder addTransitionMapping(TransitionMapping transitionMapping) {
        transitionMappings.add(transitionMapping);
        return this;
    }

    public Workflow build() {
        workflow.setTerminalStates(terminalStates);
        workflow.setTransitionMappings(transitionMappings);
        return workflow;
    }
}
