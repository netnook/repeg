package net.netnook.qpeg.expressions;

public abstract class Predicate extends SimpleExpression {

	protected Predicate(ParsingExpressionBuilderBase builder) {
		super(builder);
		if (!builder.isIgnore()) {
			throw new IllegalStateException();
		}
	}

	public static TruePredicateBuilder match(ParsingExpressionBuilder expression) {
		return new TruePredicateBuilder().expression(expression);
	}

	public static class TruePredicateBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;

		public TruePredicateBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		@Override
		public TruePredicateBuilder ignore() {
			throw new UnsupportedOperationException();
		}

		@Override
		public TruePredicateBuilder onSuccess(OnSuccessHandler onSuccess) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TruePredicate build(BuildContext ctxt) {
			return new TruePredicate(this, expression.build(ctxt));
		}
	}

	private static class TruePredicate extends Predicate {

		private final ParsingExpression expression;

		private TruePredicate(TruePredicateBuilder builder, ParsingExpression expression) {
			super(builder);
			this.expression = expression;
		}

		@Override
		public String buildGrammar() {
			return "&(" + expression.buildGrammar() + ")";
		}

		@Override
		public boolean parse(Context context) {
			Context.Marker marker = context.mark();

			boolean success = expression.parse(context);

			context.resetTo(marker);

			return success;
		}
	}

	public static FalsePredicateBuilder not(ParsingExpressionBuilder expression) {
		return new FalsePredicateBuilder().expression(expression);
	}

	public static class FalsePredicateBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;

		public FalsePredicateBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		@Override
		public FalsePredicateBuilder ignore() {
			throw new UnsupportedOperationException();
		}

		@Override
		public FalsePredicateBuilder onSuccess(OnSuccessHandler onSuccess) {
			throw new UnsupportedOperationException();
		}

		@Override
		public FalsePredicate build(BuildContext ctxt) {
			return new FalsePredicate(this, expression.build(ctxt));
		}
	}

	private static class FalsePredicate extends Predicate {
		private final ParsingExpression expression;

		private FalsePredicate(FalsePredicateBuilder builder, ParsingExpression expression) {
			super(builder);
			this.expression = expression;
		}

		@Override
		public String buildGrammar() {
			return "!(" + expression.buildGrammar() + ")";
		}

		@Override
		public boolean parse(Context context) {
			Context.Marker marker = context.mark();

			boolean success = !expression.parse(context);

			context.resetTo(marker);

			return success;
		}
	}

}
