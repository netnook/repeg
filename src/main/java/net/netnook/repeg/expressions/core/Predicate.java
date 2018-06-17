package net.netnook.repeg.expressions.core;

import java.util.Collections;
import java.util.List;

import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.ParsingExpressionBuilder;
import net.netnook.repeg.expressions.CompoundExpression;
import net.netnook.repeg.expressions.Expression;
import net.netnook.repeg.expressions.ExpressionBase;
import net.netnook.repeg.expressions.ExpressionBuilderBase;
import net.netnook.repeg.expressions.RootContext;

/**
 * Predicate expression e.g. '{@code &(a)}' (match), '{@code !(a)}' (no match).
 * <p>
 * This expression tests the current input for a match/no-match of a sub-expression
 * itself returning a match if the sub-expression matches (in the case of a &amp;/match predicate)
 * or if the sub-expression does not match (in the case of a !/no-match predicate).
 * <p>
 * This expression never consumes any input and always resets the stack to it's initial position
 * irrespective of the matching result of the sub-expression.
 * <p>
 * This expression has no default {@link OnSuccessHandler}.
 */
public final class Predicate extends ExpressionBase implements CompoundExpression {

	/**
	 * Create a new {@link Predicate} expression which matches when the specified sub-expression matches.
	 *
	 * @param expression the sub-expression to match.
	 * @return the new {@link Predicate} expression.
	 */
	public static Builder match(ParsingExpressionBuilder expression) {
		return new Builder().expression(expression, false);
	}

	/**
	 * Create a new {@link Predicate} expression which matches when the specified sub-expression does not match.
	 *
	 * @param expression the sub-expression to match.
	 * @return the new {@link Predicate} expression.
	 */
	public static Builder not(ParsingExpressionBuilder expression) {
		return new Builder().expression(expression, true);
	}

	public static class Builder extends ExpressionBuilderBase {
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

	protected final Expression expression;
	protected final boolean invert;

	private Predicate(Builder builder) {
		super(builder.getOnSuccess());
		this.expression = builder.expression.build();
		this.invert = builder.invert;
	}

	@Override
	public List<Expression> parts() {
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
