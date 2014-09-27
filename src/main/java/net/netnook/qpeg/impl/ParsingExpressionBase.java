package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;

public abstract class ParsingExpressionBase implements ParsingExpression {

	protected final boolean ignore;
	protected final OnSuccessHandler onSuccess;

	protected ParsingExpressionBase(ParsingExpressionBuilderBase builder) {
		if (builder.isIgnore() && builder.getOnSuccess() != null) {
			throw new IllegalArgumentException("Cannot have ignore=true and an onSuccess handler at same time");
		}

		this.ignore = builder.isIgnore();
		this.onSuccess = (builder.getOnSuccess() == null) ? OnSuccessHandler.NO_OP : builder.getOnSuccess();
	}

	@Override
	public final boolean isIgnore() {
		return ignore;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + buildGrammar() + "]";
	}
}
