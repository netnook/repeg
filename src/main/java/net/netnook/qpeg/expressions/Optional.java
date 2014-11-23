package net.netnook.qpeg.expressions;

import java.util.Collections;
import java.util.List;

public final class Optional extends CompoundExpression {

	public static Builder of(ParsingExpressionBuilder expression) {
		return new Builder().expression(expression);
	}

	public static class Builder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;

		public Builder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		@Override
		public Builder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		@Override
		public Builder ignore() {
			super.ignore();
			return this;
		}

		@Override
		protected Optional doBuild(BuildContext ctxt) {
			return new Optional(this, expression.build(ctxt));
		}
	}

	private final ParsingExpression expression;

	private Optional(Builder builder, ParsingExpression expression) {
		super(builder);
		this.expression = expression;
	}

	@Override
	public List<ParsingExpression> parts() {
		return Collections.singletonList(expression);
	}

	@Override
	public String buildGrammar() {
		return "(" + expression.buildGrammar() + ")?";
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		boolean success = expression.parse(context);
		if (!success) {
			context.resetTo(startPosition, startStackIdx);
		}
		return true;
	}
}
