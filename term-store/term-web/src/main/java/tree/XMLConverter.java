package tree;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import persistence.beans.Term;

public class XMLConverter extends AbstractTreeStructureConverter<String, Term> {
	@Override
	protected ConversionStrategy<String, Term> baseConversionStrategy(
			ConversionStrategy<String, Term> recursiveConversionStrategy) {
		return t -> {
			return "<term><name>" + t.getName() + "</name><label>" + t.getLabel() + "</label><purpose>" + t.getPurpose()
					+ "</purpose><category>" + t.getCategory().getName() + "</category>"
					+ recursiveConversionStrategy.apply(t) + "</term>";
		};
	}

	@Override
	protected ConversionStrategy<String, Term> recursiveConversionStrategy() {
		return t -> {
			String children = "";
			if (Objects.nonNull(t.getChildren())) {
				children += "<children>" + convert(t.getChildren()) + "</children>";
			}
			return children;
		};
	}

	@Override
	protected String getReducerIdentity() {
		return "<terms>";
	}

	@Override
	protected BinaryOperator<String> getReducerAccumulator() {
		return String::concat;
	}

	@Override
	protected UnaryOperator<String> getFinalStep() {
		return s -> s + "</terms>";
	}

}
