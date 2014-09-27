package net.netnook.qpeg.impl;

import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;

public class OneOrMore extends CompoundExpression {

	public static OneOrMoreBuilder of(ParsingExpressionBuilder expression) {
		return new OneOrMoreBuilder().expression(expression);
	}

	public static class OneOrMoreBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;
		private OnSuccessHandler onSuccess = OnSuccessHandler.NO_OP;

		public OneOrMoreBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		public OneOrMoreBuilder onSuccess(OnSuccessHandler onSuccess) {
			this.onSuccess = onSuccess;
			return this;
		}

		@Override
		public OneOrMoreBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public OneOrMoreBuilder ignore(boolean ignore) {
			super.ignore(ignore);
			return this;
		}

		@Override
		public OneOrMoreBuilder alias(String alias) {
			super.alias(alias);
			return this;
		}

		@Override
		public OneOrMore build(BuildContext ctxt) {
			return new OneOrMore(expression.build(ctxt), onSuccess, ignore(), alias());
		}
	}

//	private static final Function<? super OneOrMoreNode, ParseNode> NO_OP = r -> r;

	private final ParsingExpression expression;

	private OneOrMore(ParsingExpression expression, OnSuccessHandler onSuccess, boolean ignore, String alias) {
		super(ignore, alias, onSuccess);
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
		Context.Marker startMarker = context.marker();

		int matchCount = 0;
//		List<ParseNode> children = new ArrayList<>();

		while (true) {
			Context.Marker fallbackMarker = context.marker();

			boolean match = expression.parse(context);

			if (!match) {
				context.reset(fallbackMarker);
				break;
			}

			matchCount++;

//			if (!child.isIgnore()) {
//				children.add(child);
//			}
		}

		if (matchCount == 0) {
			// Note: not necessary as done as part of loop above
			// context.setPosition(stackPosition);
			// context.resetStack(stackPosition);
			return false;
		}

		onSuccess.accept(context, startMarker);
//		List<ParseNode> children = context.popTo(stackPosition);
//		if (!isIgnore()) {
//			context.push(new OneOrMoreNode(context, this, startPosition, context.position(), children));
//		}

		//return onSuccess.apply(result); FIXME: do onsuccess
		return true;
	}
}
