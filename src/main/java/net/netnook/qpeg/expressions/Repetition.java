package net.netnook.qpeg.expressions;

import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.expressions.Context.Marker;

public class Repetition extends CompoundExpression {

	public static RepetitionBuilder zeroOrMore(ParsingExpressionBuilder expression) {
		return new RepetitionBuilder() //
				.expression(expression);
	}

	public static RepetitionBuilder oneOrMore(ParsingExpressionBuilder expression) {
		return new RepetitionBuilder() //
				.expression(expression) //
				.minCount(1);
	}

	public static class RepetitionBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;
		private int minCount = 0;
		private int maxCount = Integer.MAX_VALUE;

		public RepetitionBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		@Override
		public RepetitionBuilder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		@Override
		public RepetitionBuilder ignore() {
			super.ignore();
			return this;
		}

		public RepetitionBuilder minCount(int minCount) {
			this.minCount = minCount;
			return this;
		}

		public RepetitionBuilder maxCount(int maxCount) {
			this.maxCount = maxCount;
			return this;
		}

		@Override
		public Repetition build(BuildContext ctxt) {
			return new Repetition(this, expression.build(ctxt));
		}
	}

	private final ParsingExpression expression;
	private final int minCount;
	private final int maxCount;

	private Repetition(RepetitionBuilder builder, ParsingExpression expression) {
		super(builder);
		this.expression = expression;
		this.minCount = builder.minCount;
		this.maxCount = builder.maxCount;

		if (expression instanceof Optional) {
			throw new IllegalArgumentException("Cannot have optional content in repeating construct.");
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
		} else if (minCount == maxCount) {
			return "(" + expression.buildGrammar() + "){" + minCount + "," + maxCount + "}";
		} else {
			return "(" + expression.buildGrammar() + "){" + minCount + "}";
		}
	}

	@Override
	public boolean parse(Context context) {
		Marker startMarker = context.mark();

		int successCount = 0;

		while (true) {
			Marker fallbackMarker = context.mark();

			boolean success = expression.parse(context);

			if (!success) {
				context.resetTo(fallbackMarker);
				break;
			}

			successCount++;
		}

		if (successCount < minCount || successCount > maxCount) {
			return false;
		}

		onSuccess(context, startMarker);

		return true;
	}
}

