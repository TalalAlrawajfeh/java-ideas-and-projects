import contracts.StateModifier;
import workflow.Workflow;
import workflow.WorkflowBuilder;
import workflow.WorkflowState;
import workflow.WorkflowTransitionMappingBuilder;


public class Main {
    private static final WorkflowState CHEQUE_TECHNICAL_CLEARING = new WorkflowState(1L, sm -> System.out.println("Cheque is now in technical clearing"));
    private static final WorkflowState CHEQUE_WAITING_REPLY = new WorkflowState(2L, sm -> System.out.println("Cheque is waiting reply"));
    private static final WorkflowState BANK_REPLIED = new WorkflowState(3L, sm -> System.out.println("Bank replied"));
    private static final WorkflowState FAILED = new WorkflowState(4L, sm -> System.out.println("failed"));

    private static final StateModifier INITIAL_MODIFIER = new StateModifier(0L);
    private static final StateModifier CHEQUE_REJECTED = new StateModifier(1L);
    private static final StateModifier CBS_SUCCESS = new StateModifier(2L);
    private static final StateModifier SYSTEM_ERROR = new StateModifier(3L);

    public static void main(String[] args) {
        Workflow workflow = new WorkflowBuilder().setInitialState(CHEQUE_TECHNICAL_CLEARING)
                .addTerminalState(BANK_REPLIED)
                .addTerminalState(FAILED)
                .addTransitionMapping(new WorkflowTransitionMappingBuilder()
                        .setPriorState(CHEQUE_TECHNICAL_CLEARING)
                        .setStateModifier(CHEQUE_REJECTED)
                        .setConsequentState(CHEQUE_WAITING_REPLY)
                        .build())
                .addTransitionMapping(new WorkflowTransitionMappingBuilder()
                        .setPriorState(CHEQUE_WAITING_REPLY)
                        .setStateModifier(CBS_SUCCESS)
                        .setConsequentState(BANK_REPLIED)
                        .build())
                .addTransitionMapping(new WorkflowTransitionMappingBuilder()
                        .setPriorState(CHEQUE_WAITING_REPLY)
                        .setStateModifier(SYSTEM_ERROR)
                        .setConsequentState(FAILED)
                        .build())
                .build();

        workflow.doTransition(INITIAL_MODIFIER);
        workflow.doTransition(CHEQUE_REJECTED);
        workflow.doTransition(CBS_SUCCESS);
    }
}
