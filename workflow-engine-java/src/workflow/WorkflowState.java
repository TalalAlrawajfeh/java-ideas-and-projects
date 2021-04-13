package workflow;

import contracts.State;
import contracts.StateModifier;

import java.util.function.Consumer;

public class WorkflowState implements State {
    public static final State VOID_STATE = new WorkflowState(-1L, null);

    private Long id;
    private Consumer<StateModifier> stateModifierConsumer;

    public Long getId() {
        return id;
    }

    public WorkflowState(Long id, Consumer<StateModifier> stateModifierConsumer) {
        this.id = id;
        this.stateModifierConsumer = stateModifierConsumer;
    }

    @Override
    public void onAdvent(StateModifier stateModifier) {
        if (stateModifierConsumer != null) {
            stateModifierConsumer.accept(stateModifier);
        }
    }
}
