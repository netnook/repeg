package net.netnook.qpeg.expressions.core;

import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.expressions.CompoundExpression;
import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.RootContext;

public final class Predicate extends CompoundExpression {

	public static Builder match(ParsingExpressionBuilder expression) {
		return new Builder().expression(expression, false);
	}

	public static Builder not(ParsingExpressionBuilder expression) {
		return new Builder().expression(expression, true);
	}

	public static class Builder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;
		private boolean invert;

		public Builder expression(ParsingExpressionBuilder expression, boolean invert) {
			super.onSuccess(OnSuccessHandler.CLEAR_STACK);
			this.expression = expression;
			this.invert = invert;
			return this;
		}

		@Override
		public Builder onSuccess(OnSuccessHandler onSuccess) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected Predicate doBuild() {
			return new Predicate(this);
		}
	}

	protected final ParsingExpression expression;
	protected final boolean invert;

	private Predicate(Builder builder) {
		super(builder.getOnSuccess());
		this.expression = builder.expression.build();
		this.invert = builder.invert;
	}

	@Override
	public List<ParsingExpression> parts() {
		return Collections.singletonList(expression);
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		boolean match = expression.parse(context);

		boolean success = invert ^ match;

		context.resetTo(startPosition, startStackIdx);

		return success;
	}

	@Override
	public String buildGrammar() {
		return invert  //
				? "!(" + expression.buildGrammar() + ")" //
				: "&(" + expression.buildGrammar() + ")";
	}
}
