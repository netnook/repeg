package net.netnook.repeg.expressions.core;

import java.util.Collections;
import java.util.List;

import net.netnook.repeg.expressions.CompoundExpression;
import net.netnook.repeg.expressions.OnSuccessHandler;
import net.netnook.repeg.expressions.ParsingExpression;
import net.netnook.repeg.expressions.ParsingExpressionBase;
import net.netnook.repeg.expressions.ParsingExpressionBuilder;
import net.netnook.repeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.repeg.expressions.RootContext;

/**
 * Predicate expression e.g. '{@code &(a)}' (match), '{@code !(a)}' (no match).
 * <p>
 * This expression tests the current input for a match/no-match of a sub-expression
 * itself returning a match if the sub-expression matches (in the case of a &/match predicate)
 * or if the sub-expression does not match (in the case of a !/no-match predicate).
 * <p>
 * This expression never consumes any input and always resets the stack to it's initial position
 * irrespective of the matching result of the sub-expression.
 * <p>
 * This expression has no default {@link net.netnook.repeg.expressions.OnSuccessHandler}.
 */
public final class Predicate extends ParsingExpressionBase implements CompoundExpression {

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
