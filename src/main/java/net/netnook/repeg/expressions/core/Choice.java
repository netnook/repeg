package net.netnook.repeg.expressions.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.netnook.repeg.Expression;
import net.netnook.repeg.ExpressionBuilder;
import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.expressions.CompoundExpression;
import net.netnook.repeg.expressions.ExpressionBase;
import net.netnook.repeg.expressions.ExpressionBuilderBase;
import net.netnook.repeg.expressions.RootContext;

/**
 * Ordered choice expression i.e. '{@code (a | b | c)}'.
 * <p>
 * This expression handles an ordered choice of sub-expressions, attempting to match each one in turn
 * and returning a match when the first sub-expression matches.  If none of the sub-expressions match, this
 * choice expression returns no-match.
 * <p>
 * This expression has no default {@link OnSuccessHandler}.
 */
public final class Choice extends ExpressionBase implements CompoundExpression {

	/**
	 * Create a new {@link Choice} expression for the specified sub-expressions.
	 * <p>
	 * Each sub-expression is tested in turn and a Choice matches if one of these sub-expressions match.
	 *
	 * @param expressions the sub-expressions to match in order.
	 * @return the new {@link Choice} expression.
	 */
	public static Builder of(ExpressionBuilder... expressions) {
		return new Builder().expressions(expressions);
	}

	public static class Builder extends ExpressionBuilderBase {
		private ExpressionBuilder[] expressions;

		public Builder expressions(ExpressionBuilder[] expressions) {
			this.expressions = expressions;
			return this;
		}

		@Override
		public Builder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		@Override
		protected Choice doBuild() {
			return new Choice(this);
		}
	}

	private final Expression[] expressions;

	private Choice(Builder builder) {
		super(builder.getOnSuccess());
		this.expressions = build(builder.expressions);
	}

	@Override
	public List<Expression> parts() {
		return Arrays.asList(expressions);
	}

	@Override
	public String buildGrammar() {
		return Stream.of(expressions) //
				.map(Expression::buildGrammar) //
				.collect(Collectors.joining(" | ", "(", ")"));
	}

	@Override
	protected boolean doParse(RootContext context, int startPosition, int startStackIdx) {
		for (Expression expression : expressions) {
			boolean success = expression.parse(context);
			if (success) {
				return true;
			}
			context.resetTo(startPosition, startStackIdx);
		}
		return false;
	}
}
