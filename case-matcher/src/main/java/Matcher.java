import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class Matcher<T, J> {
    private Map<Predicate<T>, Function<T, J>> cases;

    private Matcher(Map<Predicate<T>, Function<T, J>> cases) {
        this.cases = cases;
    }

    public static <T, J> MatcherBuilder<T, J> builder() {
        return new MatcherBuilder<>();
    }

    public J match(T t) {
        return cases.entrySet()
                .stream()
                .filter(e -> e.getKey().test(t))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("A condition is not handled: " + t))
                .getValue()
                .apply(t);
    }

    public static class MatcherBuilder<T, J> {
        private Map<Predicate<T>, Function<T, J>> cases = new LinkedHashMap<>();

        public MatcherBuilder<T, J> addCase(Predicate<T> condition, Function<T, J> resultFunction) {
            cases.put(condition, resultFunction);
            return this;
        }

        public MustBuild<T, J> defaultCase(Function<T, J> resultFunction) {
            cases.put(t -> true, resultFunction);
            return this::build;
        }

        public Matcher<T, J> build() {
            return new Matcher<>(cases);
        }

        public interface MustBuild<T, J> {
            Matcher<T, J> build();
        }
    }
}
