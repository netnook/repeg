package net.netnook.qpeg.impl;

public abstract class ParsingExpressionBase implements ParsingExpression {

	protected final boolean ignore;
	protected final String alias;
	protected final OnSuccessHandler onSuccess;

	protected ParsingExpressionBase(boolean ignore, String alias, OnSuccessHandler onSuccess) {
		this.ignore = ignore;
		this.alias = alias;
		this.onSuccess = onSuccess;
	}

	public final boolean isIgnore() {
		return ignore;
	}

	@Override
	public String alias() {
		return alias;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + buildGrammar() + "]";
	}
}
