package net.netnook.qpeg.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.impl.Context.Marker;

public class Choice extends CompoundExpression {

	public static ChoiceBuilder of(ParsingExpressionBuilder... expressions) {
		return new ChoiceBuilder().expressions(expressions);
	}

	public static class ChoiceBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder[] expressions;

		public ChoiceBuilder expressions(ParsingExpressionBuilder[] expressions) {
			this.expressions = expressions;
			return this;
		}

		@Override
		public ChoiceBuilder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		@Override
		public ChoiceBuilder ignore() {
			super.ignore();
			return this;
		}

		@Override
		public Choice build(BuildContext ctxt) {
			return new Choice(this, build(ctxt, expressions));
		}
	}

	private final ParsingExpression[] expressions;

	private Choice(ChoiceBuilder builder, ParsingExpression[] expressions) {
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
	public boolean parse(Context context) {
		Marker startMarker = context.mark();

		boolean success = false;
		for (ParsingExpression expression : expressions) {
			success = expression.parse(context);
			if (success) {
				break;
			}
			context.resetTo(startMarker);
		}

		if (!success) {
			return false;
		}

		onSuccess(context, startMarker);

		return true;
	}
}
