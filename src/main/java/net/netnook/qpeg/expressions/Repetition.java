package net.netnook.qpeg.expressions;

import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.expressions.Context.Marker;

public class Repetition extends CompoundExpression {

	public static Builder of(ParsingExpressionBuilder expression) {
		return zeroOrMore(expression);
	}

	public static Builder zeroOrMore(ParsingExpressionBuilder expression) {
		return new Builder() //
				.expression(expression);
	}

	public static Builder oneOrMore(ParsingExpressionBuilder expression) {
		return new Builder() //
				.expression(expression) //
				.minCount(1);
	}

	public static class Builder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;
		private int minCount = 0;
		private int maxCount = Integer.MAX_VALUE;

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

		@Override
		public Repetition doBuild(BuildContext ctxt) {
			if (minCount > maxCount) {
				throw new InvalidExpressionException("Invalid expression: minCount > maxCount");
			}

			return new Repetition(this, expression.build(ctxt));
		}
	}

	private final ParsingExpression expression;
	private final int minCount;
	private final int maxCount;

	private Repetition(Builder builder, ParsingExpression expression) {
		super(builder);
		this.expression = expression;
		this.minCount = builder.minCount;
		this.maxCount = builder.maxCount;

		if (expression instanceof Optional) {
			throw new InvalidExpressionException("Cannot have optional content in repeating construct.");
		}
	}

	@Override
	public List<ParsingExpression> parts() {
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
	protected boolean parseImpl(Context context, Marker startMarker) {
		int successCount = 0;

		while (successCount < maxCount) {
			Marker fallbackMarker = context.updateMark();

			boolean success = expression.parse(context);

			if (!success) {
				context.resetTo(fallbackMarker);
				break;
			}

			successCount++;
		}

		return successCount >= minCount;
	}
}

