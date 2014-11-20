package net.netnook.qpeg.expressions;

import net.netnook.qpeg.expressions.Context.Marker;

public abstract class ParsingExpressionBase implements ParsingExpression {

	protected final boolean ignore;
	protected final OnSuccessHandler onSuccess;

	protected ParsingExpressionBase(ParsingExpressionBuilder builder) {
		if (builder.isIgnore() && builder.getOnSuccess() != null) {
			throw new InvalidExpressionException("Cannot have ignore=true and an onSuccess handler at same time");
		}

		this.ignore = builder.isIgnore();
		this.onSuccess = (builder.getOnSuccess() == null) ? OnSuccessHandler.NO_OP : builder.getOnSuccess();
	}

	@Override
	public final boolean parse(Context context) {
		Marker startMarker = context.updateMark();

		onExpressionEnter(context);

		boolean success = parseImpl(context, startMarker);

		onExpressionExit(context, startMarker, success);

		return success;
	}

	protected abstract boolean parseImpl(Context context, Marker startMarker);

	protected void onExpressionEnter(Context context) {
		context.getListener().onExpressionEnter(this, context);
	}

	protected void onExpressionExit(Context context, Marker startMarker, boolean success) {
		context.mark(startMarker);

		if (success) {
			if (ignore) {
				context.clear();
			} else {
				onSuccess.accept(context);
			}
		}

		context.getListener().onExpressionExit(this, context, success);
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
