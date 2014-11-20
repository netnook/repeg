package net.netnook.qpeg.expressions;

import net.netnook.qpeg.expressions.Context.Marker;

public abstract class ParsingExpressionBase implements ParsingExpression {

	private final OnSuccessHandler onSuccess;

	protected ParsingExpressionBase(ParsingExpressionBuilder builder) {
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
			onSuccess.accept(context);
		}

		context.getListener().onExpressionExit(this, context, success);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + buildGrammar() + "]";
	}
}
