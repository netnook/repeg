package net.netnook.repeg.expressions;

import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.ParsingExpressionBuilder;

public abstract class ExpressionBase implements Expression, ParsingExpressionBuilder {

	private final OnSuccessHandler onSuccess;

	protected ExpressionBase(OnSuccessHandler onSuccess) {
		this.onSuccess = (onSuccess == null) ? OnSuccessHandler.NO_OP : onSuccess;
	}

	@Override
	public Expression build() {
		return this;
	}

	protected Expression[] build(ParsingExpressionBuilder[] builders) {
		Expression[] results = new Expression[builders.length];
		for (int i = 0; i < builders.length; i++) {
			results[i] = builders[i].build();
		}
		return results;
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
