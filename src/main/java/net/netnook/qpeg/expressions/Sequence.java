package net.netnook.qpeg.expressions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Sequence extends CompoundExpression {

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
		public Builder ignore() {
			super.ignore();
			return this;
		}

		@Override
		protected Sequence doBuild(BuildContext ctxt) {
			return new Sequence(this, build(ctxt, expressions));
		}
	}

	private final ParsingExpression[] expressions;

	private Sequence(Builder builder, ParsingExpression[] expressions) {
		super(builder);
		this.expressions = expressions;
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
