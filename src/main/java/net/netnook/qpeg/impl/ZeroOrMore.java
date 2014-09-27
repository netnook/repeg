package net.netnook.qpeg.impl;

import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.impl.Context.Marker;

public class ZeroOrMore extends CompoundExpression {

	public static ZeroOrMoreBuilder of(ParsingExpressionBuilder expression) {
		return new ZeroOrMoreBuilder().expression(expression);
	}

	public static class ZeroOrMoreBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;

		public ZeroOrMoreBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		@Override
		public ZeroOrMoreBuilder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		@Override
		public ZeroOrMoreBuilder ignore() {
			super.ignore();
			return this;
		}

		@Override
		public ZeroOrMore build(BuildContext ctxt) {
			return new ZeroOrMore(this, expression.build(ctxt));
		}
	}

	private final ParsingExpression expression;

	private ZeroOrMore(ZeroOrMoreBuilder builder, ParsingExpression expression) {
		super(builder);
		this.expression = expression;

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

		while (true) {
			Marker fallbackMarker = context.marker();

			boolean success = expression.parse(context);

			if (!success) {
				context.reset(fallbackMarker);
				break;
			}
		}

		onSuccess(context, startMarker);

		return true;
	}
}
