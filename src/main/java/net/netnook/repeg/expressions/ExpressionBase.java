package net.netnook.repeg.expressions;

import net.netnook.repeg.Context;
import net.netnook.repeg.Expression;
import net.netnook.repeg.ExpressionBuilder;
import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.ParseListener;

public abstract class ExpressionBase implements Expression, ExpressionBuilder {

	private final OnSuccessHandler onSuccess;

	protected ExpressionBase(OnSuccessHandler onSuccess) {
		this.onSuccess = onSuccess;
	}

	@Override
	public Expression build() {
		return this;
	}

	protected Expression[] build(ExpressionBuilder[] builders) {
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
		ParseListener listener = context.getListener();

		Context handlerContext = context.slice(startPosition, startStackIdx);

		listener.onExpressionEnter(this, handlerContext);

		boolean success = doParse(context, startPosition, startStackIdx);

		handlerContext = context.slice(startPosition, startStackIdx);

		if (success && onSuccess != null) {
			onSuccess.accept(handlerContext);
		}

		listener.onExpressionExit(this, handlerContext, success);

		return success;
	}

	protected abstract boolean doParse(RootContext context, int startPosition, int startStackIdx);

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + buildGrammar() + "]";
	}
}
