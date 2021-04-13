package adapters;

/**
 * Created by u624 on 3/24/17.
 */
@FunctionalInterface
public interface Convertable<T> {
    T convert();
}
