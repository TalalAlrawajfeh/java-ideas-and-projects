package tree;

@FunctionalInterface
public interface ConversionStrategy<R, T> {
	R apply(T t);
}
