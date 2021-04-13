package adapters;

import exceptions.UseCaseException;

/**
 * Created by u624 on 3/24/17.
 */
@FunctionalInterface
public interface UseCase<T> {
    void execute(T t) throws UseCaseException;
}
