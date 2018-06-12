package net.netnook.repeg.expressions;

public abstract class ParsingExpressionBase implements ParsingExpression, ParsingExpressionBuilder {

	private final OnSuccessHandler onSuccess;

	protected ParsingExpressionBase(OnSuccessHandler onSuccess) {
		this.onSuccess = (onSuccess == null) ? OnSuccessHandler.NO_OP : onSuccess;
	}

	@Override
	public ParsingExpression build() {
		return this;
	}

	public OnSuccessHandler getOnSuccess() {
		return onSuccess;
	}

	@Override
	public final boolean parse(RootContext context) {
		int startPosition = context.position();
		int startStackIdx = context.stackSize();

		onExpressionEnter(context);

		boolean success = parseImpl(context, startPosition, startStackIdx);

		onExpressionExit(context, startPosition, startStackIdx, success);

		return success;
	}

	protected abstract boolean parseImpl(RootContext context, int startPosition, int startStackIdx);

	private void onExpressionEnter(RootContext context) {
		context.getListener().onExpressionEnter(this, context);
	}

	private void onExpressionExit(RootContext context, int startPosition, int startStackIdx, boolean success) {
		if (success) {
			onSuccess.accept(context.slice(startPosition, startStackIdx));
		}

		context.getListener().onExpressionExit(this, context, startPosition, startStackIdx, success);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + buildGrammar() + "]";
	}
}
