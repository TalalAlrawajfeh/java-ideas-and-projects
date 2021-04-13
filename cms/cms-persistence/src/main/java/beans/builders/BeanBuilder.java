package beans.builders;

@FunctionalInterface
public interface BeanBuilder<T> {
	T build();
}
