package net.netnook.repeg.expressions;

public abstract class ParsingExpressionBuilderBase implements ParsingExpressionBuilder {

	private OnSuccessHandler onSuccess;
	private ParsingExpression built;

	public OnSuccessHandler getOnSuccess() {
		return onSuccess;
	}

	public ParsingExpressionBuilderBase onSuccess(OnSuccessHandler onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	@Override
	public final ParsingExpression build() {
		if (built == null) {
			built = doBuild();
		}
		return built;
	}

	protected abstract ParsingExpression doBuild();

	protected void validate(boolean valid, String message) {
		if (!valid) {
			throw new InvalidExpressionException(message);
		}
	}
}