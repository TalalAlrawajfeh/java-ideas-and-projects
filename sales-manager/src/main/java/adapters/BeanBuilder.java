package adapters;

/**
 * Created by u624 on 3/22/17.
 */
@FunctionalInterface
public interface BeanBuilder<T> {
    T build();
}
