package net.netnook.qpeg.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;

public class Sequence extends CompoundExpression {

	public static SequenceBuilder of(ParsingExpressionBuilder... expressions) {
		return new SequenceBuilder().expressions(expressions);
	}

	public static class SequenceBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder[] expressions;
		private OnSuccessHandler onSuccess = OnSuccessHandler.NO_OP;

		public SequenceBuilder expressions(ParsingExpressionBuilder[] expressions) {
			this.expressions = expressions;
			return this;
		}

		public SequenceBuilder onSuccess(OnSuccessHandler onSuccess) {
			this.onSuccess = onSuccess;
			return this;
		}

		@Override
		public SequenceBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public SequenceBuilder ignore(boolean ignore) {
			super.ignore(ignore);
			return this;
		}

		@Override
		public SequenceBuilder alias(String alias) {
			super.alias(alias);
			return this;
		}

		@Override
		public Sequence build(BuildContext ctxt) {
			return new Sequence(build(ctxt, expressions), onSuccess, ignore(), alias());
		}
	}

//	private static final OnSuccessHandler NO_OP = (r,i) -> {};

	private final ParsingExpression[] expressions;
//	private final OnSuccessHandler onSuccess;

	private Sequence(ParsingExpression[] expressions, OnSuccessHandler onSuccess, boolean ignore, String alias) {
		super(ignore, alias, onSuccess);
		this.expressions = expressions;
//		this.onSuccess = onSuccess;
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
	public boolean parse(Context context) {
		Context.Marker marker = context.marker();

//		List<ParseNode> children = new ArrayList<>();

		boolean match = true;
		for (ParsingExpression expression : expressions) {
			match = match && expression.parse(context);
			if (!match) {
				break;
			}
//			if (!expression.isIgnore()) {
//				context.push();
//				children.add(child);
//			}
		}

		if (!match) {
			context.reset(marker);
			return false;
		}

//		List<ParseNode> children = context.popTo(stackPosition);

		onSuccess.accept(context, marker);

//		context.resetStack(stackPosition);

//		if (!isIgnore()) {
//			context.push(new SequenceNode(context, this, startPosition, context.position(), children));
//		}

		//return onSuccess.apply(result);
		return true;
	}
}
