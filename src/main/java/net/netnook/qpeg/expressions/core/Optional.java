package net.netnook.qpeg.expressions.core;

import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.expressions.CompoundExpression;
import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.RootContext;

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
		protected Optional doBuild() {
			return new Optional(this);
		}
	}

	private final ParsingExpression expression;

	private Optional(Builder builder) {
		super(builder.getOnSuccess());
		this.expression = builder.expression.build();
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
