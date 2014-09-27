package net.netnook.qpeg.builder;

import net.netnook.qpeg.impl.ParsingExpression;

public abstract class ParsingExpressionBuilderBase implements ParsingExpressionBuilder {

	private String name;
	private String alias;
	private boolean ignore;

	public String name() {
		return name;
	}

	public ParsingExpressionBuilderBase name(String name) {
		this.name = name;
		return this;
	}

	public String alias() {
		return alias;
	}

	public ParsingExpressionBuilderBase alias(String alias) {
		this.alias = alias;
		return this;
	}

	@Deprecated
	public boolean ignore() {
		return ignore;
	}

	@Deprecated
	public ParsingExpressionBuilderBase ignore(boolean ignore) {
		this.ignore = ignore;
		return this;
	}

	protected static ParsingExpression[] build(BuildContext ctxt, ParsingExpressionBuilder[] builders) {
		ParsingExpression[] results = new ParsingExpression[builders.length];
		for (int i = 0; i < builders.length; i++) {
			results[i] = builders[i].build(ctxt);
		}
		return results;
	}
}
