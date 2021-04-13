package workflow;

import commons.Pair;
import contracts.*;

import java.util.Collection;

import static workflow.WorkflowState.VOID_STATE;

public class Workflow implements FiniteStateMachine {
    private State initialState;
    private Collection<State> terminalStates;
    private Collection<TransitionMapping> transitionMappings;

    private State currentState = VOID_STATE;

    public Workflow() {
        /* default constructor */
    }

    @Override
    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    @Override
    public void setTerminalStates(Collection<State> terminalStates) {
        this.terminalStates = terminalStates;
    }

    @Override
    public void setTransitionMappings(Collection<TransitionMapping> transitionMappings) {
        this.transitionMappings = transitionMappings;
    }

    @Override
    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void doTransition(StateModifier stateModifier) {
        if (VOID_STATE.equals(currentState)) {
            currentState = initialState;
        } else {
            final Pair<State, StateModifier> stateEventPair = new Pair<>(currentState, stateModifier);
            currentState = transitionMappings.stream()
                    .filter(transitionMapping ->
                            transitionMapping.getPriorStateToModifier().equals(stateEventPair))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No state found for this transition"))
                    .getConsequentState();
        }
        currentState.onAdvent(stateModifier);
    }

    @Override
    public boolean isCurrentStateTerminal() {
        return terminalStates.stream().anyMatch(s -> s.equals(currentState));
    }
}
