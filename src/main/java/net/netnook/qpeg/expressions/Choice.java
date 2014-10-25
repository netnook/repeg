package net.netnook.qpeg.expressions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.netnook.qpeg.expressions.Context.Marker;

public class Choice extends CompoundExpression {

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
		public Choice build(BuildContext ctxt) {
			return new Choice(this, build(ctxt, expressions));
		}
	}

	private final ParsingExpression[] expressions;

	private Choice(Builder builder, ParsingExpression[] expressions) {
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
				.collect(Collectors.joining(" | ", "(", ")"));
	}

	@Override
	protected boolean parseImpl(Context context, Marker startMarker) {
		for (ParsingExpression expression : expressions) {
			boolean success = expression.parse(context);
			if (success) {
				return true;
			}
			context.resetTo(startMarker);
		}
		return false;
	}
}
