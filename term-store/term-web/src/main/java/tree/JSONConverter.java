package tree;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import persistence.beans.Term;

public class JSONConverter extends AbstractTreeStructureConverter<String, Term> {
	private String servletPath;
	TermHREFBuilder termHREFBuilder = new TermHREFBuilder();

	public JSONConverter(String servletPath) {
		this.servletPath = servletPath;
	}

	@Override
	protected ConversionStrategy<String, Term> baseConversionStrategy(
			ConversionStrategy<String, Term> recursiveConversionStrategy) {
		return t -> "{\"text\":\"" + t.getName() + "\", \"href\":\"" + termHREFBuilder.href(servletPath, t) + "\""
				+ recursiveConversionStrategy.apply(t) + "}";
	}

	@Override
	protected ConversionStrategy<String, Term> recursiveConversionStrategy() {
		return t -> {
			String nodes = "";
			if (Objects.nonNull(t.getChildren())) {
				nodes = ", \"nodes\":" + convert(t.getChildren());
			}
			return nodes;
		};
	}

	@Override
	protected String getReducerIdentity() {
		return "[";
	}

	@Override
	protected BinaryOperator<String> getReducerAccumulator() {
		return (s1, s2) -> {
			if (getReducerIdentity().equals(s1))
				return s1 + s2;
			else
				return s1 + "," + s2;
		};
	}

	@Override
	protected UnaryOperator<String> getFinalStep() {
		return s -> s + "]";
	}

}
