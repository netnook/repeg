package net.netnook.qpeg.expressions;

public abstract class ParsingExpressionBuilderBase implements ParsingExpressionBuilder {

	private OnSuccessHandler onSuccess;
	private ParsingExpression built;

	public ParsingExpressionBuilderBase ignore() {
		this.onSuccess = OnSuccessHandler.CLEAR_STACK;
		return this;
	}

	@Override
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

	protected static ParsingExpression[] build(ParsingExpressionBuilder[] builders) {
		ParsingExpression[] results = new ParsingExpression[builders.length];
		for (int i = 0; i < builders.length; i++) {
			results[i] = builders[i].build();
		}
		return results;
	}

	protected void validate(boolean valid, String message) {
		if (!valid) {
			throw new InvalidExpressionException(message);
		}
	}
}
