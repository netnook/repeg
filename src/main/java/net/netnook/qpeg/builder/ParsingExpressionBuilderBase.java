package net.netnook.qpeg.builder;

import net.netnook.qpeg.impl.OnSuccessHandler;
import net.netnook.qpeg.impl.ParsingExpression;

public abstract class ParsingExpressionBuilderBase implements ParsingExpressionBuilder {

	private boolean ignore;
	private OnSuccessHandler onSuccess;

	public boolean isIgnore() {
		return ignore;
	}

	public ParsingExpressionBuilderBase ignore() {
		this.ignore = true;
		return this;
	}

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
