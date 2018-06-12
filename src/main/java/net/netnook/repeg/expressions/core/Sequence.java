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
 * Sequence expression i.e. '{@code (a b c)}'.
 * <p>
 * This expression handles an sequence of sub-expressions, attempting to match each sub-expression
 * in sequence and returning a match if all sub-expressions match.
 * <p>
 * This expression has no default {@link net.netnook.repeg.expressions.OnSuccessHandler}.
 */
public final class Sequence extends ParsingExpressionBase implements CompoundExpression {

	/**
	 * Create a new {@link Sequence} expression with the specified sub-expressions.
	 *
	 * @param expressions sub-expressions to match
	 * @param <T>
	 * @return the new {@link Sequence} expression.
	 */
	public static <T extends ParsingExpressionBuilder> Builder of(T... expressions) {
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
		protected Sequence doBuild() {
			return new Sequence(this);
		}
	}

	private final ParsingExpression[] expressions;

	private Sequence(Builder builder) {
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
				.collect(Collectors.joining(" ", "(", ")"));
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		for (ParsingExpression expression : expressions) {
			boolean success = expression.parse(context);
			if (!success) {
				return false;
			}
		}
		return true;
	}
}
