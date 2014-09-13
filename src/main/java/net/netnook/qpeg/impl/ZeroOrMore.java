package net.netnook.qpeg.impl;

import java.util.ArrayList;
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
		private Function<? super ZeroOrModeNode, ParseNode> onSuccess = NO_OP;

		public ZeroOrMoreBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		public ZeroOrMoreBuilder onSuccess(Function<? super ZeroOrModeNode, ParseNode> onSuccess) {
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

	//	public static ZeroOrMore of(ParsingExpression expression) {
	//		return new ZeroOrMore(expression, false, null);
	//	}

	private static final Function<? super ZeroOrModeNode, ParseNode> NO_OP = r -> r;

	private final ParsingExpression expression;
	private final Function<? super ZeroOrModeNode, ParseNode> onSuccess;

	private ZeroOrMore(ParsingExpression expression, Function<? super ZeroOrModeNode, ParseNode> onSuccess, boolean ignore, String alias) {
		super(ignore, alias);
		this.expression = expression;
		this.onSuccess = onSuccess;

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
	public ParseNode parse(Context context) {
		int startPosition = context.position();

		List<ParseNode> children = new ArrayList<>();

		while (true) {
			int fallbackPosition = context.position();
			ParseNode child = expression.parse(context);

			if (child == null) {
				context.setPosition(fallbackPosition);
				break;
			}

			if (!child.isIgnore()) {
				children.add(child);
			}
		}

		return new ZeroOrModeNode(context, this, startPosition, context.position(), children);
	}

	//	@Override
	//	public ZeroOrMore ignore() {
	//		return new ZeroOrMore(expression, true, alias);
	//	}
}
