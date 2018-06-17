package net.netnook.repeg.expressions;

import net.netnook.repeg.Expression;
import net.netnook.repeg.ExpressionBuilder;
import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.exceptions.InvalidExpressionException;

public abstract class ExpressionBuilderBase implements ExpressionBuilder {

	private OnSuccessHandler onSuccess;
	private Expression built;

	public OnSuccessHandler getOnSuccess() {
		return onSuccess;
	}

	public ExpressionBuilderBase onSuccess(OnSuccessHandler onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	@Override
	public final Expression build() {
		if (built == null) {
			built = doBuild();
		}
		return built;
	}

	protected abstract Expression doBuild();

	protected void validate(boolean valid, String message) {
		if (!valid) {
			throw new InvalidExpressionException(message);
		}
	}
}
