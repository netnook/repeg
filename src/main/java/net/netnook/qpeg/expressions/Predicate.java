package net.netnook.qpeg.expressions;

public final class Predicate extends SimpleExpression {

	public static Builder match(ParsingExpressionBuilder expression) {
		return new Builder().expression(expression, false);
	}

	public static Builder not(ParsingExpressionBuilder expression) {
		return new Builder().expression(expression, true);
	}


	public static class Builder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;
		private boolean invert;

		public Builder expression(ParsingExpressionBuilder expression, boolean invert) {
			this.expression = expression;
			this.invert = invert;
			return this;
		}

		@Override
		public Builder ignore() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Builder onSuccess(OnSuccessHandler onSuccess) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Predicate build(BuildContext ctxt) {
			return new Predicate(this, expression.build(ctxt));
		}
	}

	protected final ParsingExpression expression;
	protected final boolean invert;

	protected Predicate(Builder builder, ParsingExpression expression) {
		super(builder);
		if (!builder.isIgnore()) {
			throw new IllegalStateException();
		}
		this.expression = expression;
		this.invert = builder.invert;
	}

	@Override
	public boolean parse(Context context) {
		Context.Marker marker = context.mark();

		boolean match = expression.parse(context);

		boolean success = invert ^ match;

		context.resetTo(marker);

		return success;
	}

	@Override
	public String buildGrammar() {
		return invert  //
				? "!(" + expression.buildGrammar() + ")" //
				: "&(" + expression.buildGrammar() + ")";
	}
}
