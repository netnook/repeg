package net.netnook.qpeg.expressions;

import net.netnook.qpeg.expressions.Context.Marker;

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

	protected final void onSuccess(Context context, Marker startMarker) {
		context.mark(startMarker);
		if (ignore) {
			context.clear();
		} else {
			onSuccess.accept(context);
		}
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
