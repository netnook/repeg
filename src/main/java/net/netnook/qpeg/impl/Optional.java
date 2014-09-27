package net.netnook.qpeg.impl;

import java.util.Arrays;
import java.util.List;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.impl.Context.Marker;

public class Optional extends CompoundExpression {

	public static OptionalBuilder of(ParsingExpressionBuilder expression) {
		return new OptionalBuilder().expression(expression);
	}

	public static class OptionalBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;

		public OptionalBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		@Override
		public OptionalBuilder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		@Override
		public OptionalBuilder ignore() {
			super.ignore();
			return this;
		}

		@Override
		public Optional build(BuildContext ctxt) {
			return new Optional(this, expression.build(ctxt));
		}
	}

	private final ParsingExpression expression;

	private Optional(OptionalBuilder builder, ParsingExpression expression) {
		super(builder);
		this.expression = expression;
	}

	@Override
	public List<ParsingExpression> parts() {
		return Arrays.asList(expression);
	}

	@Override
	public String buildGrammar() {
		return "(" + expression.buildGrammar() + ")?";
	}

	@Override
	public boolean parse(Context context) {
		Marker startMarker = context.mark();

		boolean success = expression.parse(context);
		if (!success) {
			context.resetTo(startMarker);
		}

		onSuccess(context, startMarker);

		return true;
	}
}
