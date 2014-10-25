package net.netnook.qpeg.expressions;

public abstract class ParsingExpressionBuilderBase implements ParsingExpressionBuilder {

	private boolean ignore;
	private OnSuccessHandler onSuccess;

	@Override
	public boolean isIgnore() {
		return ignore;
	}

	public ParsingExpressionBuilderBase ignore() {
		this.ignore = true;
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

	protected static ParsingExpression[] build(BuildContext ctxt, ParsingExpressionBuilder[] builders) {
		ParsingExpression[] results = new ParsingExpression[builders.length];
		for (int i = 0; i < builders.length; i++) {
			results[i] = builders[i].build(ctxt);
		}
		return results;
	}
}
