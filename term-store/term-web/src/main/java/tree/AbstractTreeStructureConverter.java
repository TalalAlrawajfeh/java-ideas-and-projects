package tree;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public abstract class AbstractTreeStructureConverter<R, T> implements TreeConverter<R, T> {
	@Override
	public R convert(List<T> ts) {
		return getFinalStep().apply(ts.stream().map(baseConversionStrategy(recursiveConversionStrategy())::apply)
				.reduce(getReducerIdentity(), getReducerAccumulator()));
	}

	protected abstract ConversionStrategy<R, T> baseConversionStrategy(
			ConversionStrategy<R, T> recursiveConversionStrategy);

	protected abstract ConversionStrategy<R, T> recursiveConversionStrategy();

	protected abstract R getReducerIdentity();

	protected abstract BinaryOperator<R> getReducerAccumulator();

	protected abstract UnaryOperator<R> getFinalStep();
}
