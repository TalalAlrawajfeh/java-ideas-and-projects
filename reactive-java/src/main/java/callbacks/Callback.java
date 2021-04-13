package callbacks;


/**
 * Created by u624 on 4/20/17.
 */
@FunctionalInterface
public interface Callback<T> {
    void process(T t);
}
