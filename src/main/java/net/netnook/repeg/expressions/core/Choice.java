package net.netnook.repeg.expressions.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.netnook.repeg.expressions.CompoundExpression;
import net.netnook.repeg.expressions.OnSuccessHandler;
import net.netnook.repeg.expressions.ParsingExpression;
import net.netnook.repeg.expressions.ParsingExpressionBase;
import net.netnook.repeg.expressions.ParsingExpressionBuilder;
import net.netnook.repeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.repeg.expressions.RootContext;

/**
 * Ordered choice expression i.e. '{@code (a | b | c)}'.
 * <p>
 * This expression handles an ordered choice of sub-expressions, attempting to match each one in turn
 * and returning a match when the first sub-expression matches.  If none of the sub-expressions match, this
 * choice expression returns no-match.
 * <p>
 * This expression has no default {@link net.netnook.repeg.expressions.OnSuccessHandler}.
 */
public final class Choice extends ParsingExpressionBase implements CompoundExpression {

	/**
	 * Create a new {@link Choice} expression for the specified sub-expressions.
	 *
	 * @param expressions the sub-expressions to test for match in order.
	 * @return the new {@link Choice} expression.
	 */
	public static Builder of(ParsingExpressionBuilder... expressions) {
		return new Builder().expressions(expressions);
	}

	public static class Builder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder[] expressions;

		public Builder expressions(ParsingExpressionBuilder[] expressions) {
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

	private final ParsingExpression[] expressions;

	private Choice(Builder builder) {
		super(builder.getOnSuccess());
		this.expressions = ParsingExpressionBuilder.build(builder.expressions);
	}

	@Override
	public List<ParsingExpression> parts() {
		return Arrays.asList(expressions);
	}

	@Override
	public String buildGrammar() {
		return Stream.of(expressions) //
				.map(ParsingExpression::buildGrammar) //
				.collect(Collectors.joining(" | ", "(", ")"));
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		for (ParsingExpression expression : expressions) {
			boolean success = expression.parse(context);
			if (success) {
				return true;
			}
			context.resetTo(startPosition, startStackIdx);
		}
		return false;
	}
}
