package net.netnook.qpeg.impl;

import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.impl.Context.Marker;

public class OneOrMore extends CompoundExpression {

	public static OneOrMoreBuilder of(ParsingExpressionBuilder expression) {
		return new OneOrMoreBuilder().expression(expression);
	}

	public static class OneOrMoreBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;

		public OneOrMoreBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		@Override
		public OneOrMoreBuilder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		@Override
		public OneOrMoreBuilder ignore() {
			super.ignore();
			return this;
		}

		@Override
		public OneOrMore build(BuildContext ctxt) {
			return new OneOrMore(this, expression.build(ctxt));
		}
	}

	private final ParsingExpression expression;

	private OneOrMore(OneOrMoreBuilder builder, ParsingExpression expression) {
		super(builder);
		this.expression = expression;

		if (expression.isIgnore()) {
			throw new IllegalArgumentException("Cannot have ignorable content in repeating construct.");
		}
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
		return "(" + expression.buildGrammar() + ")*";
	}

	@Override
	public boolean parse(Context context) {
		Marker startMarker = context.marker();

		int successCount = 0;

		while (true) {
			Marker fallbackMarker = context.marker();

			boolean success = expression.parse(context);

			if (!success) {
				context.reset(fallbackMarker);
				break;
			}

			successCount++;
		}

		if (successCount == 0) {
			return false;
		}

		onSuccess(context, startMarker);

		return true;
	}
}
