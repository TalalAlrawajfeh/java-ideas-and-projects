package contracts;

import java.util.Collection;

public interface FiniteStateMachine {
    void setInitialState(State initialState);

    void setTerminalStates(Collection<State> terminalStates);

    void setTransitionMappings(Collection<TransitionMapping> transitionMappings);

    void doTransition(StateModifier stateModifier);

    State getCurrentState();

    boolean isCurrentStateTerminal();
}
