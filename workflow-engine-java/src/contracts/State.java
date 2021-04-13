package contracts;

@FunctionalInterface
public interface State {
    void onAdvent(StateModifier stateModifier);
}
