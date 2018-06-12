package net.netnook.repeg.expressions.core;

import java.util.Collections;
import java.util.List;

import net.netnook.repeg.expressions.CompoundExpression;
import net.netnook.repeg.expressions.OnSuccessHandler;
import net.netnook.repeg.expressions.ParsingExpression;
import net.netnook.repeg.expressions.ParsingExpressionBuilder;
import net.netnook.repeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.repeg.expressions.RootContext;

/**
 * Optional expression i.e. '{@code (a)?}'.
 * <p>
 * This expression handles an optional sub-expression, attempting to match the sub-expression
 * but always returning a positive match.  A {@link Optional} expression's {@link OnSuccessHandler} will
 * always be called.
 * <p>
 * This expression has no default {@link net.netnook.repeg.expressions.OnSuccessHandler}.
 */
public final class Optional extends CompoundExpression {

	/**
	 * Create a new {@link Optional} expression for the specified sub-expression.
	 *
	 * @param expression the sub-expression to match.
	 * @return the new {@link Optional} expression.
	 */
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
