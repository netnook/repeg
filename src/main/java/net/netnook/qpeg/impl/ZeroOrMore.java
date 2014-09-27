package net.netnook.qpeg.impl;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;
import net.netnook.qpeg.parsetree.ZeroOrModeNode;

public class ZeroOrMore extends CompoundExpression {

	public static ZeroOrMoreBuilder of(ParsingExpressionBuilder expression) {
		return new ZeroOrMoreBuilder().expression(expression);
	}

	public static class ZeroOrMoreBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;
		private OnSuccessHandler onSuccess = OnSuccessHandler.NO_OP;

		public ZeroOrMoreBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		public ZeroOrMoreBuilder onSuccess(OnSuccessHandler onSuccess) {
			this.onSuccess = onSuccess;
			return this;
		}

		@Override
		public ZeroOrMoreBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public ZeroOrMoreBuilder ignore(boolean ignore) {
			super.ignore(ignore);
			return this;
		}

		@Override
		public ZeroOrMoreBuilder alias(String alias) {
			super.alias(alias);
			return this;
		}

		@Override
		public ZeroOrMore build(BuildContext ctxt) {
			return new ZeroOrMore(expression.build(ctxt), onSuccess, ignore(), alias());
		}
	}

	private static final Function<? super ZeroOrModeNode, ParseNode> NO_OP = r -> r;

	private final ParsingExpression expression;

	private ZeroOrMore(ParsingExpression expression, OnSuccessHandler onSuccess, boolean ignore, String alias) {
		super(ignore, alias, onSuccess);
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
		Context.Marker startMarker = context.marker();

//		int matchCount = 0;
		//		List<ParseNode> children = new ArrayList<>();

		while (true) {
			Context.Marker fallbackMarker = context.marker();

			boolean match = expression.parse(context);

			if (!match) {
				context.reset(fallbackMarker);
				break;
			}

//			matchCount++;

			//			if (!child.isIgnore()) {
			//				children.add(child);
			//			}
		}

//		if (matchCount == 0) {
//			// context.setPosition(startPosition);  Not necessary since will have been reset in while loop
//			return false;
//		}

		onSuccess.accept(context, startMarker);

//		List<ParseNode> children = context.popTo(stackPosition);
//		if (!isIgnore()) {
//			context.push(new ZeroOrModeNode(context, this, startPosition, context.position(), children));
//		}

		//return onSuccess.apply(result); FIXME: do onsuccess
		return true;
	}
}
