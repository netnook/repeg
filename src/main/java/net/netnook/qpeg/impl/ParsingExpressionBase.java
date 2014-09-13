package net.netnook.qpeg.impl;

public abstract class ParsingExpressionBase implements ParsingExpression {

	protected final boolean ignore;
	protected final String alias;

	protected ParsingExpressionBase(boolean ignore, String alias) {
		this.ignore = ignore;
		this.alias = alias;
	}

	public final boolean isIgnore() {
		return ignore;
	}

	@Override
	public String alias() {
		return alias;
	}
}
