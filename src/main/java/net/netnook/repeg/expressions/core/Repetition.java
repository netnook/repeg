package net.netnook.repeg.expressions.core;

import java.util.Collections;
import java.util.List;

import net.netnook.repeg.Expression;
import net.netnook.repeg.ExpressionBuilder;
import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.exceptions.InvalidExpressionException;
import net.netnook.repeg.expressions.CompoundExpression;
import net.netnook.repeg.expressions.ExpressionBase;
import net.netnook.repeg.expressions.ExpressionBuilderBase;
import net.netnook.repeg.expressions.RootContext;

/**
 * Repetition expression e.g. '{@code (a)*}' (zero or more), '{@code (a)+}' (one or more),
 * '{@code (a){3}}' (3 times).
 * <p>
 * This expression handles an repetition of a sub-expression, attempting to match the sub-expression
 * repeatedly for a specified minimum and maximum number of times.  This expression will match
 * only if the minimum number of repeats are matched and will never consume more than the maximum
 * number of repetitions.
 * <p>
 * By default, the minimum repetition count is  {@code 0} and the maximum number of repetitions
 * is {@code Integer.MAX_VALUE}.
 * <p>
 * This expression has no default {@link OnSuccessHandler}.
 */
public final class Repetition extends ExpressionBase implements CompoundExpression {

	/**
	 * Same as {@link Repetition#zeroOrMore(ExpressionBuilder)}.
	 *
	 * @param expression the sub-expression to match
	 * @return the new {@link Repetition} expression.
	 */
	public static Builder of(ExpressionBuilder expression) {
		return zeroOrMore(expression);
	}

	/**
	 * Create a {@link Repetition} expression matching exactly one repetition (i.e. minCount=1 and maxCount=1).
	 *
	 * @param expression the sub-expression to match
	 * @return the new {@link Repetition} expression.
	 */
	public static Builder one(ExpressionBuilder expression) {
		return new Builder() //
				.minCount(1) //
				.maxCount(1) //
				.expression(expression);
	}

	/**
	 * Create a {@link Repetition} expression matching zero or more repetitions (i.e. minCount=0 and maxCount=Integer.MAX_VALUE).
	 *
	 * @param expression the sub-expression to match
	 * @return the new {@link Repetition} expression.
	 */
	public static Builder zeroOrMore(ExpressionBuilder expression) {
		return new Builder() //
				.expression(expression);
	}

	/**
	 * Create a {@link Repetition} expression matching one or more repetitions (i.e. minCount=1 and maxCount=Integer.MAX_VALUE).
	 *
	 * @param expression the sub-expression to match
	 * @return the new {@link Repetition} expression.
	 */
	public static Builder oneOrMore(ExpressionBuilder expression) {
		return new Builder() //
				.expression(expression) //
				.minCount(1);
	}

	public static class Builder extends ExpressionBuilderBase {
		private ExpressionBuilder expression;
		private int minCount = 0;
		private int maxCount = Integer.MAX_VALUE;

		public Builder expression(ExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		@Override
		public Builder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		public Builder count(int count) {
			validate(count >= 1, "Invalid expression: count must be >= 1");
			this.minCount = count;
			this.maxCount = count;
			return this;
		}

		public Builder minCount(int minCount) {
			validate(minCount >= 0, "Invalid expression: min count must be >= 0");
			this.minCount = minCount;
			return this;
		}

		public Builder maxCount(int maxCount) {
			validate(maxCount >= 1, "Invalid expression: max count must be >= 1");
			this.maxCount = maxCount;
			return this;
		}

		public Builder maxUnbounded() {
			this.maxCount = Integer.MAX_VALUE;
			return this;
		}

		@Override
		protected Expression doBuild() {
			if (minCount > maxCount) {
				throw new InvalidExpressionException("Invalid expression: minCount > maxCount");
			}

			if (expression instanceof CharacterExpression.Builder) {
				CharacterExpression.Builder characterExpression = (CharacterExpression.Builder) expression;

				// Only if condition is met do we try and optimise
				boolean replaceWithCharExpression = characterExpression.getOnSuccess() == null //
						&& characterExpression.getMinCount() == 1 //
						&& characterExpression.getMaxCount() == 1;

				if (replaceWithCharExpression) {
					CharacterExpression.Builder clone = characterExpression.clone();
					clone.minCount(minCount);
					clone.maxCount(maxCount);
					clone.onSuccess(getOnSuccess());
					return clone.build();
				}
			}

			return new Repetition(this);
		}
	}

	private final Expression expression;
	private final int minCount;
	private final int maxCount;

	private Repetition(Builder builder) {
		super(builder.getOnSuccess());
		this.expression = builder.expression.build();
		this.minCount = builder.minCount;
		this.maxCount = builder.maxCount;

		if (expression instanceof Optional) {
			throw new InvalidExpressionException("Cannot have optional content in repeating construct.");
		}
	}

	public int getMinCount() {
		return minCount;
	}

	public int getMaxCount() {
		return maxCount;
	}

	@Override
	public List<Expression> parts() {
		return Collections.singletonList(expression);
	}

	@Override
	public String buildGrammar() {
		if (minCount == 0 && maxCount == Integer.MAX_VALUE) {
			return "(" + expression.buildGrammar() + ")*";
		} else if (minCount == 1 && maxCount == Integer.MAX_VALUE) {
			return "(" + expression.buildGrammar() + ")+";
		} else if (maxCount == Integer.MAX_VALUE) {
			return "(" + expression.buildGrammar() + "){" + minCount + ",}";
		} else if (minCount == maxCount) {
			return "(" + expression.buildGrammar() + "){" + minCount + "}";
		} else {
			return "(" + expression.buildGrammar() + "){" + minCount + "," + maxCount + "}";
		}
	}

	@Override
	protected boolean doParse(RootContext context, int startPosition, int startStackIdx) {
		int successCount = 0;

		while (successCount < maxCount) {
			int fallbackPosition = context.position();
			int fallbackStackIdx = context.stackSize();

			boolean success = expression.parse(context);

			if (!success) {
				context.resetTo(fallbackPosition, fallbackStackIdx);
				break;
			}

			successCount++;
		}

		return successCount >= minCount;
	}
}

